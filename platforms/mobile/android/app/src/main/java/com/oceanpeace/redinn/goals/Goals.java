package com.oceanpeace.redinn.goals;

import android.content.Context;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.getcapacitor.JSObject;
import com.oceanpeace.redinn.PropertiesManager;
import com.oceanpeace.redinn.mayo.GoalMayo;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Goals {
    Context context;
    public Goals(Context context) {
        this.context = context;
    }

    /**
     * Creates goal file and saves all passed data
     *
     * @param name              name of the goal
     * @param appsAsJSON        {"0": "com.example.one", "1", "com.example.two", ...}
     * @param weekDaysAsString  "0100101"
     * @param limitInMin        time limit in minutes
     * @return                  true if creating file was successful
     *                          false if limit of goals was reached
     * @throws IOException      throws expectation if IO fails
     *
     */
    public boolean createGoal(String name, JSObject appsAsJSON, String weekDaysAsString, long limitInMin) throws IOException {
        PropertiesManager goalsProperties = new PropertiesManager("goals.properties", context);
        String tempIO = goalsProperties.Read("int");
        int _int = Integer.parseInt(tempIO == null ? "0" : tempIO);
        tempIO = goalsProperties.Read("limit");
        int limit = Integer.parseInt(tempIO == null ? "0" : tempIO);
        tempIO = goalsProperties.Read("used");
        int used = Integer.parseInt(tempIO == null ? "0" : tempIO);

        if (used >= limit)
            return false;


        PropertiesManager propertiesManager = new PropertiesManager(
                context.getFilesDir() + "/goals",
                _int + ".properties"
        );

        propertiesManager.Create();
        propertiesManager.Write(
                new String[] {"name", "apps", "weekDays", "limit", "history"},
                new String[] {name, appsAsJSON.toString(), weekDaysAsString, String.valueOf(limitInMin), ""}
                );
        goalsProperties.Write("int", (_int + 1) + "");
        goalsProperties.Write("used", (used + 1) + "");

        WorkManager.getInstance(context.getApplicationContext()).enqueueUniqueWork(6002+"", ExistingWorkPolicy.REPLACE, new OneTimeWorkRequest.Builder(GoalMayo.class).build());

        return true;
    }

    public void edit(String fileName, String name, JSObject appsAsJSON, String weekDaysAsString, long limitInMin) {
        PropertiesManager propertiesManager = new PropertiesManager(
                context.getFilesDir() + "/goals",
                fileName
        );
        propertiesManager.Write(
                new String[] {"name", "apps", "weekDays", "limit"},
                new String[] {name, appsAsJSON.toString(), weekDaysAsString.toString(), String.valueOf(limitInMin)}
        );

        WorkManager.getInstance(context.getApplicationContext()).enqueueUniqueWork(6002+"", ExistingWorkPolicy.REPLACE, new OneTimeWorkRequest.Builder(GoalMayo.class).build());

    }

    public JSObject getAllGoals(){
        File[] files = new File(context.getFilesDir() + "/goals").listFiles();

        if (files == null)
            return null;

        JSObject ret = new JSObject();


        for (File file: files) {
            ret.put(file.getName(), getGoal(file.getName()));
        }

        return ret;
    }


    public JSObject getGoal(String fileName) {
        PropertiesManager propertiesManager = new PropertiesManager(
                context.getFilesDir() + "/goals",
                fileName
        );
        JSObject ret = new JSObject();

        ret.put("name", propertiesManager.Read("name"));
        ret.put("apps", JSONObject.quote(propertiesManager.Read("apps")));
        ret.put("weekDays", propertiesManager.Read("weekDays"));
        ret.put("limit", Long.valueOf(propertiesManager.Read("limit")));
        ret.put("history",propertiesManager.Read("history"));

        return ret;
    }


    public void delete(String fileName) {
        File file = new File(context.getFilesDir() + "/goals", fileName);
        file.delete();

        WorkManager.getInstance(context.getApplicationContext()).enqueueUniqueWork(6002+"", ExistingWorkPolicy.REPLACE, new OneTimeWorkRequest.Builder(GoalMayo.class).build());

    }
}
