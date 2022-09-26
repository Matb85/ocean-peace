package com.redinn.oceanpeace.icons;

import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

@CapacitorPlugin(name = "Icons")
public class IconsPlugin extends Plugin {

    @Override
    public void load() {
        Log.d("IconPlugin", "Starting the instance");
    }

    private JSONArray sortListToJSON(List<JSONObject> list) {
        list.sort(new Comparator<JSONObject>() {
            // You can change "Name" with "ID" if you want to sort by ID
            private static final String KEY_NAME = "label";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = "";
                String valB = "";

                try {
                    valA = (String) a.get(KEY_NAME);
                    valB = (String) b.get(KEY_NAME);
                } catch (JSONException e) {
                    Log.e("IconsPlugin", "error white sorting data");
                }

                return valA.compareTo(valB);
                // if you want to change the sort order, simply use the following:
                // return -valA.compareTo(valB);
            }
        });

        JSONArray sorted = new JSONArray();

        for (int i = 0; i < list.size(); i++) {
            sorted.put(list.get(i));
        }
        return sorted;
    }

    @PluginMethod
    public void getAllIcons(PluginCall call) {
        /* note: one cannot access actual files! */
        JSObject res = new JSObject();
        List<JSONObject> sortable = new ArrayList<>();

        /* retrieve data */
        Properties iconDB = IconManager.getIconsData(getActivity().getApplicationContext());

        Enumeration<String> props = (Enumeration<String>) iconDB.propertyNames();
        while (props.hasMoreElements()) {
            /* get the app's package name */
            String packageName = props.nextElement();
            String data = iconDB.getProperty(packageName);
            sortable.add((new AppIconData(packageName, data)).toJSON());
        }
        res.put("apps", sortListToJSON(sortable));

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
        Properties iconDB = IconManager.getIconsData(getActivity().getApplicationContext());
        String data = iconDB.getProperty(packageName);
        if (data == null) {
            call.reject("no data for packageName " + packageName + " found");
            return;
        }
        AppIconData parsedData = new AppIconData(packageName, data);

        res.put("app", parsedData.toJSON());

        call.resolve(res);
    }
}
