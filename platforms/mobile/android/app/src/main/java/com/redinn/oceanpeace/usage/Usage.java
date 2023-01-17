package com.redinn.oceanpeace.usage;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.icu.util.Calendar;
import android.util.Log;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class Usage {

    private UsageStatsManager getManager(Context context) {
        return (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }
    private List<UsageStats> getStats(UsageStatsManager manager) {
        return manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                System.currentTimeMillis()
                        - Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60L * 60L * 1000L
                        - Calendar.getInstance().get(Calendar.MINUTE) * 60L * 1000L
                        - Calendar.getInstance().get(Calendar.SECOND) * 1000L,
                System.currentTimeMillis());
    }


    long totalTime = -1;


    public JSArray GetUsageData(Context context) {

        long time = System.currentTimeMillis();
        UsageStatsManager manager = getManager(context);
        List<UsageStats> stats = getStats(manager);

        totalTime = 0;

        JSArray appsUsage = new JSArray();

        int it = 0;
        for (UsageStats stat: stats) {
            String packageName = stat.getPackageName();

            int appTime = (int)(stat.getTotalTimeInForeground()/1000 /60);

            totalTime += appTime;

            JSObject app = new JSObject();
            app.put("timeSpent", appTime);
            app.put("packageName", packageName);
            appsUsage.put(app);
            it++;
        }

        Log.d("Mayo", "GetUsageData: " + appsUsage.toString());


        return appsUsage;
    }

    public long getTotalTime() {
        return totalTime;
    }
    
    public JSONArray getUnlockStats(Context context) {

        long startingTime = System.currentTimeMillis()
                - 10 * 60L * 60L * 1000L
                - Calendar.getInstance().get(Calendar.MINUTE) * 60L * 1000L
                - Calendar.getInstance().get(Calendar.SECOND) * 1000L
                - Calendar.getInstance().get(Calendar.MILLISECOND);



        JSONArray Stats = new JSONArray();

        long iteration_time = startingTime;
        byte hour= (byte) ( Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY) - 10 );
        short key = 0;
        while( iteration_time < Calendar.getInstance().getTimeInMillis() ) {

            int count = countUnlocks(
                    iteration_time,
                    iteration_time + 60L * 60 * 1000,
                    context);

            JSONObject record = new JSONObject();

            try {
                record.put("hour", hour > 12 ? hour-12 + "pm" : hour + "am");
                record.put("key", key);
                record.put("value", count);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Stats.put(record);
            hour++;
            key += 10;
            iteration_time += 60L * 60L * 1000L;
        }

        Log.i("TEST", "getUnlockStats: " + Stats.toString());
        return Stats;
    }

     int countUnlocks(long start_time, long end_time, Context context) {
        UsageStatsManager manager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        int count = 0;

        UsageEvents events = manager.queryEvents(
                // begin time
                start_time,
                // end time
                end_time
        );

        while (events.hasNextEvent()) {
            UsageEvents.Event _event = new  UsageEvents.Event();
            events.getNextEvent(_event);

            if (_event.getEventType() == UsageEvents.Event.KEYGUARD_HIDDEN) {
                count++;
            }
        }

        return count;
    }

}