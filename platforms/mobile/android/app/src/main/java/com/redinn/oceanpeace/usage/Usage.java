package com.redinn.oceanpeace.usage;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.icu.util.Calendar;
import android.util.Log;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.redinn.oceanpeace.FunctionBase;
import com.redinn.oceanpeace.icons.IconManager;

import org.json.JSONArray;
import org.json.JSONException;
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


    public JSArray GetUsageData(Context context) {

        long time = System.currentTimeMillis();
        UsageStatsManager manager = getManager(context);
        List<UsageStats> stats = getStats(manager);

        JSArray appsUsage = new JSArray();

        check_stats:
        for (UsageStats stat: stats) {

            String packageName = stat.getPackageName();

            int appTime = (int)(stat.getTotalTimeInForeground()/1000 /60);

            if (appTime < 1)
                continue check_stats;

            for (int i=0; i<appsUsage.length(); i++) {
                try {
                    if (appsUsage.getJSONObject(i).getJSONObject("icon").getString("packageName").equals(packageName)) {
                        int minutes = appsUsage.getJSONObject(i).getInt("minutes");
                        appsUsage.put(i, appsUsage.getJSONObject(i).put("minutes", minutes + appTime));
                        continue check_stats;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSObject icon = new JSObject();
            try {
                icon = JSObject.fromJSONObject(
                        IconManager.getIcon(
                                packageName,
                                context.getApplicationContext()).toJSON()
                );

            } catch (Exception e) {
                e.printStackTrace();
                icon.put("packageName", "");
                icon.put("label", "unknown");
                icon.put("path", "");
                icon.put("version", "");
            }

            JSObject app = new JSObject();
            app.put("minutes", appTime);
            app.put("icon", icon);
            appsUsage.put(app);
        }

        Log.d("Mayo", "GetUsageData: " + appsUsage.toString());


        return appsUsage;
    }

    public long getTotalTime(Context context) {
        long time=0;

        List<UsageStats> statsList = getStats(getManager(context));

        for (UsageStats stats : statsList) {
            time += stats.getTotalTimeInForeground()/1000/60;
        }

        return time;
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