package com.oceanpeace.redinn;

import static com.oceanpeace.redinn.FunctionBase.JSONArrayHasElement;
import static com.oceanpeace.redinn.FunctionBase.getDayOfWeekStringShort;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.oceanpeace.redinn.goals.Goals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
// TODO: rename this class

public class TestAccessibilityService extends AccessibilityService {

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

        public static JSONArray todayGoals = new JSONArray();
    //endregion


    @Override
    public void onServiceConnected() {

        dayOfWeek = getDayOfWeekStringShort();

        // region creating lists of packageNames

            // creating arrays using worker with back off delay if the work fail
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
        Log.i("W", "" + SystemClock.elapsedRealtime());
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo == null)
            return;

        //blockade if a new window is still a part of running application
        if (event.getPackageName() == previousPackageName)
            return;


        long thisChangeTime = 0;
        long displayTime = 0;
        //
        // Calculating the display time of the previous window
        //
        thisChangeTime = event.getEventTime();
        displayTime = thisChangeTime - previousChangeTime;
        previousChangeTime = thisChangeTime;




        Toast.makeText(getApplicationContext(), "" + event.getPackageName(), Toast.LENGTH_SHORT).show();
//        if (previousPackageName != null) {
//
//            WorkManager.getInstance(getApplicationContext())
//                    .enqueueUniqueWork(String.valueOf(6001),
//                            ExistingWorkPolicy.APPEND_OR_REPLACE,
//                            new OneTimeWorkRequest.Builder(MayoWorker.class).build());
//
//        }
        
        Log.i("WINDOW",
                "\nstartTime: " + previousChangeTime +
                        "\ncurrentTime: " + thisChangeTime +
                        "\nduration: " + displayTime
        );

        Log.i("W", "" + SystemClock.elapsedRealtime());
        
    }

    @Override
    public void onInterrupt() {
    }

    // region Mayo

        private void mayo(String packageName, long eventTime) throws JSONException {
            // variables
            long duration;


            //checking if window is blocked by FOCUS session
            if (JSONArrayHasElement(focus, packageName)) {
                mayoClose();
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


            //check if window is blocked by goals
            // TODO: make the timer that will close app when usage time meet the limit
            if (JSONArrayHasElement(close, packageName)) {
                return;
            }

            //check if window is set to notify by goals
            // TODO: make the timer that will send notification when usage time meet the limit
            if (JSONArrayHasElement(notify, packageName)) {
                return;
            }
        }



        // TODO: make closing app function
        private void mayoClose() {

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
            if (!JSONArrayHasElement(
                    new JSONArray(goal.getString("activeDays")),
                    dayOfWeek))
                return;

            JSONArray packageNames;

            packageNames = new JSONArray( goal.getString("apps") );

            // adding packageNames to the notify array
            for (int i = 0; i < packageNames.length(); i++) {
                //checking if the packageName already exist in array
                if (JSONArrayHasElement(notify, "packageName" ,packageNames.getString(i))) {
                    // search for this packageName
                    for (int j = 0; j < notify.length(); j++) {
                        if (notify.getJSONObject(j).getString("packageName").equals(packageNames.getString(i))) {
                            //check if the goal is not already present
                            if (!JSONArrayHasElement(notify.getJSONObject(j).getJSONArray("goals"), goal.getString("id")))
                                notify.getJSONObject(j).getJSONArray("goals").put(goal.getString("id"));
                        }
                    }
                    continue;
                }


                JSONObject element = new JSONObject()
                                .put("packageName", packageNames.getString(i))
                                .put("goals", new JSONArray().put(goal.getString("id")));

                notify.put(element);

            }
            updateLoadedGoals(goal);
        }

        void updateCloseArray (JSONObject goal) throws JSONException {
            // skip function if goal is not set for today
            if (!JSONArrayHasElement(
                    new JSONArray(goal.getString("activeDays")),
                    dayOfWeek))
                return;

            JSONArray packageNames;

            packageNames = new JSONArray( goal.getString("apps") );

            // adding packageNames to the notify array
            for (int i = 0; i < packageNames.length(); i++) {
                //checking if the packageName already exist in array
                if (JSONArrayHasElement(close, "packageName" ,packageNames.getString(i))) {
                    // search for this packageName
                    for (int j = 0; j < close.length(); j++) {
                        if (close.getJSONObject(j).getString("packageName").equals(packageNames.getString(i))) {
                            //check if the goal is not already present
                            if (!JSONArrayHasElement(close.getJSONObject(j).getJSONArray("goals"), goal.getString("id")))
                                close.getJSONObject(j).getJSONArray("goals").put(goal.getString("id"));
                        }
                    }
                    continue;
                }


                JSONObject element = new JSONObject()
                        .put("packageName", packageNames.getString(i))
                        .put("goals", new JSONArray().put(goal.getString("id")));

                close.put(element);

            }
            updateLoadedGoals(goal);
        }

        private void updateLoadedGoals (JSONObject goal) throws  JSONException {
            if(JSONArrayHasElement(todayGoals, "id", goal.getString("id")))
                return;

            todayGoals.put(goal);
        }

    // endregion

    //region Workers

    public class MayoWorker extends Worker {
        public MayoWorker(Context context, WorkerParameters params) {
            super(context, params);
        }


        @NonNull
        @Override
        public Result doWork() {


            return null;
        }


    }

    public class CreateGoalsArraysWorker extends Worker {
        public CreateGoalsArraysWorker(Context context, WorkerParameters params) {
            super(context, params);
        }


        @NonNull
        @Override
        public Result doWork() {

            try {
                updateGoalsArray();
                return Result.success();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return Result.retry();
        }


    }

    //endregion


}

