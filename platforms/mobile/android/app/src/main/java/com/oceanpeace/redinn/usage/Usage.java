package com.redinn.oceanpeace.usage;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import com.getcapacitor.JSObject;

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
}