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

//                    Iterator<String> iterator = goals.keys();
//                    while (iterator.hasNext()) {
//                        PropertiesManager manager = new PropertiesManager(
//                                getApplicationContext().getFilesDir() + "/goals",
//                                iterator.next()
//                        );
//                        String history = manager.Read("history");
//                        if (history.length() == 7) {
//                            history = history.substring(1,6);
//                        }
//                       manager.Write("history", history + "1");
//                    }
                }
            }
        };
        timer.schedule(timerTask, 1000 * 2, 1000 * 2);

        return Result.success();
    }


    private JSONArray getGoals(int DAY_OF_WEEK) {
        Goals manager = new Goals(getApplicationContext());
        JSONArray allGoals = manager.getAllGoals();
        JSONArray ret = new JSONArray();



        int dayIndex;
        switch (DAY_OF_WEEK) {
            case Calendar.MONDAY:
                dayIndex = 0;
                break;
            case Calendar.TUESDAY:
                dayIndex = 1;
                break;
            case Calendar.WEDNESDAY:
                dayIndex = 2;
                break;
            case Calendar.THURSDAY:
                dayIndex = 3;
                break;
            case Calendar.FRIDAY:
                dayIndex = 4;
                break;
            case Calendar.SATURDAY:
                dayIndex = 5;
                break;
            case Calendar.SUNDAY:
                dayIndex = 6;
                break;
            default:
                Log.e("MAYO", "getGoals: day don't exist");
                dayIndex = 10;
                break;
        }



        JSONObject current = new JSONObject();
        for (int i = 0; i < allGoals.length(); i++) {
            try {
                current = allGoals.getJSONObject(i);
                if (current.getString("weekDays").charAt(dayIndex) == '1') {
                    ret.put(current);
                }
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
                Calendar.getInstance().getTimeInMillis() - (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000 + (Calendar.getInstance().get(Calendar.MINUTE) - 1)* 60 * 1000),
                Calendar.getInstance().getTimeInMillis());


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

            if (_apps.length() < 1)
                continue;



            long limit = 0;
            try {
                limit = Long.getLong(_goal.getString("limit"));
            } catch (NullPointerException e) {
                e.printStackTrace();
                continue;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            long tTime = 0;

//            for (UsageStats stats: usageStats) {
//                for (int j=0; j< apps.length(); j++) {
//                    if ( == stats.getPackageName())
//                        tTime += stats.getTotalTimeInForeground();
//                }
//            }
//            if (tTime >= limit * 60 * 1000) {
//                NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(getApplicationContext(), "ocean");
//                nBuilder.setContentTitle("Goal met")
//                        .setContentText(_goal.getString("name") + "limit was reached")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .setBigContentTitle(_goal.getString("name"))
//                                .bigText("Limit of " +
//                                        ((int)(limit/1000/60/60) < 1 ? "" : (int)(limit/1000/60/60) + "h ") +
//                                        ((int)(limit/1000/60) < 1 ? "" : (int)(limit/1000/60) + "min ") +
//                                        "was reached")
//                        )
//                        .setPriority(NotificationCompat.PRIORITY_MAX)
//                        .setAutoCancel(true);
//
//                Log.i("Err", "goalMayo: elo koniec mordo!");
//
//                PropertiesManager pm = new PropertiesManager(getApplicationContext().getFilesDir() + "/goals", "0.properties");
//                String history = pm.Read("history");
//                if (history.length() == 7) {
//                    history = history.substring(1,6);
//                }
//                    pm.Write("history", history + "0");
//
//
//                goals.remove(_iterator);
//            }
//            Log.i("MAYO", "goalMayo: " + _goal.getString("name") + ": " + tTime);
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

        return new ForegroundInfo(6001 ,notification);
    }

}