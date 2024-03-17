package com.redinn.oceanpeace

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.getcapacitor.BridgeActivity
import com.redinn.oceanpeace.focus.FocusPlugin
import com.redinn.oceanpeace.goals.GoalsPlugin
import com.redinn.oceanpeace.helper.PermissionHelper
import com.redinn.oceanpeace.icons.IconManager
import com.redinn.oceanpeace.icons.IconsPlugin
import com.redinn.oceanpeace.managers.PermissionManager
import com.redinn.oceanpeace.presets.PresetsPlugin
import com.redinn.oceanpeace.schedule.SchedulePlugin
import com.redinn.oceanpeace.usage.UsagePlugin

class MainActivity : BridgeActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        //region Plugin Registration
        registerPlugin(UsagePlugin::class.java)
        registerPlugin(FocusPlugin::class.java)
        registerPlugin(GoalsPlugin::class.java)
        registerPlugin(IconsPlugin::class.java)
        registerPlugin(PresetsPlugin::class.java)
        registerPlugin(SchedulePlugin::class.java)
        registerPlugin(UIPlugin::class.java)
        registerPlugin(PermissionManager::class.java)

        val isEnabled = PermissionHelper.isAccessibilitySettingsOn(this)
        if (!isEnabled) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }

        //endregion
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        Thread { IconManager.regenerateIcons(applicationContext) }.start()
        super.onStart()
    }


}