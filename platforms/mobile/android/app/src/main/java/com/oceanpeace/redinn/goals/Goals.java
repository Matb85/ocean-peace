package com.oceanpeace.redinn.goals;

import android.content.Context;

import com.getcapacitor.JSObject;
import com.oceanpeace.redinn.PropertiesManager;

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
     * @param weekDaysASJSON    {"MON": "false", "TUE", "true", ...}
     * @param limitInMin        time limit in minutes
     * @return                  true if creating file was successful
     *                          false if limit of goals was reached
     * @throws IOException      throws expectation if IO fails
     *
     */
    public boolean createGoal(String name, JSObject appsAsJSON, JSObject weekDaysASJSON, long limitInMin) throws IOException {
        PropertiesManager goalsProperties = new PropertiesManager("goals.properties", context);
        Integer _int = Integer.getInteger(goalsProperties.Read("int"), null);
        Integer limit = Integer.getInteger(goalsProperties.Read("limit"), null);
        Integer used = Integer.getInteger(goalsProperties.Read("used"), null);

        if ((used == null ? 0 : used) >= (limit == null ? 3 : limit))
            return false;


        PropertiesManager propertiesManager = new PropertiesManager(
                context.getFilesDir() + "/goals",
                (_int == null ? "0" : _int) + ".properties"
        );

        propertiesManager.Create();
        propertiesManager.Write(
                new String[] {"name", "apps", "weekDays", "limit"},
                new String[] {name, appsAsJSON.toString(), weekDaysASJSON.toString(), String.valueOf(limitInMin)}
                );
        goalsProperties.Write("int", _int == null ? "1" : String.valueOf(_int + 1));

        goalsProperties.Write("used", used == null ? "1" : String.valueOf(used + 1));
        return true;
    }

    public void edit(String fileName, String name, JSObject appsAsJSON, JSObject weekDaysASJSON, long limitInMin) {
        PropertiesManager propertiesManager = new PropertiesManager(
                context.getFilesDir() + "/goals",
                fileName
        );
        propertiesManager.Write(
                new String[] {"name", "apps", "weekDays", "limit"},
                new String[] {name, appsAsJSON.toString(), weekDaysASJSON.toString(), String.valueOf(limitInMin)}
        );
    }

    public JSObject getAllGoals(){
        File[] files = new File(context.getFilesDir() + "/goals").listFiles();

        if (files == null)
            return null;

        JSObject ret = new JSObject();

        int i=0;

        for (File file: files) {
            ret.put(i + "", getGoal(file.getPath()));
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
        ret.put("weekDays", JSONObject.quote(propertiesManager.Read("weekDays")));
        ret.put("limit", Long.valueOf(propertiesManager.Read("limit")));

        return ret;
    }


    public void delete(String fileName) {
        File file = new File(context.getFilesDir() + "/goals", fileName);
        file.delete();
    }
}
