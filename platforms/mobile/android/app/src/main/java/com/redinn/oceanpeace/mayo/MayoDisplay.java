package com.redinn.oceanpeace.mayo;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.redinn.oceanpeace.R;

public abstract class MayoDisplay {

    public Runnable closeAppDisplay(Context context) {

        return new Runnable() {
            @Override
            public void run() {
                View testView = LayoutInflater.from(context).inflate(R.layout.popup, null);

                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH +
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE +
                                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        PixelFormat.TRANSLUCENT);
                params.gravity = Gravity.RIGHT | Gravity.TOP;
                params.setTitle("Load Average");


                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
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
                        context.startActivity(startMain);

                        wm.removeView(testView);
                    }
                });

            }// run
        }; // Runnable

    }



}
