package com.redinn.oceanpeace.mayo;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.redinn.oceanpeace.database.OceanDatabase;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;

public class Mayo extends AccessibilityService {

    String PREVIOUSLY_OPENED_PACKAGE_NAME;
    String CURRENT_PACKAGE_NAME;

    long previousOpenTime;

    MayoAPI API;
    boolean IS_API_BOUNDED = false;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MayoAPI.LocalBinder binder = (MayoAPI.LocalBinder) iBinder;
            API = binder.getService();
            IS_API_BOUNDED = true;

            Log.d("MAYO", "API service connected!");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            IS_API_BOUNDED = false;
        }
    };

    private static final String HANDLER_TOKEN = "MAYO.TOKEN";
    Handler localHandler = new Handler();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        run(accessibilityEvent);
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onCreate() {

        bindAPI();

        super.onCreate();
    }

    private void handlePreviousApp(long windowChangeTime) {

        Disposable d = OceanDatabase.getDatabase(getApplicationContext()).goalDAO().updateGoalSessionTime(
                PREVIOUSLY_OPENED_PACKAGE_NAME,
                windowChangeTime - previousOpenTime
        ).test().assertComplete();
        d.dispose();

        Log.i("TAG", "handlePreviousApp: aaaaa");

    }

    private void handleDisplayPopUp() {
        Disposable d = OceanDatabase.getDatabase(getApplicationContext()).goalDAO().getTimeLeftForApp(CURRENT_PACKAGE_NAME)
                .subscribeWith(new DisposableSingleObserver<Long>() {
            @Override
            public void onSuccess(@NonNull Long aLong) {
                long TIME_REMAINING = aLong;


                Log.i("BRUH", "onSuccess: " + aLong + " " + IS_API_BOUNDED);
                localHandler.postAtTime(new Runnable() {
                                       @Override
                                       public void run() {
                                           API.display();
                                       }
                                   }, HANDLER_TOKEN, SystemClock.uptimeMillis() + aLong);
                //Looper.loop();

                //MayoDisplay.close(getApplicationContext(), TIME_REMAINING);
                Log.i("Mayo -> [" + CURRENT_PACKAGE_NAME + "]", "display set up for " + TIME_REMAINING);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("Mayo -> [" + CURRENT_PACKAGE_NAME + "]", "no matching record");
            }
        });
        d.dispose();
    }

    void run(AccessibilityEvent accessibilityEvent) {
        CURRENT_PACKAGE_NAME = (String) accessibilityEvent.getPackageName();

        // EDGE CASE FOR LAST PACKAGE BEING NULL AT THE START
        if (PREVIOUSLY_OPENED_PACKAGE_NAME != null) {

            handlePreviousApp(accessibilityEvent.getEventTime());

        }

        // CHECK IF EVENT IS PART OF SAME APP
        if (!CURRENT_PACKAGE_NAME.equals(PREVIOUSLY_OPENED_PACKAGE_NAME)) {

            localHandler.removeCallbacksAndMessages(HANDLER_TOKEN);

            handleDisplayPopUp();
        }

        PREVIOUSLY_OPENED_PACKAGE_NAME = CURRENT_PACKAGE_NAME;
        previousOpenTime = accessibilityEvent.getEventTime();
    }


    void bindAPI() {
        bindService(new Intent(this, MayoAPI.class), connection, Context.BIND_AUTO_CREATE + Context.BIND_IMPORTANT);
    }

    class APICommunicationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ( intent.getAction().equals(MayoAPI.BROADCAST_REQUEST_BINDING)) {
                bindAPI();
                Log.d("MAYO", "binding to API service...");
            }
        }
    };

}