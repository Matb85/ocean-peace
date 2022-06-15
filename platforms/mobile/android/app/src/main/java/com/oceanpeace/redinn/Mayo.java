package com.oceanpeace.redinn;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import androidx.activity.result.ActivityResultLauncher;
import android.app.Fragment;
import android.util.Log;

import com.getcapacitor.JSObject;

import org.json.JSONArray;

import java.util.List;

public class Mayo {
    public Mayo() {
    }


    public String GetUsageData() {


        long time = System.currentTimeMillis();
        UsageStatsManager manager = (UsageStatsManager)MainActivity.getAppContext().getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                                                time - 1000 * 100, time);
        JSONArray appsUsage = new JSONArray();

        long total=0;

        for (UsageStats stat: stats) {
            String packageName = stat.getPackageName();
            // Blocks OceanPeace from appearing in stats
            if (manager.isAppInactive(packageName) || stat.getTotalTimeInForeground()<1)
                continue;

            long appTime = stat.getTotalTimeInForeground();
            total += appTime;

            JSObject app = new JSObject();
            app.put("timeSpent", appTime);
            app.put("appName", packageName);
            appsUsage.put(app);
        }

        Log.d("Mayo", "GetUsageData: " + appsUsage.toString() + "}," + total);
        return appsUsage.toString();
    }
}