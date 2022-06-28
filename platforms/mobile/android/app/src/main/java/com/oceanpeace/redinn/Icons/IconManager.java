package com.oceanpeace.redinn.Icons;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import me.zhanghai.android.appiconloader.AppIconLoader;

public class IconManager {
    final private Context ctx;
    final private PackageManager pm;
    // https://github.com/zhanghai/AppIconLoader
    final private AppIconLoader loader;
    final public String ICONS_FOLDER;
    final public String APP_DATA_FOLDER;


    public IconManager(Context ctx) {
        this.ctx = ctx;
        this.pm = ctx.getPackageManager();
        this.loader = new AppIconLoader(128, false, ctx);
        this.APP_DATA_FOLDER = ctx.getDataDir().getAbsolutePath();
        this.ICONS_FOLDER = this.APP_DATA_FOLDER + "/app_icons";
    }

    public Properties getIconsData() {
        Properties iconDB = new Properties();
        try {
            FileInputStream iconDBFile = ctx.openFileInput("iconDB.properties");
            iconDB.load(iconDBFile);
            for (Object key : iconDB.keySet()) {
                Log.d("IconManager", "iconDBFile: " + key + ": " + iconDB.getProperty(key.toString()));
            }
        } catch (IOException e) {
            Log.e("IconManager", "could not open the iconDB file" + e);
        }
        return iconDB;
    }

    public void regenerateIcons() {
        Log.e("IconManager", "regenerating icons...");

        /* prepare a folder for the icons */
        File iconFolderFile = new File(ICONS_FOLDER);
        if (!iconFolderFile.isDirectory()) {
            Log.e("IconManager", "creating the app_icons folder");
            iconFolderFile.mkdir();
        }
        Properties iconDB = new Properties();

        /* get a list of ALL the installed apps - including system utilities.
         * https://developer.android.com/reference/android/content/pm/ApplicationInfo
         * */
        final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            /* filter out unwanted system utils */
            if (!packageInfo.sourceDir.startsWith("/data/app/") || pm.getLaunchIntentForPackage(packageInfo.packageName) == null)
                continue;

            /* generate a file path for each icon */
            String iconPath = ICONS_FOLDER + "/" + packageInfo.packageName + ".png";
            /* log stuff */
            Log.d("IconManager-" + packageInfo.packageName, "name: " + pm.getApplicationLabel(packageInfo));
            Log.d("IconManager-" + packageInfo.packageName, "icon path: " + iconPath);
            File f = new File(iconPath);
            if (!f.isFile()) {
                Log.d("IconManager-" + packageInfo.packageName, "icon does not exist - generating a new one: " + iconPath);
                Bitmap bitmap = loader.loadIcon(packageInfo);
                bitmapDrawableToFile(bitmap, iconPath);
            }
            /* push the data to the main properties object
            * the data is stored in an array, values are separated with ';' characters */
            final String data = pm.getApplicationLabel(packageInfo) + ";" + iconPath + ";" + packageInfo.sourceDir;
            iconDB.setProperty(packageInfo.packageName, data);
        }

        try {
            FileOutputStream iconDBFile = ctx.openFileOutput("iconDB.properties", Context.MODE_PRIVATE);
            Log.d("IconManager", "saving icon data to iconDB.properties in "+ctx.getFileStreamPath("iconDB.properties").getAbsolutePath());
            iconDB.store(iconDBFile, "");
        } catch (IOException e) {
            Log.e("IconManager", "could not open the iconDB file" + e);
        }
    }

    // http://www.carbonrider.com/2016/01/01/extract-app-icon-in-android/
    private void bitmapDrawableToFile(Bitmap icon, String iconPath) {
        /* try to save the icon */
        try {
            FileOutputStream out = new FileOutputStream(iconPath);
            icon.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {
            Log.e("IconPlugin", "failed to close FileOutputStream " + e);
        }
    }
}
