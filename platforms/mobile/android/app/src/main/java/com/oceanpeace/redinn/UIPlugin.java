package com.redinn.oceanpeace;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.redinn.oceanpeace.managers.JSONManager;

import org.json.JSONObject;

import java.io.File;

@CapacitorPlugin(name = "UI")
public class UIPlugin extends Plugin {

    @PluginMethod
    public void fadeIn(PluginCall call) {
        this.getBridge().getWebView().animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                call.resolve();
            }
        });
    }

    @PluginMethod
    public void fadeOut(PluginCall call) {
        this.getBridge().getWebView().animate().alpha(1f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                call.resolve();
            }
        });
    }

    @PluginMethod
    public void setPreferences(PluginCall call) {
        try {
            JSONManager.writeFile(call.getObject("data"), FunctionBase.getFilesDir(getActivity().getApplicationContext()) + "/settings.json");
            call.resolve();
        } catch (Exception e) {
            Log.e("UIPlugin", e.toString());
            call.reject(e.toString());
        }
    }

    @PluginMethod
    public void setPreference(PluginCall call) {
        try {
            JSONObject data = new JSONObject();
            try {
                data = JSONManager.readFile(new File(FunctionBase.getFilesDir(getActivity().getApplicationContext()) + "/settings.json"));
            } catch (Exception e) {
                Log.e("UIPlugin", "settings.json does not exist " + e.toString());
            }
            data.put(call.getString("key"), call.getString("value"));
            JSONManager.writeFile(data, FunctionBase.getFilesDir(getActivity().getApplicationContext()) + "/settings.json");
            call.resolve();
        } catch (Exception e) {
            Log.e("UIPlugin", e.toString());
            call.reject(e.toString());
        }
    }

    @PluginMethod
    public void getPreferences(PluginCall call) {
        try {
            JSONObject data = new JSONObject();
            try {
                data = JSONManager.readFile(new File(FunctionBase.getFilesDir(getActivity().getApplicationContext()) + "/settings.json"));
            } catch (Exception e) {
                Log.e("UIPlugin", "settings.json does not exist " + e.toString());
            }            JSObject res = new JSObject();
            res.put("data", data);
            call.resolve(res);
        } catch (Exception e) {
            Log.e("UIPlugin", e.toString());
            call.reject(e.toString());
        }
    }
}

