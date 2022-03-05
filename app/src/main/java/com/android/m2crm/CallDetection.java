package com.android.m2crm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.provider.CallLog;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CallDetection extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)){
            new CountDownTimer(1000,1000){

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    String res[]=getCallDetails(context.getApplicationContext());
                    addItemToSheet(res);
                }
            }.start();
        }
    }

    private void addItemToSheet(String[] res) {


        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwMR7rgljjd9zXe-VukAT1q-P_mTayFFJMhOm7E_WR7vYtDjx1fToHAiZUFlHKmjjNmUA/exec", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("animesh", "success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("animesh","failed");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String type=res[3].equals("1")?"INCOMING":res[3].equals("2")?"OUTGOING":"MISSED";

                Map<String ,String > params=new HashMap<>();
                params.put("action","addCaller");
                params.put("vName","animesh");
                params.put("vId","20118010");
                params.put("vDate",res[1]);
                params.put("vPhone",res[0]);
                params.put("vDuration",res[2]);
                params.put("vType",type);
                params.put("sheet","Sheet1");
                params.put("url","https://docs.google.com/spreadsheets/d/1i_oQCVU5xywm6Utf9stoW76qBESfIsY3BIR0QhjVTuU/edit#gid=0");
                return params;
            }
        };
        int socketTimeOut=5000;
        RetryPolicy retryPolicy=new DefaultRetryPolicy(socketTimeOut,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue= Volley.newRequestQueue(DashboardActivity.getInstance());
        requestQueue.add(stringRequest);


    }

    private String[] getCallDetails(Context context) {

        String res[]=new String[4];

        StringBuffer stringBuffer=new StringBuffer();
        Cursor cursor=context.getContentResolver().query(CallLog.Calls.CONTENT_URI,null,null,null,CallLog.Calls.DATE+" DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration=cursor.getColumnIndex(CallLog.Calls.DURATION);
        int type=cursor.getColumnIndex(CallLog.Calls.TYPE);
        while (cursor.moveToNext()) {
            res[0] = cursor.getString(number);
            String callDate = cursor.getString(date);
            res[1] = (new Date(Long.valueOf(callDate))).toString();
            res[2] =cursor.getString(duration);
            res[3] =cursor.getString(type);
            String dir = null;
            Log.i("animesh", Arrays.toString(res));
            cursor.close();
            break;
        }
        return res;
    }

}
