package com.oceanpeace.redinn;

import android.content.Context;
import android.os.Bundle;

import com.getcapacitor.BridgeActivity;
import com.oceanpeace.redinn.focus.FocusPlugin;
import com.oceanpeace.redinn.mayo.MayoPlugin;

public class MainActivity extends BridgeActivity {
    private static Context context;

    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        registerPlugin(TestPlugin.class);
        registerPlugin(MayoPlugin.class);
        registerPlugin(FocusPlugin.class);
        MainActivity.context = getApplicationContext();
        registerPlugin(AppIcons.class);
        this.context = getApplicationContext();
    }
    public static Context getAppContext(){
        return MainActivity.context;
    }
}
