package com.autoai.readnotification.services;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
 import android.text.TextUtils;
import android.util.Log;

import com.autoai.readnotification.BuildConfig;
import com.autoai.readnotification.NotificationContentUtils;
import com.autoai.readnotification.NotificationListenerUtils;
import com.autoai.readnotification.NotificationUtils;
import com.autoai.readnotification.VersionUtils;
import com.autoai.readnotification.models.PendingNotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import androidx.core.app.NotificationCompat;


@SuppressLint("NewApi")
public abstract class BaseNotificationListener extends NotificationListenerService {

    protected final String TAG = getClass().getSimpleName();

    private ArrayList<PendingNotification> pending = new ArrayList<>();
    private ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    private List<Integer> notifHandled = new ArrayList<>();
    private Map<Integer, Integer> previouslyDismissed = new HashMap<>();

    private ArrayList<String> duplicateReplyPackages = new ArrayList<>();
    private String lastRemovedKey = "";

    protected void onEnabled(boolean enabled) {
        NotificationListenerUtils.setListenerConnected(this, enabled);
        Log.d(TAG, "Listener enabled: " + enabled + "..");
    }

    private void initDupeReplyList() {
//        duplicateReplyPackages.add("com.google.android.talk3");
//        duplicateReplyPackages.add("com.google.android.apps.messaging");
        duplicateReplyPackages.add("com.whatsapp");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Listener created..");
        initDupeReplyList();
        onEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Listener destroyed..");
        onEnabled(false);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "Listener connected..");
        onEnabled(true);
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.d(TAG, "Listener disconnected..");
        onEnabled(false);
    }

    @Override
    public IBinder onBind(Intent mIntent) {
        IBinder mIBinder = super.onBind(mIntent);
        onEnabled(true);
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent mIntent) {
        boolean mOnUnbind = super.onUnbind(mIntent);
        onEnabled(false);
        return mOnUnbind;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        if(shouldBeIgnored(sbn)) {
            if(BuildConfig.DEBUG) {
                Bundle extras = NotificationCompat.getExtras(sbn.getNotification());
                String title = NotificationUtils.getTitle(extras);
                String msg = NotificationUtils.getMessage(extras);
                Log.d(TAG, "Ignoring potential duplicate from " + sbn.getPackageName() + ":\n" + title + "\n" + msg);
            }
            return;
        }

        if(shouldAppBeAnnounced(sbn, rankingMap))
            handleSbn(sbn);
    }

    private boolean shouldBeIgnored(StatusBarNotification sbn) {
        if(!duplicateReplyPackages.contains(sbn.getPackageName()))
            return false;
        int hashCode = getHashCode(sbn);
        return notifHandled.indexOf(hashCode) > -1 || previouslyDismissed.containsValue(hashCode);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if(shouldBeIgnored(sbn))
            return;

        if (shouldAppBeAnnounced(sbn)) {
            handleSbn(sbn);
        }
    }

    private int getHashCode(StatusBarNotification sbn) {
        Bundle extras = NotificationCompat.getExtras(sbn.getNotification());
        String title = NotificationUtils.getTitle(extras);
        String msg = NotificationUtils.getMessage(extras);
        return (title + msg + sbn.getPackageName()).hashCode();
    }

    private void handleSbn(StatusBarNotification sbn) {
        if(!lastRemovedKey.equals(getKey(sbn)))
            postDelayed(sbn);
        lastRemovedKey = "";
    }

    private void postDelayed(StatusBarNotification sbn) {
        PendingNotification pn = new PendingNotification(sbn);
        int index = pending.indexOf(pn);
        if (index >= 0) {
            boolean remove = false;
            try {
                if (NotificationCompat.isGroupSummary(sbn.getNotification())) {
                    pending.get(index).setDismissKey(pn.getDismissKey());
                    return;
                } else if ((index = pending.indexOf(pn)) >= 0 && NotificationCompat.isGroupSummary(pending.get(index).getSbn().getNotification())) { //Fix for bug where by now the list could be empty so we need to re-evaluate the index
                    pn.setDismissKey(pending.get(index).getDismissKey());
                    remove = true;
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            if (remove && pending.size() > index) {
                pending.get(index).getScheduledFuture().cancel(false);
                pending.remove(index);
            }
        }

        Runnable task = new Runnable() {
            public void run() {
                if (pending.size() > 0) {
                    PendingNotification pn = pending.get(0);
                    pending.remove(0);
                    if(duplicateReplyPackages.contains(pn.getSbn().getPackageName()))
                        notifHandled.add(getHashCode(pn.getSbn()));
                    onNotificationPosted(pn.getSbn(), pn.getDismissKey());
                }
            }
        };
        ScheduledFuture<?> scheduledFuture = worker.schedule(task, 200, TimeUnit.MILLISECONDS);
        pn.setScheduledFuture(scheduledFuture);
        pending.add(pn);
    }

    @Override
    public void onNotificationRemoved(final StatusBarNotification sbn) {
        if(sbn != null && duplicateReplyPackages.contains(sbn.getPackageName())) {
            int hashCode = getHashCode(sbn);
            int indexOf = notifHandled.indexOf(hashCode);
            if (indexOf > -1) {
                notifHandled.remove(indexOf);
                Bundle extras = NotificationCompat.getExtras(sbn.getNotification());
                String title = NotificationContentUtils.getTitle(extras);
                if(TextUtils.isEmpty(title))
                    return;
                int titleHashcode = title.hashCode();
                if(previouslyDismissed.containsKey(titleHashcode))
                    previouslyDismissed.put(titleHashcode, hashCode);
                else {
                    if (previouslyDismissed.size() >= 5)
                        previouslyDismissed.remove(previouslyDismissed.size() - 1);
                    previouslyDismissed.put(titleHashcode, hashCode);
                }
            }
        }
    }

    private String getKey(StatusBarNotification sbn) {
        return VersionUtils.isLollipop() ? sbn.getKey() : String.valueOf(sbn.getId());
    }

    protected boolean shouldAppBeAnnounced(StatusBarNotification sbn, RankingMap rankingMap) {
        return shouldAppBeAnnounced(sbn);
    }
    protected abstract boolean shouldAppBeAnnounced(StatusBarNotification sbn);
    protected abstract void onNotificationPosted(StatusBarNotification sbn, String dismissKey);

}