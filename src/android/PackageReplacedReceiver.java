package de.kolbasa.apkupdater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PackageReplacedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String packageName = context.getPackageName();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        String mainActivity = launchIntent.getComponent().getClassName();

        Intent activityIntent = new Intent();
        activityIntent.setClassName(context, String.format("%s.%s", packageName, mainActivity));
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(activityIntent);
    }

}