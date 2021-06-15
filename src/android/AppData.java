package de.kolbasa.apkupdater;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class AppData {

    private final Activity activity;

    public AppData(Activity activity) {
        this.activity = activity;
    }

    public String getAppName() {
        PackageManager pm = this.activity.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(this.activity.getPackageName(), 0);
            return (String) pm.getApplicationLabel(appInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAppVersion() {
        PackageManager pm = this.activity.getPackageManager();
        try {
            return pm.getPackageInfo(this.activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getAppVersionCode() {
        PackageManager pm = this.activity.getPackageManager();
        try {
            return pm.getPackageInfo(this.activity.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
