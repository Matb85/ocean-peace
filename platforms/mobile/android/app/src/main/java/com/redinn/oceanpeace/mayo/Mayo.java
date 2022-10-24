package com.redinn.oceanpeace.mayo;

import static com.redinn.oceanpeace.FunctionBase.JSONArrayGetIndexOf;
import static com.redinn.oceanpeace.FunctionBase.JSONArrayOptElement;
import static com.redinn.oceanpeace.FunctionBase.getDayOfWeekStringShort;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;

import com.redinn.oceanpeace.FunctionBase;
import com.redinn.oceanpeace.R;
import com.redinn.oceanpeace.goals.Goals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Mayo extends AccessibilityService {

    // region variables
        String closedPackageName = null;
        long closedChangeTime = SystemClock.uptimeMillis();
        String dayOfWeek = null;
        boolean closedIsInArrays = true;

        /**
         * JSONArray of this type elements: <br/>
         *
         *  { <br/>
         * 	    &emsp "packageName": "com.facebook.katana", <br/>
         * 	    &emsp "goals": [ <br/>
         * 		    &emsp&emsp "goal1234", <br/>
         * 		    &emsp&emsp "goal1234", <br/>
         *  	&emsp ], <br/>
         *      &emsp "limit": 120 <br/>
         *      &emsp "limitGoal": "goal1234" <br/>
         *  } <br/>
         */
        private static JSONArray notify = new JSONArray();

        /**
         * JSONArray of this type elements: <br/>
         *
         *  { <br/>
         * 	    &emsp "packageName": "com.facebook.katana", <br/>
         * 	    &emsp "goals": [ <br/>
         * 		    &emsp&emsp "goal1234", <br/>
         * 		    &emsp&emsp "goal1234", <br/>
         *  	&emsp ], <br/>
         *      &emsp "limit": 120 <br/>
         *      &emsp "limitGoal": "goal1234" <br/>
         *  } <br/>
         */
        private static JSONArray close = new JSONArray();

        /**
         * JSONArray of this type elements: <br/>
         *
         * [<br/>
         *  &emsp "com.facebook.katana", "com.instagram.katana:, <br/>
         * ]<br/>
         *
         */
        private static JSONArray focus = new JSONArray();

        private Timer timer = new Timer("mayo.Timer", false);
    //endregion


    //region Accessibility

    MayoReceiver mReceiver = new MayoReceiver();

    @Override
    public void onServiceConnected() {

        IntentFilter filter = new IntentFilter();
        filter.addAction("ocean.waves.mayo.force_update");
        filter.addAction("ocean.waves.mayo.change");
        filter.addAction("ocean.waves.mayo.delete");
        registerReceiver(mReceiver, filter);
        dayOfWeek = getDayOfWeekStringShort();

        // region creating lists of packageNames
        try {
            loadTodayGoals();
            updatePackagesArrays();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //endregion

    }



    // TODO: refactor code by using functions and workers or threads
    // TODO: make clear documentation & comments
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        ApplicationInfo info =getApplicationInfo();

        try {
            // run function
            run(event.getPackageName().toString(), event.getEventTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        allGoalsToFile();
        unregisterReceiver(mReceiver);
    }
    //endregion



    // region Mayo

    private void run(String openedPackageName, long eventTime) throws JSONException {
        //checking if current window is a part of the same app as previous
        if (openedPackageName.equals(closedPackageName))
            return;

        timer.cancel();

        //checking if window is blocked by FOCUS session
        if (FunctionBase.JSONArrayOptElement(focus, openedPackageName) != null) {
            mayoClose(openedPackageName);
            return;
        }

        // variables
        long duration = 0;


            // get the session duration
            duration = eventTime - closedChangeTime;


            //checking if the date changed
            //if not then update arrays
            if (!dayOfWeek.equals(getDayOfWeekStringShort())) {
                // get the time spent after midnight
                long pastMidnightTime =
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * (60 * 60 * 1000) +
                                Calendar.getInstance().get(Calendar.MINUTE) * (60 * 1000) +
                                Calendar.getInstance().get(Calendar.SECOND) * (1000);
                // update yesterday's part of session
                if (duration - pastMidnightTime >0)
                    updateGoals(closedPackageName, duration - pastMidnightTime);


                //update history
                endDay(dayOfWeek);
                // update dayOfWeek
                dayOfWeek = getDayOfWeekStringShort();

                // save today goals
                allGoalsToFile();
                // update arrays
                loadTodayGoals();
                updatePackagesArrays();

                duration = pastMidnightTime;


            }
        if (closedIsInArrays) {
            updateGoals(closedPackageName, duration);
            updateAPI(todayGoals);
        }
        closedChangeTime = eventTime;
        Log.i("MAYO", "duration: " + duration);
        Log.i("MAYO", "goals update: " + todayGoals.toString());
        Log.i("MAYO", "window: " + openedPackageName);

        //check if window is blocked by goals
        JSONObject closeElement = FunctionBase.JSONArrayOptElement(close, "packageName", openedPackageName);
        JSONObject notifyElement = FunctionBase.JSONArrayOptElement(notify, "packageName", openedPackageName);
        if ( closeElement != null) {

            timer = new Timer("mayo.Timer");
            Date closeTime = new Date();
            closeTime.setTime(
                    Calendar.getInstance().getTimeInMillis()
                    + closeElement.getLong("limit")
                    - FunctionBase.JSONArrayOptElement(todayGoals, Goals.NAME, closeElement.getString("limitGoal")).getLong(Goals.SESSIONTIME)
            );
            timer.schedule(new MClose(), closeTime);

            closedIsInArrays = true;
        }

        //check if window is set to notify by goals
        else if (notifyElement != null) {

            timer = new Timer("mayo.Timer");
            Date closeTime = new Date();
            closeTime.setTime(
                    Calendar.getInstance().getTimeInMillis()
                            + closeElement.getLong("limit")
                            - FunctionBase.JSONArrayOptElement(todayGoals, Goals.NAME, closeElement.getString("limitGoal")).getLong(Goals.SESSIONTIME)
            );
            //TODO: Make notification window appear
            timer.schedule(new MClose(), closeTime);
            closedIsInArrays = true;
        }
        else {
            closedIsInArrays = false;
        }

        closedPackageName = openedPackageName;


    }
    void endDay(String dayOfWeek) {
        for (int i=0; i< todayGoals.length(); i++) {
            try {
                JSONObject goal = todayGoals.getJSONObject(i);
                JSONArray array = new JSONArray(goal.getString(Goals.SESSIONSHISTORY));
                // update history
                if (goal.getLong(Goals.SESSIONTIME) > (Long.parseLong(goal.getString(Goals.LIMIT)) * 1000 * 60))
                    array.put(new JSONObject().put("status", false).put("time", goal.getLong(Goals.SESSIONTIME)).put("day", dayOfWeek));
                else
                    array.put(new JSONObject().put("status", true).put("time", goal.getLong(Goals.SESSIONTIME)).put("day", dayOfWeek));
                // remove too old records
                if (array.length()>7)
                    array.remove(0);
                goal.put(Goals.SESSIONSHISTORY, array.toString());

                //zero the sessionTime
                goal.put(Goals.SESSIONTIME, 0L);

                changeTodayGoals(goal);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    void updateGoals(String packageName, long sessionTime) throws JSONException {
        JSONObject closePack = JSONArrayOptElement(close, "packageName", packageName);
        if (closePack != null) {
            JSONArray goalsArray = closePack.getJSONArray("goals");
            for (int i=0; i< goalsArray.length(); i++) {
                JSONObject goal = JSONArrayOptElement(todayGoals, Goals.ID, goalsArray.getString(i));
                long time = goal.getLong(Goals.SESSIONTIME) + sessionTime;
                long limit = goal.getLong(Goals.LIMIT);
                if (time > limit) {

                }
                goal.put(Goals.SESSIONTIME, time);
                goal.put(Goals.SESSIONUPDATE, Calendar.getInstance().getTime().toString());
                saveGoal(goal);
            }
        }


        JSONObject notifyPack = JSONArrayOptElement(notify, "packageName", packageName);
        if (notifyPack != null) {
            JSONArray goalsArray = notifyPack.getJSONArray("goals");
            for (int i=0; i< goalsArray.length(); i++) {
                JSONObject goal = JSONArrayOptElement(todayGoals, Goals.ID, goalsArray.getString(i));
                long time = goal.getLong(Goals.SESSIONTIME) + sessionTime;
                long limit = goal.getLong(Goals.LIMIT);
                if (time > limit) {

                }
                goal.put(Goals.SESSIONTIME, time);
                goal.put(Goals.SESSIONUPDATE, Calendar.getInstance().getTime().toString());
                saveGoal(goal);
            }
        }
    }


    class MClose extends TimerTask {
        @Override
        public void run() {
            Looper.prepare();

            new Handler().post(new Runnable() {
                @Override
                public void run() {

                    View testView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup, null);
                    WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                            PixelFormat.TRANSLUCENT);
                    params.gravity = Gravity.RIGHT | Gravity.TOP;
                    params.setTitle("Load Average");
                    WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
                    wm.addView(testView, params);

                    TextView text = (TextView) testView.findViewById(R.id.closeText);
                    text.setText(closedPackageName); // OPTION: packageName
                    testView.findViewById(R.id.closePopupBtn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // close app function
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);

                            wm.removeView(testView);
                        }
                    });

                }
            });

            Looper.loop();
        }
    }

    // TODO: make closing app function
    private void mayoClose(String packageName) {
        // displaying popup
        View testView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup, null);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        params.setTitle("Load Average");
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(testView, params);

        TextView text = (TextView) testView.findViewById(R.id.closeText);
        text.setText(packageName);
        testView.findViewById(R.id.closePopupBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close app function
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);

                wm.removeView(testView);
            }
        });
    }

    // TODO: make notification function
    private void mayoNotify() {

    }


    //endregion


    //region Focus

    // TODO: rewrite Focus, to make it works with MAYO2.0
    public static void startFocus(JSONArray packages) {
        focus = packages;
    }
    public static void stopFocus() {
        focus = new JSONArray();
    }

    //endregion



    // region today's goals
    /**
     * Array containing goals which are scheduled for today
     */
    public JSONArray todayGoals = new JSONArray();

    private void allGoalsToFile() {
        Goals goalsClass = new Goals(getApplicationContext());
        for (int i=0; i< todayGoals.length(); i++) {
            try {
                goalsClass.saveGoal(todayGoals.getJSONObject(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveGoal(JSONObject goal) throws JSONException{
        int index = JSONArrayGetIndexOf(todayGoals, Goals.ID, goal.getString(Goals.ID));
        if (index >= 0) {
            todayGoals.put(index, goal);
        }
    }

    private void loadTodayGoals() throws JSONException{
        Log.i("MAYO", "loadTodayGoals: loading...");
        // empty the array
        Log.i("MAYO", "loadTodayGoals: clearing array...");
        todayGoals = new JSONArray();
        // get all goals
        Goals goalsClass = new Goals(getApplicationContext());
        JSONArray goals = goalsClass.getAllGoals();

        //iterate through all goals
        for (int i=0; i<goals.length(); i++) {
            // if goal is not set for today, skip
            if (FunctionBase.JSONArrayOptElement(
                    new JSONArray(goals.getJSONObject(i).getString(Goals.ACTIVEDAYS)),
                    dayOfWeek) == null)
                continue;

            //add goal to today goals
            todayGoals.put(goals.getJSONObject(i));
        }
        Log.i("MAYO", "todayGoals: " + todayGoals.toString());
        Log.i("MAYO", "loadTodayGoals: done!");
    }

    public void changeTodayGoals(JSONObject goal) throws  JSONException {
        Log.i("MAYO", "updateTodayGoals: updating...");
        // check if the goal with this id is in the array
        JSONObject previous = JSONArrayOptElement(todayGoals, Goals.ID, goal.getString("id"));
        if(previous != null) {
            // get index of previous goal in array
            int index = JSONArrayGetIndexOf(todayGoals, Goals.ID, previous.getString(Goals.ID));

            //BUG: Progress do not synchronize

            // TODO: Fix progress synchronization while updating goal

            // date is possible to be null
            try {
                // parse Date from both goals
                Date prevUpdate = java.sql.Date.valueOf(previous.getString(Goals.SESSIONUPDATE));
                Date curUpdate = java.sql.Date.valueOf(goal.getString(Goals.SESSIONUPDATE));
                // check if previous has more up-to-date data
                if (prevUpdate.after(curUpdate)) {
                    //update data
                    goal.put(Goals.SESSIONUPDATE, Calendar.getInstance().getTime().toString());
                    goal.put(Goals.SESSIONSHISTORY, previous.getString(Goals.SESSIONSHISTORY));
                    goal.put(Goals.SESSIONTIME, previous.getString(Goals.SESSIONTIME));
                }
            } catch (NullPointerException | IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                // replace goal in array
                todayGoals.put(index, goal);
            }

        }
        else {
            // if goal doesn't exist add it
            todayGoals.put(goal);
        }

        updatePackagesArrays();

        Log.i("MAYO", "updateTodayGoals: " + todayGoals.toString());
        Log.i("MAYO", "updateTodayGoals: updated!");
    }

    public void deleteTodayGoal(JSONObject goal) throws JSONException {
        Log.i("MAYO", "deleteTodayGoal: removing goal...");
        // getting goal index
        int index = JSONArrayGetIndexOf(todayGoals, Goals.ID, goal.getString(Goals.ID));
        // if goal is not in the array
        if (index < 0)
            return;

        // remove goal
        todayGoals.remove(index);
        // update arrays
        updatePackagesArrays();

        Log.i("MAYO", "deleteTodayGoal: goal removed from array!");
    }
    //endregion



    // region Updating Goals' arrays functions

        /**
         *
         * Updates <i>notify</i> and <i>close</i> arrays
         *
         * @throws JSONException
         */
        public void updatePackagesArrays() throws JSONException {
            //clear arrays
            Log.i("MAYO", "updateGoalsArray: clearing arrays...");
            notify = new JSONArray();
            close = new JSONArray();

            Log.i("MAYO", "updateGoalsArray: updating for " + dayOfWeek);

            JSONObject goal;


            // iterating through the all goals
            for (int i = 0; i < todayGoals.length(); i++) {
                goal = todayGoals.getJSONObject(i);

                // checking the goal's limit action type (notify or close)
                // & calling functions to put packageNames to the correct array
                switch (goal.getString("limitActionType")) {
                    case "Notification":
                            updateNotifyArray(goal);
                        break;
                    case "Close app":
                            updateCloseArray(goal);
                        break;
                    default:
                        //handle error
                            Log.e("MAYO", "updateGoalsArray: Error occurred while checking goal's limit action type\n"
                                + "\t" + goal.getString("id") + ":\"limitActionType\" filed my be empty or may contain not supported type of action");
                        break;
                }
            }

            Log.i("MAYO", "updateGoalsArray: arrays updated!" +
                    "\n\tnotify: " + notify.toString() +
                    "\n\tclose: " + close.toString() +
                    "\n\tgoals: " + todayGoals.toString());
        }

        static void updateNotifyArray (JSONObject goal) throws JSONException {
            JSONArray packageNames;

            packageNames = new JSONArray( goal.getString("apps") );

            long goalLimit = Long.parseLong(goal.getString(Goals.LIMIT)) * 60 * 1000;


            // adding packageNames to the notify array
            for (int i = 0; i < packageNames.length(); i++) {

                String limitName = goal.getString(Goals.NAME);

                long limit = -1;
                //checking if the packageName already exist in array
                JSONObject temp = JSONArrayOptElement(notify, "packageName" ,packageNames.getString(i));
                if (temp != null) {
                    //check if the goal is not already present
                    if (FunctionBase.JSONArrayOptElement(temp.getJSONArray("goals"), goal.getString(Goals.ID)) == null)
                        temp.getJSONArray("goals").put(goal.getString(Goals.ID));

                    if (goalLimit < temp.getLong("limit")) {
                        limit = goalLimit;
                    } else {
                        limit = temp.getLong("limit");
                        limitName = temp.getString("limitGoal");
                    }

                    continue;
                }

                // create object which will be stored
                JSONObject element = new JSONObject()
                                .put("packageName", packageNames.getString(i))
                                .put("goals", new JSONArray().put(goal.getString(Goals.ID)))
                                .put("limit", limit == -1 ? goalLimit : limit)
                                .put("limitGoal", limitName);

                // store object
                notify.put(element);

            }
        }

        static void updateCloseArray (JSONObject goal) throws JSONException {
            JSONArray packageNames;

            packageNames = new JSONArray( goal.getString("apps") );

            long goalLimit = Long.parseLong(goal.getString(Goals.LIMIT)) * 60 * 1000;

            // adding packageNames to the notify array
            for (int i = 0; i < packageNames.length(); i++) {

                String limitName = goal.getString(Goals.NAME);

                long limit = -1;
                //checking if the packageName already exist in array
                JSONObject temp = JSONArrayOptElement(close, "packageName" ,packageNames.getString(i));
                if (temp != null) {
                    //check if the goal is not already present
                    if (FunctionBase.JSONArrayOptElement(temp.getJSONArray("goals"), goal.getString(Goals.ID)) == null)
                        temp.getJSONArray("goals").put(goal.getString(Goals.ID));

                    if (goalLimit < temp.getLong("limit"))
                        limit = goalLimit;
                    else {
                        limit = temp.getLong("limit");
                        limitName = temp.getString("limitGoal");
                    }

                    continue;
                }

                // create object which will be stored
                JSONObject element = new JSONObject()
                        .put("packageName", packageNames.getString(i))
                        .put("goals", new JSONArray().put(goal.getString(Goals.ID)))
                        .put("limit", limit == -1 ? goalLimit : limit)
                        .put("limitGoal", limitName);

                // store object
                close.put(element);

            }
        }



    // endregion

    void updateAPI(JSONArray tGoals) {
            Intent intent = new Intent();
            intent.setAction("ocean.waves.mapi.update");
            intent.putExtra("todayGoals", todayGoals.toString());
            sendBroadcast(intent);
    }

    public class MayoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "ocean.waves.mayo.force_update":
                    updateAPI(todayGoals);
                    break;
                case "ocean.waves.mayo.delete":
                    try {
                        deleteTodayGoal(new JSONObject(intent.getStringExtra("goal")));
                        updateAPI(todayGoals);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "ocean.waves.mayo.change":
                    Log.i("MAYO", "onReceive: received change request");
                    try {
                        changeTodayGoals(new JSONObject(intent.getStringExtra("goal")));
                        updateAPI(todayGoals);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    // endregion
}

