package com.oceanpeace.redinn.Icons;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.oceanpeace.redinn.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import me.zhanghai.android.appiconloader.AppIconLoader;

@CapacitorPlugin(name = "Icons")
public class IconsPlugin extends Plugin {
    /**
     * get the package manager
     */
    private PackageManager getPM() {
        return MainActivity.getAppContext().getPackageManager();
    }

    private String getAppDataDir() {
        PackageManager pm = getPM();
        String s = MainActivity.getAppContext().getPackageName();
        try {
            ApplicationInfo info = pm.getApplicationInfo(s, PackageManager.GET_META_DATA);
            s = info.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("Package", "Error Package name not found ", e);
            return null;
        }
        return s;
    }

    @PluginMethod
    public void getAllIcons(PluginCall call) {
        /* note: one cannot access actual files! */
        JSObject ret = new JSObject();
        JSONArray responseArray = new JSONArray();
        PackageManager pm = getPM();
        // https://github.com/zhanghai/AppIconLoader
        AppIconLoader loader = new AppIconLoader(128,false,MainActivity.getAppContext());

        /* prepare a folder for the icons */
        String iconFolder = getAppDataDir() + "/app_icons";
        File iconFolderFile = new File(iconFolder);
        if (iconFolderFile.isDirectory()) {
            Log.e("IconsPlugin", "deleting the app_icons folder");
            iconFolderFile.delete();
        }

        iconFolderFile.mkdir();
        /* get a list of ALL the installed apps - including system utilities. */
        final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            /* filter out unwanted system utils */
            if (!packageInfo.sourceDir.startsWith("/data/app/") || pm.getLaunchIntentForPackage(packageInfo.packageName) == null)
                continue;

            /* generate a file path for each icon */
            String iconPath = iconFolder + "/" + packageInfo.packageName + ".png";
            /* log stuff */
            Log.d("IconsPlugin-" + packageInfo.packageName, "name: " + pm.getApplicationLabel(packageInfo));
            Log.d("IconsPlugin-" + packageInfo.packageName, "icon path: " + iconPath);
            File f = new File(iconPath);
            if (!f.isFile()) {
                Log.d("IconsPlugin-" + packageInfo.packageName, "icon does not exist - generating a new one: " + iconPath);
                Bitmap bitmap = loader.loadIcon(packageInfo);
                bitmapDrawableToFile(bitmap, iconPath);
            }
            /* push the data to the main object that is returned */
            JSONObject appIcon = new JSONObject();
            try {
                appIcon.put("name", pm.getApplicationLabel(packageInfo));
                appIcon.put("src", iconPath);
            } catch (JSONException err) {
                Log.e("IconsPlugin-" + packageInfo.packageName, err.toString());
            }
            responseArray.put(appIcon);
        }

        ret.put("apps", responseArray);

        call.resolve(ret);
    }

    // http://www.carbonrider.com/2016/01/01/extract-app-icon-in-android/
    private static void bitmapDrawableToFile(Bitmap icon, String iconPath) {
        /* try to save the icon */
        FileOutputStream out;
        try {
            out = new FileOutputStream(iconPath);
            icon.compress(Bitmap.CompressFormat.PNG, 100, out);
            try {
                out.close();
            } catch (Throwable ignore) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
