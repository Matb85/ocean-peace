package com.redinn.oceanpeace.icons;

import android.util.Log;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.redinn.oceanpeace.database.OceanDatabase;

@CapacitorPlugin(name = "Icons")
public class IconsPlugin extends Plugin {

    @Override
    public void load() {
        Log.d("IconPlugin", "Starting the instance");
    }

    @PluginMethod
    public void getAllIcons(PluginCall call) {
        JSObject res = new JSObject();

        JSArray icons = OceanDatabase.getInstance(getContext()).iconDAO().getAllIcons_JSON();

        res.put("apps", icons);

        call.resolve(res);
    }

    @PluginMethod
    public void getIcon(PluginCall call) {
        String packageName = call.getString("packageName");
        if (packageName == null) {
            call.reject("no packageName provided");
            return;
        }
        JSObject res = new JSObject();

        /* retrieve data */
        JSObject icon = OceanDatabase.getInstance(getContext()).iconDAO().getIcon(packageName).toJSON();

        res.put("app", icon);

        call.resolve(res);
    }
}
