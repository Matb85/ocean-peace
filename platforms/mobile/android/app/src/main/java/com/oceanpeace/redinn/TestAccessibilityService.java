package com.oceanpeace.redinn;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class TestAccessibilityService extends AccessibilityService {

    private String lastPN = null;

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
        
        Log.i("WINDOW",
                "\nonAccessibilityEvent: "  + event.getPackageName() +
                        "\nevent: " + event.toString() +
                        "\nnode_info: " + nodeInfo.toString()
        );

        if (nodeInfo.getChildCount() > 0)
        {

            for (int i=0; i<nodeInfo.getChildCount(); i++) {
                String childs ="";
                AccessibilityNodeInfo child = nodeInfo.getChild(i);
                childs = childs + child.toString() + "\n" + nodeInfo.getChildCount() + "\n";
//                if (child.getChildCount() > 0)
//                    for (int j=0; j < child.getChildCount(); j++) {
//                        AccessibilityNodeInfo child2 = child.getChild(i);
//                        childs = childs + child2.toString() + "\n";
//                    }
                Log.i("CHILD", childs);
            }

        }

        
    }

    @Override
    public void onInterrupt() {
    }

}
