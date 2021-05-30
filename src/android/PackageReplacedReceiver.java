package de.kolbasa.apkupdater;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.widget.Toast;

public class PackageReplacedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//             // Check if Android M or higher
//             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                 Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                 cordova.getActivity().startActivity(intent);
//             }

        if (intent.getAction().equals(Intent.ACTION_MY_PACKAGE_REPLACED)) {
            String packageName = context.getPackageName();
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            String mainActivity = launchIntent.getComponent().getClassName();

            Intent activityIntent = new Intent();
            activityIntent.setClassName(context, mainActivity);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            context.startActivity(activityIntent);
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
            Runtime.getRuntime().exit(0);
        }

    }

}