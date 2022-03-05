package com.android.m2crm;

import android.content.Context;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
            addItemToSheet(sbn.getNotification().extras.getString(NotificationCompat.EXTRA_TEXT),date,user,"Sheet1","https://docs.google.com/spreadsheets/d/1b6IHqWZp87JP-jql_vF-7e9_whYl5NeWMymciTRtHP0/edit#gid=0");

        }
    }

    private void addItemToSheet(String msg, String d, String user, String sheet1, String url) {
        String message = msg;
        String date = d;
        String name = user;
        if (name.charAt(0) == '+') name = name.substring(1);

        String finalName = name;
        if (message != null) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbzPoiwDXaGXSSzCXdf02-qkyUuq4UcKTQGxh_J1WA0nPsIdGiNa--twvVFWh5OA2zTNCg/exec", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("error aaya hai", error.toString());

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("action", "addMsg");
                    params.put("vEmp","animesh");
                    params.put("vId","2011898902");
                    params.put("vMessage", msg);
                    params.put("vDate", date);
                    params.put("vName", finalName);
                    params.put("sheet", sheet1);
                    params.put("url", url);
                    Log.i("animesh","called");
                    return params;


                }
            };
            int socketTimeOut = 50000;
            RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(retryPolicy);

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.getInstance());
            requestQueue.add(stringRequest);
        }
    }

    public void setListener (MyListener myListener) {
        NotificationService. myListener = myListener ;
    }
}
