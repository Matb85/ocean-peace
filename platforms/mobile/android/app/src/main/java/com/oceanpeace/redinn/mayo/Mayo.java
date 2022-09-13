package com.oceanpeace.redinn.mayo;

import static com.oceanpeace.redinn.FunctionBase.JSONArrayGetIndexOf;
import static com.oceanpeace.redinn.FunctionBase.JSONArrayOptElement;
import static com.oceanpeace.redinn.FunctionBase.getDayOfWeekStringShort;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;

import com.oceanpeace.redinn.FunctionBase;
import com.oceanpeace.redinn.R;
import com.oceanpeace.redinn.goals.Goals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


public class Mayo extends AccessibilityService {

    // region variables
        private String closedPackageName = null;
        private long closedChangeTime = SystemClock.uptimeMillis();
        private static String dayOfWeek = null;
        private boolean closedIsInArryas = true;

        /**
         * JSONArray of this type elements: <br/>
         *
         *  { <br/>
         * 	    &emsp "packageName": "com.facebook.katana", <br/>
         * 	    &emsp "goals": [ <br/>
         * 		    &emsp&emsp "goal1234", <br/>
         * 		    &emsp&emsp "goal1234", <br/>
         *  	&emsp ], <br/>
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


    //endregion


    //region Accessibility
    @Override
    public void onServiceConnected() {

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


    // TODO: refactor code by using functions and workers
    // TODO: make clear documentation & comments
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

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
    }
    //endregion



    // region Mayo

    private void run(String openedPackageName, long eventTime) throws JSONException {
        // variables
        long duration = 0;


        //checking if window is blocked by FOCUS session
        if (FunctionBase.JSONArrayOptElement(focus, openedPackageName) != null) {
            mayoClose(openedPackageName);
            return;
        }

        //checking if current window is a part of the same app as previous
        if (openedPackageName.equals(closedPackageName))
            return;

        if (closedIsInArryas) {
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
                updateGoals(closedPackageName, duration - pastMidnightTime);

                // update dayOfWeek
                dayOfWeek = getDayOfWeekStringShort();

                // save today goals
                allGoalsToFile();
                // update arrays
                loadTodayGoals();
                updatePackagesArrays();

                duration = pastMidnightTime;


            }
            updateGoals(closedPackageName, duration);

        }
        closedChangeTime = eventTime;
        Log.i("MAYO", "duration: " + duration);
        Log.i("MAYO", "goals update: " + todayGoals.toString());
        Log.i("MAYO", "window: " + openedPackageName);

        //check if window is blocked by goals
        // TODO: make the timer that will close app when usage time meet the limit
        if (FunctionBase.JSONArrayOptElement(close, "packageName", openedPackageName) != null) {
            mayoClose(openedPackageName);
            closedIsInArryas = true;
        }

        //check if window is set to notify by goals
        // TODO: make the timer that will send notification when usage time meet the limit
        else if (FunctionBase.JSONArrayOptElement(notify, "packageName", openedPackageName) != null) {
            mayoClose(openedPackageName);
            closedIsInArryas = true;
        }
        else {
            closedIsInArryas = false;
        }

        closedPackageName = openedPackageName;
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
    public static JSONArray todayGoals = new JSONArray();

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

    public static void changeTodayGoals(JSONObject goal) throws  JSONException {
        Log.i("MAYO", "updateTodayGoals: updating...");
        // check if the goal with this id is in the array
        JSONObject previous = JSONArrayOptElement(todayGoals, Goals.ID, goal.getString("id"));
        if(previous != null) {
            // get index of previous goal in array
            int index = JSONArrayGetIndexOf(todayGoals, Goals.ID, previous.getString(Goals.ID));

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

    public static void deleteTodayGoal(JSONObject goal) throws JSONException {
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
        public static void updatePackagesArrays() throws JSONException {
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

            // adding packageNames to the notify array
            for (int i = 0; i < packageNames.length(); i++) {
                //checking if the packageName already exist in array
                JSONObject temp = JSONArrayOptElement(notify, "packageName" ,packageNames.getString(i));
                if (temp != null) {
                    //check if the goal is not already present
                    if (FunctionBase.JSONArrayOptElement(temp.getJSONArray("goals"), goal.getString(Goals.ID)) == null)
                        temp.getJSONArray("goals").put(goal.getString(Goals.ID));

                    continue;
                }

                // create object which will be stored
                JSONObject element = new JSONObject()
                                .put("packageName", packageNames.getString(i))
                                .put("goals", new JSONArray().put(goal.getString(Goals.ID)));

                // store object
                notify.put(element);

            }
        }

        static void updateCloseArray (JSONObject goal) throws JSONException {
            JSONArray packageNames;

            packageNames = new JSONArray( goal.getString("apps") );

            // adding packageNames to the notify array
            for (int i = 0; i < packageNames.length(); i++) {
                //checking if the packageName already exist in array
                JSONObject temp = JSONArrayOptElement(close, "packageName" ,packageNames.getString(i));
                if (temp != null) {
                    //check if the goal is not already present
                    if (FunctionBase.JSONArrayOptElement(temp.getJSONArray("goals"), goal.getString(Goals.ID)) == null)
                        temp.getJSONArray("goals").put(goal.getString(Goals.ID));
                    continue;
                }

                // create object which will be stored
                JSONObject element = new JSONObject()
                        .put("packageName", packageNames.getString(i))
                        .put("goals", new JSONArray().put(goal.getString(Goals.ID)));

                // store object
                close.put(element);

            }
        }



    // endregion


}

