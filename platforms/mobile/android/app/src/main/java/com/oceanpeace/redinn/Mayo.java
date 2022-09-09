package com.oceanpeace.redinn;

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

import com.oceanpeace.redinn.goals.Goals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.Calendar;
import java.util.Objects;
// TODO: rename this class

public class Mayo extends AccessibilityService {

    // region variables
        private String previousPackageName = null;
        private long previousChangeTime = SystemClock.uptimeMillis();
        private String dayOfWeek = null;

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
        private JSONArray notify = new JSONArray();

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
        private JSONArray close = new JSONArray();

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
            updateGoalsArray();
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
            mayo(event.getPackageName().toString(), event.getEventTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onInterrupt() {
    }
    //endregion



    // region Mayo

    private void mayo(String packageName, long eventTime) throws JSONException {
        // variables
        long duration;


        //checking if window is blocked by FOCUS session
        if (FunctionBase.JSONArrayOptElement(focus, packageName) != null) {
            mayoClose(packageName);
            return;
        }

        //checking if current window is a part of the same app as previous
        if (Objects.equals(packageName, previousPackageName))
            return;

        //checking if the date changed
        //if not then update arrays
        if (!dayOfWeek.equals(getDayOfWeekStringShort())) {
            dayOfWeek = getDayOfWeekStringShort();
            updateGoalsArray();

            previousChangeTime = SystemClock.uptimeMillis();
            previousPackageName = null;
        }


        duration = eventTime - previousChangeTime;
        previousChangeTime = eventTime;



        Log.i("MAYO", "window: " + packageName);

        //check if window is blocked by goals
        // TODO: make the timer that will close app when usage time meet the limit
        if (FunctionBase.JSONArrayOptElement(close, "packageName", packageName) != null) {
            mayoClose(packageName);
            return;
        }

        //check if window is set to notify by goals
        // TODO: make the timer that will send notification when usage time meet the limit
        if (FunctionBase.JSONArrayOptElement(notify, "packageName", packageName) != null) {
            mayoClose(packageName);
            return;
        }
        previousPackageName = packageName;
    }



    private void updateGoal(String packageName, long eventTime) {

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




    //region Focus

    // TODO: rewrite Focus, to make it works with MAYO2.0

    public static void startFocus(JSONArray packages) {
        focus = packages;
    }
    public static void stopFocus() {
        focus = new JSONArray();
    }

    //endregion
    // endregion



    // region Updating Goals' arrays functions

        /**
         *
         * Updates <i>notify</i> and <i>close</i> arrays
         *
         * @throws JSONException
         */
        public void updateGoalsArray () throws JSONException {
            //clear arrays
            Log.i("MAYO", "updateGoalsArray: clearing arrays!");
            notify = new JSONArray();
            close = new JSONArray();
            todayGoals = new JSONArray();

            Log.i("MAYO", "updateGoalsArray: updating for " + dayOfWeek);

            JSONArray goals;
            JSONObject goal;

            Goals goalsClass = new Goals(getApplicationContext());
            goals = goalsClass.getAllGoals();

            // iterating through the all goals
            for (int i = 0; i < goals.length(); i++) {
                goal = goals.getJSONObject(i);

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

        void updateNotifyArray (JSONObject goal) throws JSONException {
            // skip function if goal is not set for today
            if (FunctionBase.JSONArrayOptElement(
                    new JSONArray(goal.getString(Goals.ACTIVEDAYS)),
                    dayOfWeek) == null)
                return;

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


                JSONObject element = new JSONObject()
                                .put("packageName", packageNames.getString(i))
                                .put("goals", new JSONArray().put(goal.getString(Goals.ID)));

                notify.put(element);

            }
            addTodayGoals(goal);
        }

        void updateCloseArray (JSONObject goal) throws JSONException {
            // skip function if goal is not set for today
            if (FunctionBase.JSONArrayOptElement(
                    new JSONArray(goal.getString(Goals.ACTIVEDAYS)),
                    dayOfWeek) == null)
                return;

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


                JSONObject element = new JSONObject()
                        .put("packageName", packageNames.getString(i))
                        .put("goals", new JSONArray().put(goal.getString(Goals.ID)));

                close.put(element);

            }
            addTodayGoals(goal);
        }



    // endregion




    // region today's goals
    /**
     * Array containing goals which are scheduled for today
     */
    public static JSONArray todayGoals = new JSONArray();

    private static void addTodayGoals(JSONObject goal) throws JSONException {
        //if the goal exist already then return
        if(JSONArrayOptElement(todayGoals, Goals.ID, goal.getString("id")) != null)
            return;

        //update array
        todayGoals.put(goal);
    }

    public static void updateTodayGoals(JSONObject goal) throws  JSONException {
        // check if the goal with this id is in the array
        JSONObject previous = JSONArrayOptElement(todayGoals, Goals.ID, goal.getString("id"));
        if(previous != null) {
            // get index of previous goal in array
            int index = JSONArrayGetIndexOf(todayGoals, Goals.ID, previous.getString(Goals.ID));
            // parse Date from both goals
            Date prevUpdate = Date.valueOf(previous.getString(Goals.SESSIONUPDATE));
            Date curUpdate = Date.valueOf(goal.getString(Goals.SESSIONUPDATE));
            // check if previous has more up-to-date data
            if(prevUpdate.after(curUpdate)) {
                //update data
                goal.put(Goals.SESSIONUPDATE, Calendar.getInstance().getTime().toString());
                goal.put(Goals.SESSIONSHISTORY, previous.getString(Goals.SESSIONSHISTORY));
                goal.put(Goals.SESSIONTIME, previous.getString(Goals.SESSIONTIME));
            }
            // replace goal in array
            todayGoals.put(index, goal);
        }
        else {
            // if goal doesn't exist add it
            todayGoals.put(goal);
        }
    }
    //endregion
}

