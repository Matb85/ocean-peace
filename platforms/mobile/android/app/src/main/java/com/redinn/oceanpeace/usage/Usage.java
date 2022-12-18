package com.redinn.oceanpeace.usage;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.icu.util.Calendar;
import android.util.Log;

import com.getcapacitor.JSObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Usage {
    Context context;
    public Usage(Context context) {
        this.context = context;
    }

    long total = 0;

    public JSObject GetUsageData() {

        long time = System.currentTimeMillis();
        UsageStatsManager manager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                                                time - 1000 * 100, time);



        JSObject appsUsage = new JSObject();

        int it = 0;
        for (UsageStats stat: stats) {
            String packageName = stat.getPackageName();
            // Blocks OceanPeace from appearing in stats

            int appTime = (int)(stat.getTotalTimeInForeground()/1000 /60);

            if (manager.isAppInactive(packageName) || appTime<1)
                continue;

            total += appTime;

            JSObject app = new JSObject();
            app.put("timeSpent", appTime);
            app.put("packageName", packageName);
            appsUsage.put("" + it, app);
            it++;
        }

        Log.d("Mayo", "GetUsageData: " + appsUsage.toString() + "}," + total);


        return appsUsage;
    }

    public long getTotalTime() {
        return total;
    }

    
    public JSONArray getUnlockStats() {

        long startingTime = System.currentTimeMillis()
                - Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60L * 60L * 1000L
                - Calendar.getInstance().get(Calendar.MINUTE) * 60L * 1000L
                - Calendar.getInstance().get(Calendar.SECOND) * 1000L
                - Calendar.getInstance().get(Calendar.MILLISECOND);

        UsageStatsManager manager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);


        JSONArray Stats = new JSONArray();

        long iteration_time = startingTime;
        byte hour=0;
        while( iteration_time + 60L * 60L * 1000L < System.currentTimeMillis() ) {

            int count = countUnlocks(
                    iteration_time,
                    iteration_time + 60L * 60L * 1000L,
                    context);

            JSONObject record = new JSONObject();

            try {
                record.put("hour", hour);
                record.put("value", count);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Stats.put(record);
            hour++;
        }

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