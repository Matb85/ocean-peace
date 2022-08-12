package com.oceanpeace.redinn.goals;

import android.content.Context;
import android.util.Log;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.oceanpeace.redinn.managers.JSONManager;
import com.oceanpeace.redinn.mayo.GoalMayo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class Goals {
    Context context;

    public Goals(Context context) {
        this.context = context;
    }

    private String getDir(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    public void saveGoal(JSONObject goal) throws Exception {
        JSONManager.writeFile(goal, getDir(context.getApplicationContext()) + "/goals/" + goal.getString("id") + ".json");

        WorkManager.getInstance(context.getApplicationContext()).enqueueUniqueWork(6002+"", ExistingWorkPolicy.REPLACE, new OneTimeWorkRequest.Builder(GoalMayo.class).build());
    }

    public JSONArray getAllGoals() {
        File[] files = new File(getDir(context.getApplicationContext()) + "/goals").listFiles();

        if (files == null)
            return null;

        /* put data from each goal the the final array */
        JSONArray ret = new JSONArray();

        Log.d("GoalsPlugin","iterating through all files...");

        for (File file : files) {
            Log.d("GoalsPlugin","reading file "+ file.getName());

            try {
                ret.put(JSONManager.readFile(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    public JSONObject getGoal(String id) {
        JSONObject res = new JSONObject();

        File file = new File(getDir(context.getApplicationContext()) + "/goals/" + id + ".json");

        try {
            res = JSONManager.readFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public void deleteGoal(String id) {
        /* delete the id from the database */
        File file = new File(getDir(context.getApplicationContext()) + "/goals/" + id + ".json");
        file.delete();

        WorkManager.getInstance(context.getApplicationContext()).enqueueUniqueWork(6002+"", ExistingWorkPolicy.REPLACE, new OneTimeWorkRequest.Builder(GoalMayo.class).build());
    }
}
