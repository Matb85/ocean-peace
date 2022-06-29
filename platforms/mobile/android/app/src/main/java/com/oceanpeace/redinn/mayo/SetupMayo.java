package com.oceanpeace.redinn.mayo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.getcapacitor.JSObject;
import com.oceanpeace.redinn.goals.Goals;

import java.util.Calendar;
import java.util.Iterator;

public class SetupMayo extends Service {
    public SetupMayo() {
    }

    @Override
    public int onStartCommand( Intent intent , int flags , int startId ) {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);

        JSObject goals = new JSObject();
        Goals goalsManager = new Goals(getApplicationContext());

        goals = goalsManager.getAllGoals();
        Iterator<String> iterator = goals.keys();


        int CODE = 6008;

        Intent piIntent = new Intent(getApplicationContext(), GoalMayo.class);
        int i=0;
        int goalsI=0;
        long[] limits;

        switch(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                while(iterator.hasNext()) {
                    
                }
                CODE = 6001;
                break;
            case Calendar.TUESDAY:
                CODE = 6002;
                break;
            case Calendar.WEDNESDAY:
                CODE = 6003;
                break;
            case Calendar.THURSDAY:
                CODE = 6004;
                break;
            case Calendar.FRIDAY:
                CODE = 6005;
                break;
            case Calendar.SATURDAY:
                CODE = 6006;
                break;
            case Calendar.SUNDAY:
                CODE = 6007;
                break;
        }



        PendingIntent goalMayoPI = PendingIntent.getForegroundService(
                getApplicationContext(),
                CODE,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT + PendingIntent.FLAG_IMMUTABLE
                );


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}