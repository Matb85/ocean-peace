package com.oceanpeace.redinn.presets;

import android.content.Context;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;

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

    private String getFileContents(final File file) throws IOException {
        final InputStream inputStream = new FileInputStream(file);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        final StringBuilder stringBuilder = new StringBuilder();

        boolean done = false;

        while (!done) {
            final String line = reader.readLine();
            done = (line == null);

            if (line != null) {
                stringBuilder.append(line);
            }
        }

        reader.close();
        inputStream.close();

        return stringBuilder.toString();
    }

    @PluginMethod
    public void savePreset(PluginCall call) {
        /* get data */
        Context ctx = getActivity().getApplicationContext();

        JSONObject data = call.getObject("data");
        try {
            String fileName = getPresetsFolder(ctx) + "/" + data.getString("id") + ".json";
            Log.d("PresetsPlugin", fileName + " " + data.toString(0));

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName));
            outputStreamWriter.write(data.toString(0));
            outputStreamWriter.close();
        } catch (IOException err) {
            Log.e("PresetsPlugin", err.toString());
        } catch (JSONException err) {
            Log.e("PresetsPlugin", err.toString());
        }

        call.resolve();
    }

    @PluginMethod
    public void deletePreset(PluginCall call) {
        String id = call.getString("id");

        call.resolve();
    }

    @PluginMethod
    public void getPreset(PluginCall call) {
        String id = call.getString("id");

        call.resolve();
    }

    @PluginMethod
    public void getAllPresets(PluginCall call) {
        Log.d("PresetsPlugin", "hello");

        Context ctx = getActivity().getApplicationContext();


        JSObject res = new JSObject();

        File[] files = new File(getPresetsFolder(ctx)).listFiles();



        /* put data from each goal the the final array */
        JSONArray arr = new JSONArray();

        if (files == null) {
            Log.d("PresetsPlugin", "no files, aborting");

            res.put("presets", arr);
            call.resolve(res);
            return;
        }
        Log.d("PresetsPlugin", "iterating through all files...");

        for (File file : files) {
            Log.d("PresetsPlugin", "reading file " + file.getName());
            try {
                String content = getFileContents(file);
                Log.d("PresetsPlugin", "file data: "+ content);
                arr.put(new JSONObject(content));
            } catch (IOException err) {
                Log.e("PresetsPlugin", err.toString());
            } catch (JSONException err) {
                Log.e("PresetsPlugin", err.toString());
            }
        }
        
        res.put("presets", arr);
        call.resolve(res);
    }
}
