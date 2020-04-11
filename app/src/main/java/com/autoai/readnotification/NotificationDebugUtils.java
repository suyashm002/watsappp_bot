package com.autoai.readnotification;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
 import android.util.Log;

import androidx.core.app.NotificationCompat;

/**
 * Created by jj on 03/09/16.
 */
public class NotificationDebugUtils {

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void printNotification(StatusBarNotification sbn) {
        final String TAG = NotificationDebugUtils.class.getSimpleName();
        Log.d(TAG, getTitle(sbn.getNotification()) + ", " +
                getMessageContent(sbn.getNotification()) + ", " +
                "Sbn id: " + sbn.getId() + ", " +
                "Sbn tag: " + sbn.getTag() + ", " +
                "Sbn key: " + (Build.VERSION.SDK_INT >= 20 ? sbn.getKey() : "Unknown") + ", " +
                "Post time: " + sbn.getPostTime() + ", " +
                "When time: " + sbn.getNotification().when + ", ");
    }

    public static String getTitle(Notification n) {
        Bundle bundle = NotificationCompat.getExtras(n);
        return bundle.getString(NotificationCompat.EXTRA_TITLE);
    }

    public static String getMessageContent(Notification n) {
        Bundle bundle = NotificationCompat.getExtras(n);
        return bundle.getString(NotificationCompat.EXTRA_TEXT);
    }

}
