package com.oceanpeace.redinn.goals;

import static com.oceanpeace.redinn.FunctionBase.JSONArrayOptElement;

import android.content.Context;
import android.util.Log;

import com.oceanpeace.redinn.FunctionBase;
import com.oceanpeace.redinn.mayo.Mayo;
import com.oceanpeace.redinn.managers.JSONManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Class providing saving, loading and deleting functions for goals. <br/>
 *  <br/>
 * Goal basic structure:<br/>
 *
 * { <br/>
 * &emsp"id": "goal1662045227355",<br/>
 * &emsp"name": "Lol",<br/>
 * &emsp"apps": "[\"com.Playrion.AirlinesManager2\"]",<br/>
 * &emsp"websites": "[{\"url\":\"Delta.com\",\"label\":\"*.Delta.com\",\"favicon\":\"\/globe.png\",\"type\":1}]",<br/>
 * &emsp"limit": "75",<br/>
 * &emsp"activeDays": "[\"Tue\"]",<br/>
 * &emsp"limitActionType": "Notification"<br/>
 * }
 */
public class Goals {
    // region constructor
    Context context;

    public Goals(Context context) {
        this.context = context;
    }
    //endregion

    // TODO: make updating goal synchronize progress
    public void saveGoal(JSONObject goal) throws Exception {

        if (goal.opt(SESSIONUPDATE) == null)
            goal.put(SESSIONUPDATE, "");

        if (goal.opt(SESSIONTIME) == null)
            goal.put(SESSIONTIME, "0");

        if (goal.opt(SESSIONSHISTORY) == null)
            goal.put(SESSIONSHISTORY, new JSONArray().toString());


        JSONManager.writeFile(goal, FunctionBase.getFilesDir(context) + "/goals/" + goal.getString("id") + ".json");

        try {
            Mayo.changeTodayGoals(goal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getAllGoals() {
        File[] files = new File(FunctionBase.getFilesDir(context) + "/goals").listFiles();

        if (files == null)
            return null;

        /* put data from each goal the the final array */
        JSONArray ret = new JSONArray();

        Log.i("GoalsPlugin", "iterating through all files...");

        for (File file : files) {

            try {
                JSONObject temp = JSONArrayOptElement(Mayo.todayGoals, ID, file.getName().substring(0,file.getName().length()-5));
                if (temp != null) {
                    Log.i("GoalsPlugin", "reading today goal " + temp.getString(ID));
                    ret.put(temp);
                }
                else {
                    Log.i("GoalsPlugin", "reading file " + file.getName());
                    ret.put(JSONManager.readFile(file));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        return ret;
    }

    public JSONObject getGoal(String id) {
        JSONObject res = new JSONObject();

        File file = new File(FunctionBase.getFilesDir(context) + "/goals/" + id + ".json");

        try {

            // checking if the goal is in today's goals to update it's data
            JSONObject temp = JSONArrayOptElement(Mayo.todayGoals, ID, id);
            if (temp != null) {
                res = temp;
            }
            else {
                res = JSONManager.readFile(file);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }



        return res;
    }


    public void deleteGoal(String id) {
        /* delete the id from the database */
        File file = new File(FunctionBase.getFilesDir(context) + "/goals/" + id + ".json");

        try {
            Mayo.deleteTodayGoal(new JSONObject().put(Goals.ID, id));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        file.delete();
    }



    //region fieldsNames
    /**
     * String containing name of <b>id</b> field
     */
    public static String ID = "id";
    /**
     * String containing name of <b>name</b> field
     */
    public static String NAME = "name";
    /**
     * String containing name of <b>apps</b> field
     */
    public static String APPS = "apps";
    /**
     * String containing name of <b>websites</b> field
     */
    public static String WEBSITES = "websites";
    /**
     * String containing name of <b>limit</b> field
     */
    public static String LIMIT = "limit";
    /**
     * String containing name of <b>active days</b> field
     */
    public static String ACTIVEDAYS = "activeDays";
    /**
     * String containing name of <b>limit action type/b> field
     */
    public static String LIMITACTIONTYPE = "limitActionType";
    /**
     * String containing name of <b>session update date</b> field
     */
    public static String SESSIONUPDATE = "sessionUpdate";
    /**
     * String containing name of <b>session activity time</b> field
     */
    public static String SESSIONTIME = "sessionTime";
    /**
     * String containing name of <b>sessions' history</b> field
     */
    public static String SESSIONSHISTORY = "sessionHistory";
    //endregion
}
