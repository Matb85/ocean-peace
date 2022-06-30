package com.oceanpeace.redinn.goals;

import android.content.Context;
import android.util.Log;

import com.oceanpeace.redinn.PropertiesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Goals {
    Context context;

    public Goals(Context context) {
        this.context = context;
    }

    /**
     * Creates a goal file and saves all passed data
     *
     * @param name  name of the goal
     * @param id    {"0": "com.example.one", "1", "com.example.two", ...}
     * @param apps  "0100101"
     * @param limit time limit in minutes
     * @throws IOException throws expectation if IO fails
     */
    public void createGoal(String id, String name, String apps, String limit, String activeDays, String limitActionType) throws IOException {
        PropertiesManager goalsProperties = new PropertiesManager("goals.properties", context);
        /* get the list of all goals ids */
        String tempGoalsIdsString = goalsProperties.Read("goalsIds");
        String goalsIdsString = tempGoalsIdsString != null ? tempGoalsIdsString : "";
        String[] goalsIds = goalsIdsString.split(";");

        /* if the goals hasn't been in the config, add it */
        boolean doesTheGoalAlreadyExists = false;
        if (Arrays.asList(goalsIds).contains(id)) {
            Log.d("GoalsPlugin", "Goal " + id + "already exists");
            doesTheGoalAlreadyExists = true;
        } else {
            Log.d("GoalsPlugin", "Goal " + id + "does not exist");
            goalsIdsString = goalsIdsString.length() != 0 ? ";" + id : id;
        }
        if (!doesTheGoalAlreadyExists) {
            goalsProperties.Write("goalsIds", goalsIdsString + "");
        }

        /* update the goal file
         * it the goal is a new one create a new file
         * if the goal has existed before, overwrite the data
         * */
        PropertiesManager propertiesManager = new PropertiesManager(
                context.getFilesDir() + "/goals",
                id + ".properties"
        );

        propertiesManager.Create();
        propertiesManager.Write(
                new String[]{"id", "name", "apps", "activeDays", "limit", "limitActionType"},
                new String[]{id, name, apps, activeDays, limit, limitActionType}
        );
    }

    public JSONArray getAllGoals() {
        File[] files = new File(context.getFilesDir() + "/goals").listFiles();

        if (files == null)
            return null;

        /* put data from each goal the the final array */
        JSONArray ret = new JSONArray();

        for (File file : files) {
            JSONObject goal = getGoal(file.getName());
            ret.put(goal);
        }

        return ret;
    }


    public JSONObject getGoal(String id) {
        JSONObject res = new JSONObject();

        PropertiesManager goalData = new PropertiesManager(
                context.getFilesDir() + "/goals",
                id
        );

        try {
            res.put("id", goalData.Read("id"));
            res.put("name", goalData.Read("name"));
            res.put("apps", goalData.Read("apps"));
            res.put("limit", goalData.Read("limit"));
            res.put("activeDays", goalData.Read("activeDays"));
            res.put("limitActionType", goalData.Read("limitActionType"));
        } catch (JSONException e) {
            Log.e("GoalsPlugin", "JSON error" + e);
        }
        return res;
    }


    public void delete(String id) {
        PropertiesManager goalsProperties = new PropertiesManager("goals.properties", context);
        /* get the list of all goals ids */
        String tempGoalsIdsString = goalsProperties.Read("goalsIds");
        String goalsIdsString = tempGoalsIdsString != null ? tempGoalsIdsString : "";
        goalsIdsString.replace(id, "").replaceAll(";;", ";");
        /* update the db */
        goalsProperties.Write("goalsIds", goalsIdsString + "");

        /* delete the id from the database */
        File file = new File(context.getFilesDir() + "/goals", id + ".properties");
        file.delete();
    }
}
