package com.redinn.oceanpeace.icons;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;

import com.redinn.oceanpeace.FunctionBase;
import com.redinn.oceanpeace.database.OceanDatabase;
import com.redinn.oceanpeace.database.icons.Icon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.zhanghai.android.appiconloader.AppIconLoader;

public class IconManager {

    public static void regenerateIcons(Context context) {
        Log.i("IconManager", "regenerating icons...");

        PackageManager packageManager = context.getPackageManager();
        String ICONS_FOLDER_DIRECTORY = FunctionBase.getFilesDir(context) + "/app_icon";
        int REGENERATED_ICONS_COUNTER = 0;

        // https://github.com/zhanghai/AppIconLoader
        final AppIconLoader appIconLoader = new AppIconLoader(128, false, context);

        /* PREPARE FOLDER FOR ICONS */
        File iconFolderFile = new File(ICONS_FOLDER_DIRECTORY);
        if (!iconFolderFile.isDirectory()) {
            Log.i("IconManager", "creating the app_icons directory...");
            if (iconFolderFile.mkdir())
                Log.i("IconManager", "success");
            else {
                Log.e("IconManager", "failed [39:36]");
                Log.e("IconManager", "aborting...");
                return;
            }
        }

        /* RETRIEVE ICONS FROM LOCAL DATABASE */
        Map<String, Icon> iconDB = OceanDatabase.getDatabase(context).iconDAO().getAllIcons();

        /* GET LIST OF INSTALLED APPS
         * including system utilities */
        final List<PackageInfo> installedPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        for (PackageInfo packageInfo : installedPackages) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;

            /* SKIP SYSTEM APPS
             * https://stackoverflow.com/questions/8784505/how-do-i-check-if-an-app-is-a-non-system-app-in-android*/
            if (!appInfo.sourceDir.startsWith("/data/app/") ||
                    packageManager.getLaunchIntentForPackage(appInfo.packageName) == null
                //(appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0
            )
                continue;

            /* COMPARE VERSION OF ICON FROM DATABASE WITH RETRIEVED ONE
             * if equal, skip generation
             * if not equal, proceed to generation process  */
            Icon tempIcon;
            if ((tempIcon = iconDB.get(appInfo.packageName)) != null) {
                if (Objects.equals(tempIcon.version, packageInfo.versionName)) {
                    Log.i("IconManager", "[" + tempIcon.packageName + "]: " + "icon already exists, skipping generation");
                    continue;
                }
            }

            Log.i("IconManager", "[" + appInfo.packageName + "]: " + "icon DOES NOT exists, generating...");

            /* RETRIEVE ICON PROPERTIES */
            String ICON_PATH = ICONS_FOLDER_DIRECTORY + "/" + appInfo.packageName + ".png";
            String LABEL = packageManager.getApplicationLabel(appInfo).toString();
            String VERSION = packageInfo.versionName;

            /* LOG ICON PROPERTIES */
            Log.i("IconManager -> " + appInfo.packageName, "icon path: " + ICON_PATH);
            Log.i("IconManager -> " + appInfo.packageName, "label: " + LABEL);
            Log.i("IconManager -> " + appInfo.packageName, "version: " + VERSION);

            /* CREATE ICON BITMAP AND SAVE IT */
            Bitmap bitmap = appIconLoader.loadIcon(appInfo);
            bitmapDrawableToFile(bitmap, ICON_PATH);


            /* SAVE ICON TO DATABASE */
            Icon newIconRecord = new Icon(appInfo.packageName, LABEL, ICON_PATH, VERSION);
            OceanDatabase.getDatabase(context).iconDAO().insert(newIconRecord);

            REGENERATED_ICONS_COUNTER++;
            Log.i("IconManager", "[" + appInfo.packageName + "]: " + "generation successful");

        } // end of installedPackages loop
        Log.i("IconManager", "successfully regenerated " + REGENERATED_ICONS_COUNTER + " icons");
    } // end of regenerateIcons()


    private static void bitmapDrawableToFile(Bitmap iconBitmap, String ICON_PATH) {
        try {
            FileOutputStream outputStream = new FileOutputStream(ICON_PATH);
            iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("IconManager", "file not found [94:90]", e);
        } catch (IOException e) {
            Log.e("IconManager", "failed to close FileOutputStream [93:89]", e);
        }

    }

}
