package com.redinn.oceanpeace

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.getcapacitor.BridgeActivity
import com.redinn.oceanpeace.focus.FocusPlugin
import com.redinn.oceanpeace.goals.GoalsPlugin
import com.redinn.oceanpeace.helper.NotTrackingListHelper
import com.redinn.oceanpeace.helper.NotificationHelper
import com.redinn.oceanpeace.icons.IconManager
import com.redinn.oceanpeace.icons.IconsPlugin
import com.redinn.oceanpeace.managers.PermissionManager
import com.redinn.oceanpeace.presets.PresetsPlugin
import com.redinn.oceanpeace.schedule.SchedulePlugin
import com.redinn.oceanpeace.usage.UsagePlugin
import io.reactivex.disposables.CompositeDisposable

class MainActivity : BridgeActivity() {
    private val mSubscriptions = CompositeDisposable()
    private var mStoreRetained: Boolean = false
    private var mBinder: MyService.MyBinder? = null

    private val con = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            mBinder = p1 as? MyService.MyBinder
        }
    }

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
        //endregion

        if (!mStoreRetained) {
            mStoreRetained = true
            MainApplication.retainStore(this)
        }
        startService(Intent(this, MyService::class.java))

        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        Thread { IconManager.regenerateIcons(applicationContext) }.start()
        NotificationHelper.createChannel(applicationContext)

        // NOTE: We need to get store ready before super.onStart(),
        //       otherwise Conductor will re-create our view and cause NPE upon using store
        if (!mStoreRetained) {
            mStoreRetained = true
            MainApplication.retainStore(this)
        }
        val l = NotTrackingListHelper.loadNotTrackingList(this)

        bindService(Intent(this, MyService::class.java), con, 0)

        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        mSubscriptions.clear()
        try {
            unbindService(con)
        } catch (e: Exception) {
            Log.e("MainActivity", e.toString())
        }
    }

    override fun onStop() {
        super.onStop()
        if (mStoreRetained) {
            mStoreRetained = false
            MainApplication.releaseStore(this)
        }
    }

//    override fun onDestroy() {
//       super.onDestroy()
//    }

}