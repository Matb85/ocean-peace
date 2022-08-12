package com.oceanpeace.redinn.presets;

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

@CapacitorPlugin(name = "Presets")
public class PresetsPlugin extends Plugin {
    private String getPresetsFolder(Context ctx) {
        return ctx.getDataDir().getAbsolutePath() + "/presets";
    }

    @Override
    public void load() {
        Context ctx = getActivity().getApplicationContext();

        File iconFolderFile = new File(getPresetsFolder(ctx));
        if (!iconFolderFile.isDirectory()) {
            Log.d("PresetsPlugin", "creating the presets folder");
            iconFolderFile.mkdir();
        }
    }

    @PluginMethod
    public void savePreset(PluginCall call) {
        /* get data */
        Context ctx = getActivity().getApplicationContext();

        JSONObject data = call.getObject("data");
        try {
            String fileName = getPresetsFolder(ctx) + "/" + data.getString("id") + ".json";
            Log.d("PresetsPlugin", fileName + " " + data.toString(0));

            JSONManager.writeFile(data, fileName);
        } catch (Exception err) {
            Log.e("PresetsPlugin", err.toString());
        }
        call.resolve();
    }

    @PluginMethod
    public void getPreset(PluginCall call) {
        Context ctx = getActivity().getApplicationContext();
        JSObject res = new JSObject();

        File file = new File(getPresetsFolder(ctx) + "/" + call.getString("id") + ".json");
        try {
            res.put("preset", JSONManager.readFile(file));
        } catch (Exception err) {
            Log.e("PresetsPlugin", err.toString());
        }
        call.resolve(res);
    }

    @PluginMethod
    public void getAllPresets(PluginCall call) {
        Context ctx = getActivity().getApplicationContext();

        JSObject res = new JSObject();
        File[] files = new File(getPresetsFolder(ctx)).listFiles();
        /* put data from each goal the the final array */
        JSONArray arr = new JSONArray();

        if (files == null) {
            Log.d("PresetsPlugin", "no presets, aborting");

            res.put("presets", arr);
            call.resolve(res);
            return;
        }
        Log.i("PresetsPlugin", "iterating through all files...");

        for (File file : files) {
            Log.i("PresetsPlugin", "reading file " + file.getName());
            try {
                arr.put(JSONManager.readFile(file));
            } catch (Exception err) {
                Log.e("PresetsPlugin", err.toString());
            }
        }

        res.put("presets", arr);
        call.resolve(res);
    }

    @PluginMethod
    public void deletePreset(PluginCall call) {
        Context ctx = getActivity().getApplicationContext();
        String filePath = getPresetsFolder(ctx) + "/" + call.getString("id") + ".json";
        File file = new File(filePath);
        Log.d("PresetsPlugin", "deleting file " + filePath);

        file.delete();

        call.resolve();
    }
}
