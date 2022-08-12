package com.oceanpeace.redinn.schedule;

import static com.oceanpeace.redinn.config.ConfigPlugin.getFilesDir;

import android.content.Context;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.oceanpeace.redinn.managers.JSONManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

@CapacitorPlugin(name = "Schedule")
public class SchedulePlugin extends Plugin {
    private String getSchedulesFolder(Context ctx) {
        return getFilesDir(ctx) + "/schedule";
    }

    @Override
    public void load() {
        Context ctx = getActivity().getApplicationContext();

        File iconFolderFile = new File(getSchedulesFolder(ctx));
        if (!iconFolderFile.isDirectory()) {
            Log.d("SchedulesPlugin", "creating the schedule folder");
            iconFolderFile.mkdir();
        }
    }

    @PluginMethod
    public void saveSchedule(PluginCall call) {
        /* get data */
        Context ctx = getActivity().getApplicationContext();

        JSONObject data = call.getObject("data");
        try {
            String fileName = getSchedulesFolder(ctx) + "/" + data.getString("id") + ".json";
            Log.d("SchedulesPlugin", fileName + " " + data.toString(0));

            JSONManager.writeFile(data, fileName);
        } catch (Exception err) {
            Log.e("SchedulesPlugin", err.toString());
        }
        call.resolve();
    }

    @PluginMethod
    public void getSchedule(PluginCall call) {
        Context ctx = getActivity().getApplicationContext();
        JSObject res = new JSObject();

        File file = new File(getSchedulesFolder(ctx) + "/" + call.getString("id") + ".json");
        try {
            res.put("schedule", JSONManager.readFile(file));
        } catch (Exception err) {
            Log.e("SchedulesPlugin", err.toString());
        }
        call.resolve(res);
    }

    @PluginMethod
    public void getAllSchedules(PluginCall call) {
        Context ctx = getActivity().getApplicationContext();

        JSObject res = new JSObject();
        File[] files = new File(getSchedulesFolder(ctx)).listFiles();
        /* put data from each goal the the final array */
        JSONArray arr = new JSONArray();

        if (files == null) {
            Log.d("SchedulesPlugin", "no Schedules, aborting");

            res.put("schedules", arr);
            call.resolve(res);
            return;
        }
        Log.d("SchedulesPlugin", "iterating through all files...");

        for (File file : files) {
            Log.d("SchedulesPlugin", "reading file " + file.getName());
            try {
                arr.put(JSONManager.readFile(file));
            } catch (Exception err) {
                Log.e("SchedulesPlugin", err.toString());
            }
        }

        res.put("schedules", arr);
        call.resolve(res);
    }

    @PluginMethod
    public void deleteSchedule(PluginCall call) {
        Context ctx = getActivity().getApplicationContext();
        String filePath = getSchedulesFolder(ctx) + "/" + call.getString("id") + ".json";
        File file = new File(filePath);
        Log.d("SchedulesPlugin", "deleting file " + filePath);

        file.delete();

        call.resolve();
    }
}
