package com.oceanpeace.redinn.goals;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.IOException;

@CapacitorPlugin(
        name = "Goal"
)
public class GoalsPlugin extends Plugin {

    @PluginMethod
    public void create(PluginCall call) {
        String name = call.getString("name", "Not named goal");
        JSObject apps = call.getObject("apps", null);
        JSObject weekDays = call.getObject("weekDays");
        if (weekDays == null)
            call.reject("Error occurred: weekDays empty");
        long limit = call.getLong("limit", 120l);

        Goals goals = new Goals(getActivity().getApplicationContext());
        try {
            if(!goals.createGoal(name, apps, weekDays, limit))
              call.reject("Limit reached");
        }
        catch (IOException ex) {
            call.reject("IO error");
        }
        call.resolve();
    }

    @PluginMethod
    public void edit(PluginCall call) {
        String fileName = call.getString("fileName");
        if (fileName == null)
            call.reject("fileName cannot be null");
        String name = call.getString("name", "Not named goal");
        JSObject apps = call.getObject("apps", null);
        JSObject weekDays = call.getObject("weekDays");
        if (weekDays == null)
            call.reject("Error occurred: weekDays empty");
        long limit = call.getLong("limit", 120l);

        Goals goal = new Goals(getActivity().getApplicationContext());
        goal.edit(fileName, name, apps, weekDays, limit);
        call.resolve();
    }

    @PluginMethod
    public void delete(PluginCall call) {
        String fileName = call.getString("fileName");
        if (fileName == null)
            call.reject("fileName cannot be null");

        Goals goal = new Goals(getActivity().getApplicationContext());
        goal.delete(fileName);
        call.resolve();
    }

    @PluginMethod
    public void get(PluginCall call) {
        String fileName = call.getString("fileName");
        if (fileName == null)
            call.reject("fileName cannot be null");

        JSObject ret = new Goals(getActivity().getApplicationContext()).getGoal(fileName);
        call.resolve(ret);
    }

    @PluginMethod
    public void getAll(PluginCall call) {
        JSObject ret = new Goals(getActivity().getApplicationContext()).getAllGoals();
        call.resolve(ret);
    }
}
