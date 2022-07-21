package com.oceanpeace.redinn.mayo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ExistingWorkPolicy;
import androidx.work.ForegroundInfo;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.getcapacitor.JSArray;
import com.oceanpeace.redinn.PropertiesManager;
import com.oceanpeace.redinn.R;
import com.oceanpeace.redinn.goals.Goals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GoalMayo extends Worker {
    public GoalMayo(Context context, WorkerParameters params) {
        super(context, params);
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    JSONArray goals = new JSArray();

    private NotificationManager notificationManager;


    @Override
    public Result doWork() {

        if (new File(getApplicationContext().getFilesDir() + "/goals").listFiles() == null) {
            Log.i("MAYO", "doWork: no goals!");
            return Result.success();
        }

        int DAY_OF_WEEK = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        goals = getGoals(DAY_OF_WEEK);
        Log.i("DEBUG", "doWork: " + goals.toString());

        setForegroundAsync(createForegroundInfo("Mayo running"));

        Timer timer = new Timer(false);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (goals != null)
                    goalMayo(goals);
                Log.i("MAYO", "run: " + 1);
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != DAY_OF_WEEK) {
                    WorkManager
                            .getInstance(getApplicationContext())
                            .enqueueUniqueWork(6002+"",
                                    ExistingWorkPolicy.REPLACE,
                                    new OneTimeWorkRequest
                                            .Builder(GoalMayo.class)
                                            .build()
                            );


                    for (int i=0; i < goals.length(); i++) {
                        PropertiesManager manager = null;
                        try {
                            manager = new PropertiesManager(
                                    getApplicationContext().getFilesDir() + "/goals",
                                    goals.getJSONObject(i).getString("id")
                                    );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String history = manager.Read("history");
                        if (history.length() == 7) {
                            history = history.substring(1,6);
                        }
                       manager.Write("history", history + "1");
                    }
                }
            }
        };
        timer.schedule(timerTask, 0, 1000 * 60 * 5);

        return Result.success();
    }


    private JSONArray getGoals(int DAY_OF_WEEK) {
        Goals manager = new Goals(getApplicationContext());
        JSONArray allGoals = manager.getAllGoals();
        JSONArray ret = new JSONArray();



        String dayIndex;
        switch (DAY_OF_WEEK) {
            case Calendar.MONDAY:
                dayIndex = "Mon";
                break;
            case Calendar.TUESDAY:
                dayIndex = "Tue";
                break;
            case Calendar.WEDNESDAY:
                dayIndex = "Wed";
                break;
            case Calendar.THURSDAY:
                dayIndex = "Thu";
                break;
            case Calendar.FRIDAY:
                dayIndex = "Fri";
                break;
            case Calendar.SATURDAY:
                dayIndex = "Sat";
                break;
            case Calendar.SUNDAY:
                dayIndex = "Sun";
                break;
            default:
                Log.e("MAYO", "getGoals: day don't exist");
                dayIndex = "Mon";
                break;
        }




        Log.i("DEBUG", "getGoals: " + allGoals.toString());
        for (int i = 0; i < allGoals.length(); i++) {
            try {
                String days = allGoals.getJSONObject(i).getString("activeDays");
                Log.i("DEBUG", "getGoals: " + days);

                if (days.contains(dayIndex))
                    ret.put(allGoals.getJSONObject(i));
            } catch (JSONException e) {
                Log.e("MAYO", "getting goal JSON failed");
            }
        }

        return ret;
    }



    public void goalMayo(JSONArray goals) {

        UsageStatsManager manager = (UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStats = manager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                System.currentTimeMillis() -  60 * 60 * 1000,
                System.currentTimeMillis());


        for (int i=0; i<goals.length(); i++) {

            JSONObject _goal = null;
            String _apps = "";
            try {
                _goal = goals.getJSONObject(i);
                _apps = _goal.getString("apps");
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            Log.i("DEBUG", "goalMayo: " + _goal.toString());

            if (_apps.length() < 1)
                continue;

            JSONArray apps = new JSONArray();
            try {
                apps = new JSONArray(_apps);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("DEBUG", "goalMayo: " + apps.toString());

            long limit = 0;
            try {
                limit = Long.parseLong(_goal.getString("limit"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            long tTime = 0;

            for (UsageStats stats: usageStats) {
                Log.i("DEBUG", "goalMayo: " + stats.getPackageName());
                for (int j=0; j < apps.length(); j++) {
                    try {
                        Log.i("DEBUG", "goalMayo: " + j);
                        if ( apps.getString(j).equalsIgnoreCase(stats.getPackageName()))
                            tTime += stats.getTotalTimeInForeground();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (tTime >= (limit * 60 * 1000)) {
                NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(getApplicationContext(), "ocean");
                try {
                    nBuilder.setContentTitle("Goal met")
                            .setContentText(_goal.getString("name") + "limit was reached")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .setBigContentTitle(_goal.getString("name"))
                                    .bigText("Limit of " +
                                            ((int)(limit/1000/60/60) < 1 ? "" : (int)(limit/1000/60/60) + "h ") +
                                            ((int)(limit/1000/60) < 1 ? "" : (int)(limit/1000/60) + "min ") +
                                            "was reached")
                            )
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setAutoCancel(true);
                } catch (JSONException e) {
                    nBuilder.setContentTitle("Goal met")
                            .setContentText("Goal limit was reached")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .setBigContentTitle("Goal was met!")
                                    .bigText("Limit of " +
                                            ((int)(limit/1000/60/60) < 1 ? "" : (int)(limit/1000/60/60) + "h ") +
                                            ((int)(limit/1000/60) < 1 ? "" : (int)(limit/1000/60) + "min ") +
                                            "was reached")
                            )
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setAutoCancel(true);
                    e.printStackTrace();
                }

                Log.i("Err", "goalMayo: elo koniec mordo!");

                PropertiesManager pm = new PropertiesManager(getApplicationContext().getFilesDir() + "/goals", "0.properties");
                String history = pm.Read("history");
                if (history != null && history.length() == 7) {
                    history = history.substring(1,6);
                }
                    pm.Write("history", history + "0");


                goals.remove(i);
            }
            try {
                Log.i("MAYO", "goalMayo: " + _goal.getString("name") + ": " + tTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }



    private ForegroundInfo createForegroundInfo(@NonNull String progress) {
        // Build a notification using bytesRead and contentLength

        Context context = getApplicationContext();

        PendingIntent intent = WorkManager.getInstance(context)
                .createCancelPendingIntent(getId());

        NotificationChannel channel = new NotificationChannel("mayoID", "Mayo", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(context, "mayoID")
                .setContentTitle("Mayo is running!")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                // Add the cancel action to the notification which can
                // be used to cancel the worker
                .setAutoCancel(true)
                .build();

        notificationManager.notify(6002, notification);

        return new ForegroundInfo(6001 ,notification);
    }

}