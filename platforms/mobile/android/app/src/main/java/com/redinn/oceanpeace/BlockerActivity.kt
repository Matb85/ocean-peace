package com.redinn.oceanpeace

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.redinn.oceanpeace.helper.LP
import com.redinn.oceanpeace.helper.ResourceHelper.dp

class BlockerActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = FrameLayout(this)
        layout.setBackgroundColor(Color.WHITE)
        setContentView(layout)
        setup(layout)
    }

    private fun setup(layout: FrameLayout) {

        val img = ImageView(this).apply {
            setImageResource(R.drawable.ic_launcher_foreground)
        }
        val textView = TextView(this).apply {
            text = "Your usage exceeds limit.\n Why not take a break?"
            gravity = Gravity.CENTER_HORIZONTAL
        }
        val closeStrictModeText = TextView(this)

        layout.apply {
            setBackgroundColor(Color.parseColor("#AAAAAAAA"))

            addView(closeStrictModeText, LP.frame(LP.WRAP_CONTENT, LP.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL)
                    .setMargins(dp(20), dp(20), dp(20), 0)
                    .build())
            addView(img, LP.frame(dp(200), dp(200), Gravity.CENTER).build())
            addView(textView, LP.frame(LP.MATCH_PARENT, LP.WRAP_CONTENT, Gravity.BOTTOM)
                    .setMargins(dp(20), 0, dp(20), dp(60))
                    .build())
        }
    }

    override fun onBackPressed() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }
}
