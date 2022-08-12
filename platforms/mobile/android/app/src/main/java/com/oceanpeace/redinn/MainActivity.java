package com.oceanpeace.redinn;

import android.os.Bundle;
import android.util.Log;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.getcapacitor.BridgeActivity;
import com.oceanpeace.redinn.focus.Focus;
import com.oceanpeace.redinn.focus.FocusPlugin;
import com.oceanpeace.redinn.goals.GoalsPlugin;
import com.oceanpeace.redinn.icons.IconWorker;
import com.oceanpeace.redinn.icons.IconsPlugin;
import com.oceanpeace.redinn.mayo.GoalMayo;
import com.oceanpeace.redinn.mayo.MayoPlugin;
import com.oceanpeace.redinn.presets.PresetsPlugin;
import com.oceanpeace.redinn.schedule.SchedulePlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        checkData();
        Focus.getInstance().setContextElements(this.getApplicationContext());

//        Goals goals = new Goals(getApplicationContext());
//        JSObject temp = new JSObject();
//        temp.put("1", "com.oceanpeace.redinn");
//        try {
//            goals.createGoal("name", temp, "1111111", 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        WorkManager.getInstance(getApplicationContext()).enqueueUniqueWork(6002+"", ExistingWorkPolicy.REPLACE,new OneTimeWorkRequest.Builder(GoalMayo.class).build());

        WorkManager.getInstance(getApplicationContext()).enqueue(IconWorker.regenerateIcons);

        registerPlugin(MayoPlugin.class);
        registerPlugin(FocusPlugin.class);
        registerPlugin(GoalsPlugin.class);
        registerPlugin(IconsPlugin.class);
        registerPlugin(PresetsPlugin.class);
        registerPlugin(SchedulePlugin.class);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        WorkManager.getInstance(getApplicationContext()).cancelAllWork();
        super.onDestroy();
    }

    private void checkData() {
        String propertiesDir = getApplicationContext().getFilesDir().getPath();
        Log.d("PROP", "checkData: " + propertiesDir);

        // Checking goals files
        File goal = new File(propertiesDir + "/goals.properties");
        Properties prop = new Properties();
        if (!goal.exists())
        {
            try {
                Log.d("CDATA", "checkData: Creating file");
                goal.createNewFile();
                prop.setProperty("limit", 3 + "");
                prop.setProperty("used", 0 + "");
                prop.setProperty("int", 0 + "");
                prop.store(new FileOutputStream(goal), null);
            }
            catch (IOException e) {
                Log.d("FAIL", "checkData: \"goals.properties\" create filed");
            }
        }
//        try  {
//            prop = new Properties();
//            FileInputStream in = new FileInputStream(goal);
//            prop.load(in);
//            in.close();
//            if (prop.getProperty("limit") == null)
//                prop.setProperty("limit", 3 + "");
//            if (prop.getProperty("used") == null)
//                prop.setProperty("used", 0 + "");
//            if (prop.getProperty("int") == null)
//                prop.setProperty("int", 0 + "");
//            prop.store(new FileOutputStream(goal), null);
//        }
//        catch (IOException e) {
//            Log.d("FAIL", "checkData: \"goals.properties\" data filed");
//        }
        goal = new File(propertiesDir + "/goals");
        if (!goal.exists()) {
            goal.mkdir();
        }

    }
}
