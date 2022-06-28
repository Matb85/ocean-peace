package com.oceanpeace.redinn.Icons;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.oceanpeace.redinn.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Enumeration;
import java.util.Properties;

@CapacitorPlugin(name = "Icons")
public class IconsPlugin extends Plugin {
    final private Context ctx;
    final private IconManager im;

    public IconsPlugin() {
        super();

        Log.d("IconPlugin", "Starting the instance");
        this.ctx = MainActivity.getAppContext();
        this.im = new IconManager(ctx);
        im.regenerateIcons();
    }

    @PluginMethod
    public void getAllIcons(PluginCall call) {
        /* note: one cannot access actual files! */
        JSObject res = new JSObject();
        JSONArray apps = new JSONArray();
        /* retrieve data */
        Properties iconDB = im.getIconsData();
        Enumeration<String> props = (Enumeration<String>) iconDB.propertyNames();
        while (props.hasMoreElements()) {
            JSONObject appIcon = new JSONObject();
            /* get the app's package name */
            String packageName = props.nextElement();
            /* the data is stored in a single string for simplicity
            * one has to split it to separate values first - order matters! */
            String[] data = iconDB.getProperty(packageName).split(";");
            String label = data[0], iconPath = data[1], version = data[2];
            Log.e("AppData", packageName + " " + label + " " + iconPath + " " + version);
            try {
                appIcon.put("packageName", packageName);
                appIcon.put("name", label);
                appIcon.put("src", iconPath);
                appIcon.put("version", version);
            } catch (JSONException err) {
                Log.e("IconsPlugin", err.toString());
            }
            apps.put(appIcon);
        }
        res.put("apps", apps);

        call.resolve(res);
    }

}
