package com.oceanpeace.redinn;

import android.os.Bundle;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        registerPlugin(TestPlugin.class);
        registerPlugin(MayoPlugin.class);
    }
}
