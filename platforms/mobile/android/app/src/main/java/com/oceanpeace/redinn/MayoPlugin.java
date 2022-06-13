package com.oceanpeace.redinn;



import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
        name="Mayo",
        permissions = {
                @Permission(
                        alias = "usage",
                        strings = {
                                Manifest.permission.PACKAGE_USAGE_STATS
                        }
                )
        }
)
public class MayoPlugin extends Plugin {

    @PluginMethod()
    public void callMayo(PluginCall call) {
        if (getPermissionState("usage") != PermissionState.GRANTED) {
            
            requestPermissionForAlias("usage", call, "usagePermsCallback");
        } else {
            runMayo(call);
        }
    }

    @PermissionCallback
    private void usagePermsCallback(PluginCall call) {
        if (getPermissionState("usage") == PermissionState.GRANTED) {
            runMayo(call);
        } else {
            call.reject("Permission is required to run Mayo algorithm");
        }
    }

    void runMayo(PluginCall call) {
        Mayo mayo = new Mayo();
        JSObject ret = new JSObject();
        ret.put("stats", mayo.GetUsageData());
        call.resolve(ret);
    }
}
