package com.redinn.oceanpeace.icons

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import com.redinn.oceanpeace.FunctionBase
import com.redinn.oceanpeace.database.OceanDatabase
import com.redinn.oceanpeace.database.icons.Icon

import me.zhanghai.android.appiconloader.AppIconLoader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


object IconManager {
    fun regenerateIcons(context: Context) {
        Log.i("IconManager", "regenerating icons...")
        val packageManager = context.packageManager
        val ICONS_FOLDER_DIRECTORY = FunctionBase.getFilesDir(context) + "/app_icon"
        var REGENERATED_ICONS_COUNTER = 0

        // https://github.com/zhanghai/AppIconLoader
        val appIconLoader = AppIconLoader(128, false, context)

        /* PREPARE FOLDER FOR ICONS */
        val iconFolderFile = File(ICONS_FOLDER_DIRECTORY)
        if (!iconFolderFile.isDirectory) {
            Log.i("IconManager", "creating the app_icons directory...")
            if (iconFolderFile.mkdir()) Log.i("IconManager", "success") else {
                Log.e("IconManager", "failed [39:36]")
                Log.e("IconManager", "aborting...")
                return
            }
        }

        /* RETRIEVE ICONS FROM THE LOCAL DATABASE */
        val iconDB = OceanDatabase.getDatabase(context).iconDAO().getAllIcons()

        /* GET LIST OF INSTALLED APPS
         * including system utilities */
        val installedPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        for (packageInfo in installedPackages) {
            val appInfo = packageInfo.applicationInfo

            /* SKIP SYSTEM APPS
             * https://stackoverflow.com/questions/8784505/how-do-i-check-if-an-app-is-a-non-system-app-in-android*/
            if (!appInfo.sourceDir.startsWith("/data/app/") ||
                packageManager.getLaunchIntentForPackage(appInfo.packageName) == null //(appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0
            ) continue

            /* COMPARE VERSION OF ICON FROM DATABASE WITH RETRIEVED ONE
             * if equal, skip generation
             * if not equal, proceed to generation process  */
            val tempIcon = iconDB.get(appInfo.packageName)
            if (tempIcon != null) {
                if (tempIcon.version == packageInfo.versionName) {
                    Log.i(
                        "IconManager",
                        "[" + tempIcon.packageName + "]: " + "icon already exists, skipping generation"
                    )
                    continue
                }
            }
            Log.i(
                "IconManager",
                "[" + appInfo.packageName + "]: " + "icon DOES NOT exists, generating..."
            )

            /* RETRIEVE ICON PROPERTIES */
            val ICON_PATH = ICONS_FOLDER_DIRECTORY + "/" + appInfo.packageName + ".png"
            val LABEL = packageManager.getApplicationLabel(appInfo).toString()
            val VERSION = packageInfo.versionName

            /* LOG ICON PROPERTIES */Log.i(
                "IconManager -> " + appInfo.packageName,
                "icon path: $ICON_PATH"
            )
            Log.i("IconManager -> " + appInfo.packageName, "label: $LABEL")
            Log.i("IconManager -> " + appInfo.packageName, "version: $VERSION")

            /* CREATE ICON BITMAP AND SAVE IT */
            val bitmap = appIconLoader.loadIcon(appInfo)
            bitmapDrawableToFile(bitmap, ICON_PATH)


            /* SAVE ICON TO DATABASE */
            val newIconRecord = Icon(appInfo.packageName, LABEL, ICON_PATH, VERSION)
            OceanDatabase.getDatabase(context).iconDAO().insert(newIconRecord)
            REGENERATED_ICONS_COUNTER++
            Log.i("IconManager", "[" + appInfo.packageName + "]: " + "generation successful")
        } // end of installedPackages loop
        Log.i("IconManager", "successfully regenerated $REGENERATED_ICONS_COUNTER icons")
    } // end of regenerateIcons()

    private fun bitmapDrawableToFile(iconBitmap: Bitmap, ICON_PATH: String) {
        try {
            val outputStream = FileOutputStream(ICON_PATH)
            iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
        } catch (e: FileNotFoundException) {
            Log.e("IconManager", "file not found [94:90]", e)
        } catch (e: IOException) {
            Log.e("IconManager", "failed to close FileOutputStream [93:89]", e)
        }
    }

    fun getAppName(context: Context, packageName: String): String {
        return try {
            val pkgName =
                OceanDatabase.getDatabase(context).iconDAO().getIcon(packageName).packageName
            pkgName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("getAppName", "fail ($packageName) ${e.message} - ${e.localizedMessage}")
            packageName
        }
    }
}
