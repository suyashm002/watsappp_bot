package com.suyash.autoreply;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.suyash.autoreply.models.NotificationIds;

public class NotificationContentUtils {

    private static final String TAG = NotificationContentUtils.class.getSimpleName();

    @TargetApi(19)
    public static String getTitle(Bundle extras) {
        Log.d(TAG, "Getting title from extras..");
        String msg = extras.getString("android.title");
        Log.d("Title Big", "" + extras.getString("android.title.big"));
        return msg;
    }

    public static String getTitle(ViewGroup localView) {
        Log.d(TAG, "Getting title..");
        String msg = null;
        TextView tv = (TextView)localView.findViewById(NotificationIds.getInstance(localView.getContext()).TITLE);
        if(tv != null) {
            msg = tv.getText().toString();
        }

        return msg;
    }

    @TargetApi(19)
    public static String getMessage(Bundle extras) {
        Log.d(TAG, "Getting message from extras..");
        Log.d("Text", "" + extras.getCharSequence("android.text"));
        Log.d("Big Text", "" + extras.getCharSequence("android.bigText"));
        Log.d("Title Big", "" + extras.getCharSequence("android.title.big"));
        Log.d("Info text", "" + extras.getCharSequence("android.infoText"));
        Log.d("Info text", "" + extras.getCharSequence("android.infoText"));
        Log.d("Subtext", "" + extras.getCharSequence("android.subText"));
        Log.d("Summary", "" + extras.getString("android.summaryText"));
        CharSequence chars = extras.getCharSequence("android.text");
        String chars1;
        return !TextUtils.isEmpty(chars)?chars.toString():(!TextUtils.isEmpty(chars1 = extras.getString("android.summaryText"))?chars1.toString():null);
    }

    public static String getMessage(ViewGroup localView) {
        Log.d(TAG, "Getting message..");
        String msg = null;
        TextView tv = (TextView)localView.findViewById(NotificationIds.getInstance(localView.getContext()).BIG_TEXT);
        if(tv != null && !TextUtils.isEmpty(tv.getText())) {
            msg = tv.getText().toString().toLowerCase();
        }

        if(TextUtils.isEmpty(msg)) {
            tv = (TextView)localView.findViewById(NotificationIds.getInstance(localView.getContext()).TEXT);
            if(tv != null) {
                msg = tv.getText().toString().toLowerCase();
            }
        }

        return msg;
    }

