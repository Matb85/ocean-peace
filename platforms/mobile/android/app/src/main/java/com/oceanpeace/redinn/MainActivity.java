package com.oceanpeace.redinn;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.getcapacitor.BridgeActivity;
import com.oceanpeace.redinn.focus.FocusPlugin;
import com.oceanpeace.redinn.goals.GoalsPlugin;
import com.oceanpeace.redinn.mayo.MayoPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class MainActivity extends BridgeActivity {

    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        registerPlugin(TestPlugin.class);
        registerPlugin(MayoPlugin.class);
        registerPlugin(FocusPlugin.class);
        registerPlugin(GoalsPlugin.class);
        MainActivity.context = getApplicationContext();

        checkData();
    }

    public static Context getAppContext()
    {
        return MainActivity.context;
    }



    private void checkData() {
        String propertiesDir = context.getFilesDir().getPath();
        Log.d("PROP", "checkData: " + propertiesDir);

        // Checking goals files
        File goal = new File(propertiesDir + "/goals.properties");
        Properties prop = new Properties();
        if (!goal.exists())
        {
            try {
                goal.createNewFile();
                prop.setProperty("limit", 3 + "");
                prop.setProperty("used", 0 + "");
                prop.store(new FileOutputStream(goal), null);
            }
            catch (IOException e) {
                Log.d("FAIL", "checkData: \"goals.properties\" create filed");
            }
        }
        try  {
            prop = new Properties();
            prop.load(new FileInputStream(goal));
            if (prop.getProperty("limit") == null)
                prop.setProperty("limit", 3 + "");
            if (prop.getProperty("used") == null)
                prop.setProperty("used", 0 + "");
            if (prop.getProperty("int") == null)
                prop.setProperty("int", 0 + "");
            prop.store(new FileOutputStream(goal), null);
        }
        catch (IOException e) {
            Log.d("FAIL", "checkData: \"goals.properties\" data filed");
        }
        goal = new File(propertiesDir + "/goals");
        if (!goal.exists()) {
            goal.mkdir();
        }

    }
}
