package com.redinn.oceanpeace.mayo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.redinn.oceanpeace.MainActivity;
import com.redinn.oceanpeace.R;
import com.redinn.oceanpeace.mayo.keepalive.KeepAliveReceiver;

public class MayoAPI extends Service {
    private static final String CHANNEL_ID = "ocean.mayo.channel.id";
    private static final String TAG = "MAYO.API";

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
        return APIBinder;
    }


    public static final String BROADCAST_REQUEST_BINDING = "mayo.req.bind";
    void requestBinding() {
        sendBroadcast(new Intent().setAction(BROADCAST_REQUEST_BINDING));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate called");
        createNotificationChanel();
        isServiceRunning = true;

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

        super.onDestroy();
    }





    void display() {

        View testView = LayoutInflater.from(this).inflate(R.layout.popup, null);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH +
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE +
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        params.setTitle("Load Average");


        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(testView, params);


        testView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        testView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return true;
            }
        });



        TextView text = (TextView) testView.findViewById(R.id.closeText);
        text.setText("Wypierdalaj nierobie");// OPTION: packageNames
        testView.findViewById(R.id.closePopupBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close app function
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);

                wm.removeView(testView);
            }
        });


    }
}
