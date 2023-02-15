package com.redinn.oceanpeace.goals;

import static com.redinn.oceanpeace.FunctionBase.JSONArrayOptElement;

import android.content.Context;

import com.redinn.oceanpeace.MainActivity;
import com.redinn.oceanpeace.database.OceanDatabase;
import com.redinn.oceanpeace.database.goals.Goal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Class providing saving, loading and deleting functions for goals. <br>
 *  <br>
 * Goal basic structure:<br>
 *
 * { <br>
 * &emsp;"id": "goal1662045227355",<br>
 * &emsp;"name": "Lol",<br>
 * &emsp;"apps": "[\"com.Playrion.AirlinesManager2\"]",<br>
 * &emsp;"websites": "[{\"url\":\"Delta.com\",\"label\":\"*.Delta.com\",\"favicon\":\"\/globe.png\",\"type\":1}]",<br>
 * &emsp;"limit": "75",<br>
 * &emsp;"activeDays": "[\"Tue\"]",<br>
 * &emsp;"limitActionType": "Notification"<br>
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
//        // prevent creating goals in an nonexistent folder
//        String GOALS_FOLDER = FunctionBase.getFilesDir(context) + "/goals";
//        File goalsFolderFile = new File(GOALS_FOLDER);
//        if (!goalsFolderFile.isDirectory()) {
//            Log.i("GoalsPlugin", "creating the goals folder");
//            goalsFolderFile.mkdir();
//        }
//
//        JSONManager.writeFile(goal, FunctionBase.getFilesDir(context) + "/goals/" + goal.getString("id") + ".json");

        Goal temp = new Goal();
        temp.id = goal.getString(Goals.ID);
        temp.name = goal.getString(Goals.NAME);
        temp.apps = goal.getString(Goals.APPS);
        temp.websites = goal.getString(Goals.WEBSITES);
        temp.limit = goal.getString(Goals.LIMIT);
        temp.activeDays = goal.getString(Goals.ACTIVEDAYS);
        temp.type = goal.getString(Goals.LIMITACTIONTYPE);
        temp.sessionUpdate = goal.getString(Goals.SESSIONUPDATE);
        temp.sessionTime = goal.getString(Goals.SESSIONTIME);
        temp.sessionHistory = goal.getString(Goals.SESSIONSHISTORY);

        OceanDatabase.getInstance(context).goalDAO().insert(temp);

        MainActivity.mAPI.changeGoal(goal);
    }

    public JSONArray getAllGoals() {
//        File[] files = new File(FunctionBase.getFilesDir(context) + "/goals").listFiles();
//
//        if (files == null)
//            return new JSONArray();

        /* put data from each goal the the final array */
        JSONArray ret = new JSONArray();

//        Log.i("GoalsPlugin", "iterating through all files...");
//
//        for (File file : files) {
//
//            try {
//                JSONObject temp = null;
//                try {
//                    if (MainActivity.mBound && !MainActivity.mAPI.todayGoalsEmpty())
//                        temp = JSONArrayOptElement(MainActivity.mAPI.getTodayGoals(), ID, file.getName().substring(0, file.getName().length() - 5));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (temp != null) {
//                    Log.i("GoalsPlugin", "reading today goal " + temp.getString(ID));
//                    ret.put(temp);
//                }
//                else {
//                    Log.i("GoalsPlugin", "reading file " + file.getName());
//                    ret.put(JSONManager.readFile(file));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        List<Goal> goals = OceanDatabase.getInstance(context).goalDAO().getAllGoals();

        try {
            for (Goal goal : goals)
                ret.put(goal.toJSON());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public JSONObject getGoal(String id) {
        JSONObject res = new JSONObject();

        //File file = new File(FunctionBase.getFilesDir(context) + "/goals/" + id + ".json");

        try {

            // checking if the goal is in today's goals to update it's data
            JSONObject temp = null;
            if (MainActivity.mBound && !MainActivity.mAPI.todayGoalsEmpty()) {
                temp = JSONArrayOptElement(MainActivity.mAPI.getTodayGoals(), ID, id);
            }
            if (temp != null) {
                res = temp;
            }
            else {

                Goal goals = OceanDatabase.getInstance(context).goalDAO().getGoalByName(id);
                res = goals.toJSON();

                //res = JSONManager.readFile(file);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }



        return res;
    }


    public void deleteGoal(String id) {
        /* delete the id from the database */
       // File file = new File(FunctionBase.getFilesDir(context) + "/goals/" + id + ".json");

        try {
            if (MainActivity.mBound)
                MainActivity.mAPI.deleteGoal(new JSONObject().put(Goals.ID, id));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Goal temp = new Goal();
        temp.id = id;

        OceanDatabase.getInstance(context).goalDAO().delete(temp);

       // file.delete();
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
     * String containing name of <b>limit action type</b> field
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
