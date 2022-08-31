package com.oceanpeace.redinn;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Date;

public class TestAccessibilityService extends AccessibilityService {

    private String lastPackageName = null;
    private long changeTime;

    @Override
    public void onServiceConnected() {
//        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
//        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
//        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
//        info.flags = AccessibilityServiceInfo.DEFAULT;
//        info.notificationTimeout = 100;
//        this.setServiceInfo(info);;
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo == null)
            return;

        if (event.getPackageName() != lastPackageName) {
            long curTime = event.getEventTime();

        }
        
        Log.i("WINDOW",
                "\nonAccessibilityEvent: "  + event.getPackageName() +
                        "\nevent: " + event.toString() +
                        "\nnode_info: " + nodeInfo.toString() +
                        "\nclass_name: " + event.getClassName() +
                        "\ndate: " + new Date(event.getEventTime()).toString()
        );

        
    }

    @Override
    public void onInterrupt() {
    }

}
