package com.oceanpeace.redinn.mayo;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.IBinder;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;

public class GoalMayo extends Service {
    public GoalMayo() {

    }

    @Override
    public int onStartCommand( Intent intent , int flags , int startId ) {

        UsageStatsManager manager = (UsageStatsManager) getApplicationContext().getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> usageStats = manager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                Calendar.getInstance().getTimeInMillis() - (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000 + (Calendar.getInstance().get(Calendar.MINUTE) - 1)* 60 * 1000),
                Calendar.getInstance().getTimeInMillis());

        int goalsI = intent.getIntExtra("goals", 0);
        long[] limits = intent.getLongArrayExtra("limits");

        for (int i=0; i<goalsI; i++) {
            String[] apps = intent.getStringArrayExtra(i+"");
            if (apps == null)
                continue;


            long limit = limits[i];
            long tTime = 0;

            for (UsageStats stats: usageStats) {
                Predicate<String> predicate = x -> x == stats.getPackageName();
                if (Arrays.stream(apps).anyMatch(predicate))
                    tTime += stats.getTotalTimeInForeground();
            }
            if (tTime >= limit) {

            }
        }


        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}