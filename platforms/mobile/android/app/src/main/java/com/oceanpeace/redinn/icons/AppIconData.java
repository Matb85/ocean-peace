package com.redinn.oceanpeace.icons;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AppIconData {
    String packageName;
    String label;
    String iconPath;
    String version;

    public AppIconData(String packageName, String data) {
        this.packageName = packageName;
        parseData(data);
    }

    public void parseData(String data) {
        /* the data is stored in iconDB.properties in a single string for simplicity
         * one has to split it to separate values first - order matters! */
        String[] parsedData = data.split(";");
        this.label = parsedData[0];
        this.iconPath = parsedData[1];
        this.version = parsedData[2];
    }

    public String stringifyData() {
        return label + ";" + iconPath + ";" + version;
    }

    public JSONObject toJSON() {
        JSONObject appIcon = new JSONObject();
        try {
            appIcon.put("packageName", packageName);
            appIcon.put("label", label);
            appIcon.put("iconPath", iconPath);
            appIcon.put("version", version);
        } catch (JSONException err) {
            Log.e("IconsPlugin", err.toString());
            return null;
        }
        return appIcon;
    }
}
