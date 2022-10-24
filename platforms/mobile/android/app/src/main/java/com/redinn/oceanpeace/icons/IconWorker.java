package com.redinn.oceanpeace.icons;

import android.content.Context;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class IconWorker extends  Worker{
    public IconWorker(
            Context context,
            WorkerParameters params
    ){
        super(context, params);
    }

    public static WorkRequest regenerateIcons =
            new OneTimeWorkRequest.Builder(IconWorker.class).build();

    @Override
    public Result doWork() {

        IconManager.regenerateIcons(getApplicationContext());

        return Result.success();
    }
}
