package com.oceanpeace.redinn.icons;

import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONArray;

import java.util.Enumeration;
import java.util.Properties;

@CapacitorPlugin(name = "Icons")
public class IconsPlugin extends Plugin {

    @Override
    public void load() {
        Log.d("IconPlugin", "Starting the instance");
        IconManager.regenerateIcons(getActivity().getApplicationContext());
    }

    @PluginMethod
    public void getAllIcons(PluginCall call) {
        /* note: one cannot access actual files! */
        JSObject res = new JSObject();
        JSONArray apps = new JSONArray();
        /* retrieve data */
        Properties iconDB = IconManager.getIconsData(getActivity().getApplicationContext());

        Enumeration<String> props = (Enumeration<String>) iconDB.propertyNames();
        while (props.hasMoreElements()) {
            /* get the app's package name */
            String packageName = props.nextElement();
            String data = iconDB.getProperty(packageName);
            apps.put((new AppIconData(packageName, data)).toJSON());
        }
        res.put("apps", apps);

        call.resolve(res);
    }

    @PluginMethod
    public void getIcons(PluginCall call) {
    }

    @PluginMethod
    public void getIcon(PluginCall call) {
    }
}
