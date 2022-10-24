package com.redinn.oceanpeace.mayo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MayoAPI extends Service {

    // region communication
    private final IBinder mBinder = new LocalBinder();
    apiBroadcast mBroadcast = new apiBroadcast();

    static boolean isResponsive = false;

    public class LocalBinder extends Binder {
        public MayoAPI getService() {
            return MayoAPI.this;
        }
    }

    @Override
    public void onCreate() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ocean.waves.mapi.update");
        registerReceiver(mBroadcast, filter);

        sendBroadcast(new Intent().setAction("ocean.waves.mayo.force_update"));
        isResponsive = false;

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mBroadcast);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



    public class apiBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "ocean.waves.mapi.update":
                    try {
                        todayGoals = new JSONArray(intent.getStringExtra("todayGoals"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    isResponsive = true;
                    break;
                default:
                    break;
            }
        }
    }

    // endregion





    // region API

    private JSONArray todayGoals = new JSONArray();

    public boolean todayGoalsEmpty() {
        return todayGoals == null;
    }

    public JSONArray getTodayGoals() {
        Log.i("MayoAPI", "getTodayGoals");
        return todayGoals;
    }

    public void changeGoal(JSONObject goal) {

        Log.i("MayoAPI", "changeGoal: sending change request");

        Intent intent = new Intent();
        intent.setAction("ocean.waves.mayo.change");
        intent.putExtra("goal", goal.toString());
        this.sendBroadcast(intent);
    }

    public void deleteGoal(JSONObject goal) {

        Log.i("MayoAPI", "deleteGoal: sending delete request");

        Intent intent = new Intent();
        intent.setAction("ocean.waves.mayo.delete");
        intent.putExtra("goal", goal.toString());
        this.sendBroadcast(intent);
    }
    // endregion
}
