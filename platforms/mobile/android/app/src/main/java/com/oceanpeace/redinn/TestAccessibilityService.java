package com.oceanpeace.redinn;

import static com.oceanpeace.redinn.FunctionBase.JSONArrayHasElement;
import static com.oceanpeace.redinn.FunctionBase.getDayOfWeekStringShort;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.NonNull;
import androidx.work.BackoffPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.oceanpeace.redinn.goals.Goals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;
// TODO: rename this class

public class TestAccessibilityService extends AccessibilityService {

    // region variables
        private String previousPackageName = null;
        private long previousChangeTime = SystemClock.uptimeMillis();
        private String dayOfWeek = null;

        private JSONArray notify = new JSONArray();
        private JSONArray close = new JSONArray();
        private static JSONArray focus = new JSONArray();
    //endregion


    @Override
    public void onServiceConnected() {

        dayOfWeek = getDayOfWeekStringShort();

        // region creating lists of packageNames

            // creating arrays using worker with back off delay if the work fail
            WorkManager.getInstance(getApplicationContext())
                    .enqueueUniqueWork(
                            "6000",
                            ExistingWorkPolicy.APPEND_OR_REPLACE,
                            new OneTimeWorkRequest.Builder(
                                CreateGoalsArraysWorker.class
                            )
                                    .setBackoffCriteria(
                                            BackoffPolicy.LINEAR,
                                            1000,
                                            TimeUnit.MILLISECONDS
                                    )
                                    .build()
                    );

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
            //checking if window is blocked by FOCUS session
            if (JSONArrayHasElement(focus, packageName)) {
                mayoClose();
                return;
            }

            //checking if the date changed
            //if not then update arrays
            if (!dayOfWeek.equals(getDayOfWeekStringShort())) {
                dayOfWeek = getDayOfWeekStringShort();
                updateGoalsArray();
            }

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
                    "\n\tclose: " + close.toString());
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
                if (JSONArrayHasElement(notify, packageNames.getString(i)))
                    continue;

                notify.put(packageNames.getString(i));
            }
        }

        void updateCloseArray (JSONObject goal) throws JSONException {
            // skip function if goal is not set for today
            if (!JSONArrayHasElement(
                    new JSONArray(goal.getString("activeDays")),
                    dayOfWeek))
                return;

            JSONArray packageNames;

            packageNames = new JSONArray( goal.getString("apps") );

            // adding packageNames to the close array
            for (int i = 0; i < packageNames.length(); i++) {
                //checking if the packageName already exist in array
                if (JSONArrayHasElement(close, packageNames.getString(i)))
                    continue;

                close.put(packageNames.getString(i));
            }
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

