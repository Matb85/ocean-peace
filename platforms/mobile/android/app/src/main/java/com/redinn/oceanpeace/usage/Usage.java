package com.redinn.oceanpeace.usage;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.icu.util.Calendar;
import android.util.Log;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.redinn.oceanpeace.database.OceanDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Usage {

    // region GET functions
    private UsageStatsManager getManager(Context context) {
        return (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }
    private List<UsageEvents.Event> getEvents(Context context) {
        List<UsageEvents.Event> list = new ArrayList<UsageEvents.Event>();

        UsageEvents events = getManager(context).queryEvents(
                System.currentTimeMillis()
                        - Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60L * 60L * 1000L
                        - Calendar.getInstance().get(Calendar.MINUTE) * 60L *  1000L
                        - Calendar.getInstance().get(Calendar.SECOND) * 1000L
                        - Calendar.getInstance().get(Calendar.MILLISECOND),
                System.currentTimeMillis()
        );

        while (events.hasNextEvent()) {
            UsageEvents.Event temp = new UsageEvents.Event();
            events.getNextEvent(temp);
            list.add(temp);
        }

        return list;
    }

    // endregion

    // region API

    // region USAGE DATA functions

    private class Stat {
        long totalTime = 0;
        long startTime;
        boolean resumed=false;
    }
    private boolean isApp(String packageName, Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    private HashMap<String, Stat> _applicationsUsageData(Context context) {

        List<UsageEvents.Event> events = getEvents(context);

        HashMap<String, Stat> activityTime = new HashMap<String, Stat>();
        long dayStart= (
                System.currentTimeMillis()
                - Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60L * 60L * 1000L
                - Calendar.getInstance().get(Calendar.MINUTE) * 60L *  1000L
                - Calendar.getInstance().get(Calendar.SECOND) * 1000L
                - Calendar.getInstance().get(Calendar.MILLISECOND)
        );


        // Activity started previous day
        for (UsageEvents.Event event : events) {
            if (event.getEventType() != UsageEvents.Event.ACTIVITY_PAUSED)
                continue;

            Stat s = new Stat();
            s.totalTime += (event.getTimeStamp() - dayStart);
            activityTime.put(event.getPackageName(), s);

            break;
        }

        // Applications usage through the day
        for(UsageEvents.Event event : events) {

            // RESUME event
            if (event.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
                Stat temp = activityTime.get(event.getPackageName());
                if (temp == null) {
                    temp = new Stat();
                }

                temp.startTime = event.getTimeStamp();
                temp.resumed = true;
            }
            // PAUSED event
            else if (event.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED) {
                Stat temp = activityTime.get(event.getPackageName());
                if (temp == null) {
                    temp = new Stat();
                }

                // If app was opened sum it's working time
                if (temp.resumed) {
                    temp.totalTime += (event.getTimeStamp() - temp.startTime);
                    temp.resumed = false;
                }

                activityTime.put(event.getPackageName(), temp);
            }
            // CLOSE ENOUGH DAMN
        }

        // Activity is currently running in foreground [edge case]
        for (String name : activityTime.keySet()) {
            Stat stat = activityTime.get(name);
            if (!stat.resumed)
                continue;

            stat.totalTime += (System.currentTimeMillis() - stat.startTime);
            activityTime.put(name, stat);
        }

        return activityTime;
    }

    // endregion

    public JSArray getUsageData(Context context) {
        JSArray ret = new JSArray();


        HashMap<String, Stat> dataSet = _applicationsUsageData(context);

        for (String packageName : dataSet.keySet()) {

            // receive ICON data
            JSObject icon = OceanDatabase.getInstance(context).iconDAO().getIcon(packageName).toJSON();

            if (Objects.equals(icon.getString("label"), ""))
                icon.put("label", "unknown");


            JSObject app = new JSObject();
            app.put("minutes", dataSet.get(packageName).totalTime /60000);
            app.put("icon", icon);
            ret.put(app);
        }

        return ret;
    }

    public long getTotalTime(Context context) {
        long time=0;

        HashMap<String, Stat> data = _applicationsUsageData(context);

        for (Stat s : data.values()) {
            time += s.totalTime/1000/60;
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

    // region UNLOCKS functions

    int countUnlocks(long start_time, long end_time, Context context) {
        UsageStatsManager manager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        int count = 0;

        UsageEvents events = manager.queryEvents(
                start_time,
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
    // endregion

    // endregion
}