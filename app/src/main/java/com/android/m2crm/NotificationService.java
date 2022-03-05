package com.android.m2crm;

import android.content.Context;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class NotificationService extends NotificationListenerService {

    Context context;
    static MyListener myListener;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        if(sbn.getPackageName().equals("com.whatsapp") || sbn.getPackageName().equals("com.whatsapp.w4b")) {
            String date= Calendar.getInstance().getTime().toString();
            String msg=sbn.getNotification().extras.getString(NotificationCompat.EXTRA_TEXT);
            String user=sbn.getNotification().extras.getString(NotificationCompat.EXTRA_TITLE);
//            Log.i(TAG, "********** onNotificationPosted");
            Log.i("animesh", "animesh :" + msg + " \t " +user + " \t " + sbn.getPackageName());
        }
    }
    public void setListener (MyListener myListener) {
        NotificationService. myListener = myListener ;
    }
}
