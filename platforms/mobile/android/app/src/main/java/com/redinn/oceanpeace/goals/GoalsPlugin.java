package com.redinn.oceanpeace.goals;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.redinn.oceanpeace.database.OceanDatabase;
import com.redinn.oceanpeace.database.goals.Goal;

import java.util.List;
import java.util.Objects;

@CapacitorPlugin(name = "Goal")
public class GoalsPlugin extends Plugin {

    @PluginMethod
    public void saveGoal(PluginCall call) {

        JSObject retrieved = call.getObject("data");
        Goal temp = new Goal();

        temp.id = Objects.requireNonNull(retrieved.getString(Goal.ID));
        temp.name = retrieved.getString(Goal.NAME);
        temp.apps = retrieved.getString(Goal.APPS);
        temp.websites = retrieved.getString(Goal.WEBSITES);
        temp.limit = retrieved.getString(Goal.LIMIT);
        temp.activeDays = retrieved.getString(Goal.ACTIVE_DAYS);
        temp.type = retrieved.getString(Goal.LIMIT_ACTION_TYPE);
        temp.sessionUpdate = retrieved.getString(Goal.SESSION_UPDATE);
        temp.sessionTime = retrieved.getString(Goal.SESSION_TIME);
        temp.sessionHistory = retrieved.getString(Goal.SESSION_HISTORY);

        OceanDatabase.getInstance(getContext().getApplicationContext()).goalDAO().insert(temp);

        call.resolve();
    }

    @PluginMethod
    public void deleteGoal(PluginCall call) {
        String id = call.getString("id");
        if (id == null)
            call.reject("id cannot be null");

        Goal temp = new Goal();
        temp.id = id;

        OceanDatabase.getInstance(getContext().getApplicationContext()).goalDAO().delete(temp);

        call.resolve();
    }

    @PluginMethod
    public void getGoal(PluginCall call) {
        String id = call.getString("id");
        if (id == null)
            call.reject("id cannot be null");

        JSObject res = OceanDatabase.getInstance(getContext()).goalDAO().getGoalByName(id).toJSON();

        call.resolve(res);
    }

    @PluginMethod
    public void getAllGoals(PluginCall call) {
        List<Goal> allGoals = OceanDatabase.getInstance(getContext()).goalDAO().getAllGoals();
        JSObject res = new JSObject();

        JSArray array = new JSArray();

        for (Goal temp : allGoals)
            array.put(temp.toJSON());

        res.put("goals", array);
        call.resolve(res);
    }
}
