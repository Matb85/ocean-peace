package com.redinn.oceanpeace.mayo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.redinn.oceanpeace.MainActivity;
import com.redinn.oceanpeace.R;
import com.redinn.oceanpeace.mayo.keepalive.KeepAlivePinger;
import com.redinn.oceanpeace.mayo.keepalive.KeepAliveReceiver;

public class MayoAPI extends Service {
    private static final String CHANNEL_ID = "ocean.mayo.channel.id";
    private static final String TAG = "MAPI";

    public static boolean isServiceRunning = false;

    public Context getMayoContext() {
        return this;
    }


    public class LocalBinder extends Binder {
        MayoAPI getService() {
            return MayoAPI.this;
        }
    }
    private final IBinder APIBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Binding...");
        return APIBinder;
    }


    public static final String BROADCAST_REQUEST_BINDING = "mayo.req.bind";
    void requestBinding() {
        sendBroadcast(new Intent().setAction(BROADCAST_REQUEST_BINDING));
    }



    MayoDisplay Display = new MayoDisplay() {
        @Override
        public Runnable closeAppDisplay(Context context) {
            return super.closeAppDisplay(context);
        }
    };

    Runnable closeApp() {
        return Display.closeAppDisplay(this);
    }


    KeepAlivePinger Ping = new KeepAlivePinger();



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate called");
        createNotificationChanel();
        isServiceRunning = true;

        if (!Ping.isRunning())
            Ping.preparePing();

        requestBinding();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");



        startForeground(1, foregroundNotification());
        return START_STICKY;
    }

    private Notification foregroundNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Mayo is Running")
                .setContentText("We will keep u productive!")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .build();
    }

    private void createNotificationChanel() {
        NotificationChannel mayoChannel = new NotificationChannel(
                CHANNEL_ID,
                "Mayo",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(mayoChannel);
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy called");
        isServiceRunning = false;

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(KeepAliveReceiver.RESTART_API_SERVICE);
        sendBroadcast(broadcastIntent);

        Ping.stop();

        super.onDestroy();
    }

}
