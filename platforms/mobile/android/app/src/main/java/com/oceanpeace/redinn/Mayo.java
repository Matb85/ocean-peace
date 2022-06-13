package com.oceanpeace.redinn;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONArray;

import java.util.List;

public class Mayo extends Service {
    public Mayo() {
    }

    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String GetUsageData() {

        long time = System.currentTimeMillis();
        UsageStatsManager manager = (UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                                                time - 1000 * 100, time);
        JSONArray appsUsage = new JSONArray();



        for (UsageStats stat: stats) {
            String packageName = stat.getPackageName();
            // Blocks OceanPeace from appearing in stats
            if (manager.isAppInactive(packageName))
                continue;

            long appTime = stat.getTotalTimeInForeground();

            JSObject app = new JSObject();
            app.put("timeSpent", appTime);
            app.put("appName", packageName);
            appsUsage.put(app);
        }

        return appsUsage.toString();
    }
}