    @TargetApi(19)
    public static String getExtended(Bundle extras, ViewGroup v) {
        Log.d(TAG, "Getting message from extras..");
        CharSequence[] lines = extras.getCharSequenceArray("android.textLines");
        if(lines != null && lines.length > 0) {
            StringBuilder var8 = new StringBuilder();
            CharSequence[] var4 = lines;
            int var5 = lines.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                CharSequence msg = var4[var6];
                if(!TextUtils.isEmpty(msg)) {
                    var8.append(msg.toString());
                    var8.append('\n');
                }
            }

            return var8.toString().trim();
        } else {
            CharSequence chars = extras.getCharSequence("android.bigText");
            return !TextUtils.isEmpty(chars)?chars.toString():(!VersionUtils.isJellyBeanMR2()?getExtended(v):getMessage(extras));
        }
    }

    public static String getExtended(ViewGroup localView) {
        Log.d(TAG, "Getting extended message..");
        String msg = "";
        NotificationIds notificationIds = NotificationIds.getInstance(localView.getContext());
        TextView tv = (TextView)localView.findViewById(notificationIds.EMAIL_0);
        if(tv != null && !TextUtils.isEmpty(tv.getText())) {
            msg = msg + tv.getText().toString() + '\n';
        }

        tv = (TextView)localView.findViewById(notificationIds.EMAIL_1);
        if(tv != null && !TextUtils.isEmpty(tv.getText())) {
            msg = msg + tv.getText().toString() + '\n';
        }

        tv = (TextView)localView.findViewById(notificationIds.EMAIL_2);
        if(tv != null && !TextUtils.isEmpty(tv.getText())) {
            msg = msg + tv.getText().toString() + '\n';
        }

        tv = (TextView)localView.findViewById(notificationIds.EMAIL_3);
        if(tv != null && !TextUtils.isEmpty(tv.getText())) {
            msg = msg + tv.getText().toString() + '\n';
        }

        tv = (TextView)localView.findViewById(notificationIds.EMAIL_4);
        if(tv != null && !TextUtils.isEmpty(tv.getText())) {
            msg = msg + tv.getText().toString() + '\n';
        }

        tv = (TextView)localView.findViewById(notificationIds.EMAIL_5);
        if(tv != null && !TextUtils.isEmpty(tv.getText())) {
            msg = msg + tv.getText().toString() + '\n';
        }

        tv = (TextView)localView.findViewById(notificationIds.EMAIL_6);
        if(tv != null && !TextUtils.isEmpty(tv.getText())) {
            msg = msg + tv.getText().toString() + '\n';
        }

        if(msg.isEmpty()) {
            msg = getExpandedText(localView);
        }

        if(msg.isEmpty()) {
            msg = getMessage(localView);
        }

        return msg.trim();
    }

    @SuppressLint({"NewApi"})
    public static ViewGroup getLocalView(Context context, Notification n) {
        RemoteViews view = null;
        if(VERSION.SDK_INT >= 16) {
            view = n.bigContentView;
        }

        if(view == null) {
            view = n.contentView;
        }

        ViewGroup localView = null;

        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            localView = (ViewGroup)inflater.inflate(view.getLayoutId(), (ViewGroup)null);
            view.reapply(context, localView);
        } catch (Exception var5) {
            ;
        }

        return localView;
    }

    public static String getExpandedText(ViewGroup localView) {
        NotificationIds notificationIds = NotificationIds.getInstance(localView.getContext());
        String text = "";
        if(localView != null) {
            View v = localView.findViewById(notificationIds.big_notification_content_text);
            View bigTitleView;
            if(v != null && v instanceof TextView) {
                String titleView = ((TextView)v).getText().toString();
                if(!titleView.equals("")) {
                    bigTitleView = localView.findViewById(android.R.id.title);
                    if(v != null && v instanceof TextView) {
                        String inboxTitleView = ((TextView)bigTitleView).getText().toString();
                        if(!inboxTitleView.equals("")) {
                            text = inboxTitleView + " " + titleView;
                        } else {
                            text = titleView;
                        }
                    } else {
                        text = titleView;
                    }
                }
            }

            v = localView.findViewById(notificationIds.inbox_notification_event_10_id);
            CharSequence titleView1;
            if(v != null && v instanceof TextView) {
                titleView1 = ((TextView)v).getText();
                if(!titleView1.equals("") && !titleView1.equals("")) {
                    text = text + titleView1.toString();
                }
            }

            v = localView.findViewById(notificationIds.inbox_notification_event_9_id);
            if(v != null && v instanceof TextView) {
                titleView1 = ((TextView)v).getText();
                if(!titleView1.equals("")) {
                    text = text + "\n" + titleView1.toString();
                }
            }

            v = localView.findViewById(notificationIds.inbox_notification_event_8_id);
            if(v != null && v instanceof TextView) {
                titleView1 = ((TextView)v).getText();
                if(!titleView1.equals("")) {
                    text = text + "\n" + titleView1.toString();
                }
            }

            v = localView.findViewById(notificationIds.inbox_notification_event_7_id);
            if(v != null && v instanceof TextView) {
                titleView1 = ((TextView)v).getText();
                if(!titleView1.equals("")) {
                    text = text + "\n" + titleView1.toString();
                }
            }

            v = localView.findViewById(notificationIds.inbox_notification_event_6_id);
            if(v != null && v instanceof TextView) {
                titleView1 = ((TextView)v).getText();
                if(!titleView1.equals("")) {
                    text = text + "\n" + titleView1.toString();
                }
            }

            v = localView.findViewById(notificationIds.inbox_notification_event_5_id);
            if(v != null && v instanceof TextView) {
                titleView1 = ((TextView)v).getText();
                if(!titleView1.equals("")) {
                    text = text + "\n" + titleView1.toString();
                }
            }

            v = localView.findViewById(notificationIds.inbox_notification_event_4_id);
            if(v != null && v instanceof TextView) {
                titleView1 = ((TextView)v).getText();
                if(!titleView1.equals("")) {
                    text = text + "\n" + titleView1.toString();
                }
            }

            v = localView.findViewById(notificationIds.inbox_notification_event_3_id);
            if(v != null && v instanceof TextView) {
                titleView1 = ((TextView)v).getText();
                if(!titleView1.equals("")) {
                    text = text + "\n" + titleView1.toString();
                }
            }

            v = localView.findViewById(notificationIds.inbox_notification_event_2_id);
            if(v != null && v instanceof TextView) {
                titleView1 = ((TextView)v).getText();
                if(!titleView1.equals("")) {
                    text = text + "\n" + titleView1.toString();
                }
            }

            v = localView.findViewById(notificationIds.inbox_notification_event_1_id);
            if(v != null && v instanceof TextView) {
                titleView1 = ((TextView)v).getText();
                if(!titleView1.equals("")) {
                    text = text + "\n" + titleView1.toString();
                }
            }

            if(text.equals("")) {
                View titleView2 = localView.findViewById(notificationIds.notification_title_id);
                bigTitleView = localView.findViewById(notificationIds.big_notification_title_id);
                View inboxTitleView1 = localView.findViewById(notificationIds.inbox_notification_title_id);
                if(titleView2 != null && titleView2 instanceof TextView) {
                    text = text + ((TextView)titleView2).getText() + " - ";
                } else if(bigTitleView != null && bigTitleView instanceof TextView) {
                    text = text + ((TextView)titleView2).getText();
                } else if(inboxTitleView1 != null && inboxTitleView1 instanceof TextView) {
                    text = text + ((TextView)titleView2).getText();
                }

                v = localView.findViewById(notificationIds.notification_subtext_id);
                if(v != null && v instanceof TextView) {
                    CharSequence s = ((TextView)v).getText();
                    if(!s.equals("")) {
                        text = text + s.toString();
                    }
                }
            }
        }

        return text.trim();
    }
}
