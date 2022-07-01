package com.oceanpeace.redinn.goals;

import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

@CapacitorPlugin(name = "Goal")
public class GoalsPlugin extends Plugin {

    @PluginMethod
    public void saveGoal(PluginCall call) {
        /* get data */
        String id = call.getString("id");
        String name = call.getString("name");
        String apps = call.getString("apps");
        String limit = call.getString("limit");
        String activeDays = call.getString("activeDays");
        String limitActionType = call.getString("limitActionType");
        if (id == null || name == null || apps == null || limit == null || activeDays == null || limitActionType == null) {
            call.reject("not enough data"+id+" "+name+" "+apps+" "+limit+" "+activeDays+" "+limitActionType);
            return;
        }



        Goals goals = new Goals(getActivity().getApplicationContext());
        try {
            goals.createGoal(id, name, apps, limit, activeDays, limitActionType);
        } catch (IOException e) {
            call.reject("IO error" + e);
        }
        call.resolve();
    }

    @PluginMethod
    public void deleteGoal(PluginCall call) {
        String id = call.getString("id");
        if (id == null)
            call.reject("id cannot be null");

        Goals goal = new Goals(getActivity().getApplicationContext());
        goal.deleteGoal(id);
        call.resolve();
    }

    @PluginMethod
    public void getGoal(PluginCall call) {
        String id = call.getString("id");
        if (id == null)
            call.reject("fileName cannot be null");

        JSObject res = new JSObject();
        JSONObject goalData = new Goals(getActivity().getApplicationContext()).getGoal(id);

        res.put("goal", goalData);
        call.resolve(res);
    }

    @PluginMethod
    public void getAllGoals(PluginCall call) {
        JSONArray allGoals = new Goals(getActivity().getApplicationContext()).getAllGoals();
        JSObject res = new JSObject();

        res.put("goals", allGoals);
        call.resolve(res);
    }
}
