package com.oceanpeace.redinn;

import android.content.Context;
import android.os.Bundle;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        registerPlugin(TestPlugin.class);
        registerPlugin(MayoPlugin.class);
        MainActivity.context = getApplicationContext();


    }

    public static Context getAppContext()
    {
        return MainActivity.context;
    }
}
