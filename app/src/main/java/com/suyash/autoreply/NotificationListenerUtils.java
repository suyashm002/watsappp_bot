package com.suyash.autoreply;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.app.Fragment;

/**
 * Created by jj on 06/10/16.
 */

public class NotificationListenerUtils {

    public static final int REQ_NOTIFICATION_LISTENER = 123;
    private final static String LISTENER_SERVICE_CONNECTED = "LISTENER_SERVICE_CONNECTED";
    private static final String TAG = "NotificationListener";

    //TODO: Multi process prefs was depreciated as of 23, no longer works

    public static void setListenerConnected(Context context, boolean listenerConnected) {
        SharedPreferences.Editor editor = context.getSharedPreferences(TAG, Context.MODE_MULTI_PROCESS).edit();
        editor.putBoolean(LISTENER_SERVICE_CONNECTED, listenerConnected);
        editor.commit();
    }

    public static boolean isListenerConnected(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_MULTI_PROCESS);
        return sp.getBoolean(LISTENER_SERVICE_CONNECTED, false);
    }

    //https://stackoverflow.com/questions/20141727/check-if-user-has-granted-notificationlistener-access-to-my-app/28160115
    //TODO: Use in UI to verify if it needs enabling or restarting
    public static boolean isListenerEnabled(Context context, Class notificationListenerCls) {
        ComponentName cn = new ComponentName(context, notificationListenerCls);
        String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        return flat != null && flat.contains(cn.flattenToString());
    }

    public static void launchNotificationAccessSettings(Activity activity) {
        Intent i = new Intent(VersionUtils.isJellyBeanMR2()
                ? "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
                : Settings.ACTION_ACCESSIBILITY_SETTINGS);
        activity.startActivityForResult(i, REQ_NOTIFICATION_LISTENER);
    }

    public static void launchNotificationAccessSettings(Fragment fragment) {
        Intent i = new Intent(VersionUtils.isJellyBeanMR2()
                ? "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
                : Settings.ACTION_ACCESSIBILITY_SETTINGS);
        fragment.startActivityForResult(i, REQ_NOTIFICATION_LISTENER);
    }

}
