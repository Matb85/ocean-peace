package com.oceanpeace.redinn.config;

import android.content.Context;

import com.getcapacitor.Plugin;

public class ConfigPlugin extends Plugin {
    public static String getFilesDir(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }
}
