package com.oceanpeace.redinn;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "UI")
public class UIPlugin extends Plugin {

    @PluginMethod
    public void fadeIn(PluginCall call) {
        this.getBridge().getWebView().animate().alpha(0).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                call.resolve();
            }
        });

    }
    @PluginMethod
    public void fadeOut(PluginCall call) {
        this.getBridge().getWebView().animate().alpha(1).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                call.resolve();
            }
        });
    }
}

