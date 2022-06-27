package com.oceanpeace.redinn.Icons;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.getcapacitor.App;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.oceanpeace.redinn.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

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
        PackageManager pm = getPM();
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
            if (packageInfo.category == ApplicationInfo.CATEGORY_UNDEFINED || !packageInfo.sourceDir.startsWith("/data/app/") || pm.getLaunchIntentForPackage(packageInfo.packageName) == null)
                continue;
            /* create a png icon for each app
             * source: https://stackoverflow.com/questions/24829556/programatically-get-android-package-icon-as-png-or-bmp */
            Drawable icon;
            icon = packageInfo.loadIcon(pm);

            Log.d("IconsPlugin-" + packageInfo.packageName, "flags:" + packageInfo.flags);

            Log.d("IconsPlugin-" + packageInfo.packageName, "icon class: " + icon.getClass());

            /* generate a file path for each icon */
            String iconPath = iconFolder + "/" + packageInfo.packageName + ".png";
            Log.d("IconsPlugin-" + packageInfo.packageName, "icon path: " + iconPath);
            bitmapDrawableToFile(icon, iconPath);

            /* log stuff */
            Log.d("IconsPlugin-" + packageInfo.packageName, "package: " + packageInfo.packageName);
            Log.d("IconsPlugin-" + packageInfo.packageName, "category: " + packageInfo.category);
            Log.d("IconsPlugin-" + packageInfo.packageName, "name: " + pm.getApplicationLabel(packageInfo));

            /* push the data to the main object that is returned */
            JSONObject appIcon = new JSONObject();
            try {
                appIcon.put("name", pm.getApplicationLabel(packageInfo));
                appIcon.put("src", iconPath);
            } catch (JSONException err) {
                Log.e("IconsPlugin-" + packageInfo.packageName, err.toString());
            }
            ret.put(packageInfo.packageName, appIcon);
        }

        call.resolve(ret);
    }

    // http://www.carbonrider.com/2016/01/01/extract-app-icon-in-android/
    private static void bitmapDrawableToFile(Drawable icon, String iconPath) {
        /* try to save the icon */
        FileOutputStream out;
        try {
            out = new FileOutputStream(iconPath);
            getBitmapFromDrawable(icon).compress(Bitmap.CompressFormat.PNG, 100, out);
            try {
                out.close();
            } catch (Throwable ignore) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File f = new File(iconPath);
        if (f.isFile()) {
            Log.d("Paths", "PATH EXISTS");
            Log.d("Files", "Size: " + f.getPath());
        } else {
            Log.d("Paths", "PATH DOES NOT EXIST");
        }
    }
    // https://stackoverflow.com/questions/44447056/convert-adaptiveicondrawable-to-bitmap-in-android-o-preview
    private static Bitmap getBitmapFromDrawable(Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }
    private static Bitmap bitmapDrawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            return Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
    }
}

/**
 public static Bitmap bitmapDrawableToBitmap (Drawable drawable) {
  if (drawable instanceof BitmapDrawable) {
  BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
  if (bitmapDrawable.getBitmap() != null) {
  return bitmapDrawable.getBitmap();
  }
  }
  if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
  return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
  } else {
  return Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
  }
  }
 * public static void saveImage(String imageUrl, String destinationFile) throws IOException {
 * URL url = new URL(imageUrl);
 * InputStream is = url.openStream();
 * FileOutputStream os = new FileOutputStream(destinationFile);
 * <p>
 * byte[] b = new byte[2048];
 * int length;
 * <p>
 * while ((length = is.read(b)) != -1) {
 * os.write(b, 0, length);
 * }
 * <p>
 * is.close();
 * os.close();
 * }
 */