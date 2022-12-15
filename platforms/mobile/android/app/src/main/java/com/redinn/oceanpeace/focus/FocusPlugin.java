package com.redinn.oceanpeace.focus;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.redinn.oceanpeace.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@CapacitorPlugin(
        name="Focus"
)

/**
 *  TODO: function checking if focus is already running
 *  TODO: finish api for FocusService
 */
public class FocusPlugin extends Plugin {

    FocusService mService;
    boolean mBound = false;

    @PluginMethod
    public void startStopwatch(PluginCall call) throws JSONException {
        JSObject c = call.getData();
        Log.i("T", "startStopwatch: " + c.toString());
        JSONArray packages = new JSONArray();

        try {
            packages = new JSONArray(c.getString("packages"));
        } catch (NullPointerException e) {
            call.reject("Data provided is empty", e);
        }

        if (packages == null || packages.length() == 0) {
            call.reject("Data provided is empty");
        }

        try {
            MainActivity.connectFocus(getActivity().getApplicationContext());
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        List<String> names = new ArrayList<>();
        for (int i=0; i< packages.length(); i++)
            // TODO: exception log
            names.add(packages.getString(i));

        try {
            MainActivity.focusService.startStopwatch(names.toArray(new String[0]));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        call.resolve();
    }

    @PluginMethod
    public void startContinuous(PluginCall call) throws JSONException {
        JSONArray packages = new JSONArray();
        long _duration = 0L;

        try {
            packages = new JSONArray(call.getString("packages"));
            Log.i("T", "startContinuous: " + packages.toString());
            _duration = call.getInt("duration").longValue();
        } catch (NullPointerException e) {
            call.reject("Data provided is empty", e);
            return;
        }

        if (packages == null || packages.length() == 0) {
            call.reject("Data provided is empty");
            return;
        }

        if (_duration <= 0) {
            call.reject("Duration cannot be <0");
            return;
        }

        try {
            MainActivity.connectFocus(getActivity().getApplicationContext());
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        List<String> names = new ArrayList<>();

        for (int i=0; i<packages.length(); i++) {
            // TODO: exception log
            names.add(packages.getString(i));
        }

        try {
            MainActivity.focusService.startContinuous(names.toArray(new String[0]), _duration);
        }
        catch (Exception e) {
            call.reject("Error", e);
        }

    }

    @PluginMethod
    public void startPomodoro(PluginCall call) throws JSONException {
        JSONArray packages = new JSONArray();
        long _work = 0;
        long _break = 0;
        int _cyclesNumber = 0;

        try {
            packages = call.getArray("packages", new JSArray());
            _work = call.getLong("workDuration");
            _break = call.getLong("breakDuration");
            _cyclesNumber = call.getInt("cyclesNumber");
        } catch (NullPointerException e) {
            call.reject("Data provided is empty", e);
        }

        if (packages == null || packages.length() == 0) {
            call.reject("Data provided is empty");
        }

        if (_work <= 0 || _break <= 0) {
            call.reject("Duration cannot be <0");
        }
        if (_cyclesNumber < 1) {
            call.reject("Cycles number cannot be <1");
        }


        MainActivity.connectFocus(getActivity().getApplicationContext());



        List<String> names = new ArrayList<>();

        for (int i=0; i<packages.length(); i++) {
            // TODO: exception log
            names.add(packages.getString(i));
        }

        mService.startPomodoro( names.toArray(new String[0]), _work, _break, _cyclesNumber);

        call.resolve();
    }

    @PluginMethod
    public void stop(PluginCall call) {
        try {
            MainActivity.focusService.stop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        call.resolve();
    }


    private void connect() {
        Intent create = new Intent().setClass(getActivity().getApplicationContext(), FocusService.class);

        getActivity().getApplicationContext().startForegroundService(create);


        Intent bind = new Intent(getContext().getApplicationContext(), FocusService.class);

        getContext().getApplicationContext().bindService(bind, connection, Context.BIND_AUTO_CREATE);
    }





    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            FocusService.LocalBinder binder = (FocusService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}
