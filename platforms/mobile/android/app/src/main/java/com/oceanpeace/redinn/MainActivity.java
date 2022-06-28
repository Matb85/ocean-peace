package com.oceanpeace.redinn;

import android.content.Context;
import android.os.Bundle;
import com.getcapacitor.BridgeActivity;
import com.oceanpeace.redinn.focus.FocusPlugin;
import com.oceanpeace.redinn.mayo.MayoPlugin;
import com.oceanpeace.redinn.Icons.IconsPlugin;

public class MainActivity extends BridgeActivity {
    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        registerPlugin(MayoPlugin.class);
        registerPlugin(FocusPlugin.class);
        registerPlugin(IconsPlugin.class);

    }
    public static Context getAppContext(){
        return MainActivity.context;
    }
}
