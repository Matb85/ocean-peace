package com.oceanpeace.redinn.Icons;

import android.content.Context;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.oceanpeace.redinn.MainActivity;

import org.json.JSONArray;

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
            /* get the app's package name */
            String packageName = props.nextElement();
            String data = iconDB.getProperty(packageName);
            apps.put((new AppIconData(packageName, data)).toJSON());
        }
        res.put("apps", apps);

        call.resolve(res);
    }

}
