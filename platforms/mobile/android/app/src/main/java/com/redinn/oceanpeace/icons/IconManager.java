package com.redinn.oceanpeace.icons;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.redinn.oceanpeace.FunctionBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import me.zhanghai.android.appiconloader.AppIconLoader;

public class IconManager {

    public static Properties getIconsData(Context ctx) {
        Properties iconDB = new Properties();
        try {
            FileInputStream iconDBFile = ctx.openFileInput("iconDB.properties");
            iconDB.load(iconDBFile);
        } catch (IOException e) {
            Log.e("IconManager", "could not open the iconDB file " + e);
        }
        return iconDB;
    }

    public static void regenerateIcons(Context ctx) {
        Log.i("IconManager", "regenerating icons...");
        /* initial setup */
        PackageManager pm = ctx.getPackageManager();
        String ICONS_FOLDER = FunctionBase.getFilesDir(ctx) + "/app_icons";
        // https://github.com/zhanghai/AppIconLoader
        final AppIconLoader loader= new AppIconLoader(128, false, ctx);

        /* prepare a folder for the icons */
        File iconFolderFile = new File(ICONS_FOLDER);
        if (!iconFolderFile.isDirectory()) {
            Log.i("IconManager", "creating the app_icons folder");
            iconFolderFile.mkdir();
        }
        /* create a new DB from the ground up to prevent persisting the data of uninstalled apps */
        Properties existingIconDB = IconManager.getIconsData(ctx);
        Properties newIconDB = new Properties();

        /* get a list of ALL the installed apps - including system utilities.
         * https://developer.android.com/reference/android/content/pm/ApplicationInfo
         * */
        final List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_META_DATA);
        for (PackageInfo packageInfo : packages) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            /* filter out unwanted system utilities
            * https://stackoverflow.com/questions/8784505/how-do-i-check-if-an-app-is-a-non-system-app-in-android */
            if (!appInfo.sourceDir.startsWith("/data/app/") || pm.getLaunchIntentForPackage(appInfo.packageName) == null)
                continue;
            /* compare the version of the app from iconDB with the retrieved one
             * if they are equal, skip generating
             * if not, generate the icon  */
            String existingIconData = existingIconDB.getProperty(appInfo.packageName);
            if (existingIconData != null) {
                AppIconData parsedIconData = new AppIconData(appInfo.packageName, existingIconData);
                if (Objects.equals(parsedIconData.version, packageInfo.versionName)) {
                    Log.i("IconManager", "icon for " + appInfo.packageName + " already exists, skipping generation");
                    /* before skipping rewrite the entry from the outdated DB to the new one for data compliance */
                    newIconDB.setProperty(appInfo.packageName, parsedIconData.stringifyData());
                    existingIconDB.remove(appInfo.packageName);
                    continue;
                }
            }
            Log.i("IconManager", "icon for " + appInfo.packageName + " DOES NOT exists, generating");

            /* generate a file path for each icon */
            String iconPath = ICONS_FOLDER + "/" + appInfo.packageName + ".png";
            /* log stuff */
            Log.i("IconManager-" + appInfo.packageName, "name: " + pm.getApplicationLabel(appInfo));
            Log.i("IconManager-" + appInfo.packageName, "icon path: " + iconPath);
            Bitmap bitmap = loader.loadIcon(appInfo);
            bitmapDrawableToFile(bitmap, iconPath);

            /* push the data to the main properties object
             * the data is stored in an array, values are separated with ';' characters */
            final String data = pm.getApplicationLabel(appInfo) + ";" + iconPath + ";" + packageInfo.versionName;
            newIconDB.setProperty(appInfo.packageName, data);
        }

        try {
            FileOutputStream iconDBFile = ctx.openFileOutput("iconDB.properties", Context.MODE_PRIVATE);
            Log.d("IconManager", "saving icon data to iconDB.properties in " + ctx.getFileStreamPath("iconDB.properties").getAbsolutePath());
            newIconDB.store(iconDBFile, "");
        } catch (IOException e) {
            Log.e("IconManager", "could not open the iconDB file" + e);
        }
    }

    // http://www.carbonrider.com/2016/01/01/extract-app-icon-in-android/
    private static void bitmapDrawableToFile(Bitmap icon, String iconPath) {
        try {
            /* try to save the icon */
            FileOutputStream out = new FileOutputStream(iconPath);
            icon.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {
            Log.e("IconPlugin", "failed to close FileOutputStream " + e);
        }
    }

    public static AppIconData getIcon(String packageName, Context context) throws Exception {
        if (packageName == null)
            throw new Exception("getIcon: packageName is null");

        Properties iconDB = IconManager.getIconsData(context);
        String data = iconDB.getProperty(packageName);
        if (data == null) {
            throw new Exception("getIcon: No data for package: " + packageName);
        }

        return new AppIconData(packageName, data);
    }

}
