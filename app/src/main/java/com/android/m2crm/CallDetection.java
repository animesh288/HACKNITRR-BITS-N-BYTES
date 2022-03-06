package com.android.m2crm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
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


        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbzAvOy_pmb7tEKHCCFV4fS_OwOvhrCK3kHGya9BgKw2DagyJTnuT32N3lSgJfxa3xiVuA/exec", new Response.Listener<String>() {
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

                if(type.equals("INCOMING")) DashboardActivity.incoming_count++;
                if(type.equals("OUTGOING")) DashboardActivity.outgoing_count++;
                if(type.equals("MISSED")) DashboardActivity.missed_count++;

                updateDashboard();

                Log.i("animesh",DashboardActivity.call_url);

                TagResolver tagResolver=new TagResolver();
                ArrayList<String > tags=tagResolver.getTags("Hello! I am Rahul, I placed an order on swiggy 20 mins ago and I cannot reach my delivery executive Give me a moment sir, let me just check. Sure! Thank you sir for being on hold, your order will take 15 more minutes to get ready as there is a rush at the restaurant. Your delivery executive is at the restaurant to collect the order as soon as he does it, he will contact you himself.But why can’t I reach my delivery executive? As our delivery man hasnt received your order yet from the restaurant and your delivery code hasnt been generated so that’s why you are are unable to reach the delivery guy.But I wanted to customize my order a little. sir for the same you can contact the restaurant directly. The contact details are provided below your order sure! Thank you for your help Executive: It was my pleasure sir and we apologize for the inconvenience caused. I hope you enjoy your order and have a good day forth");

                Map<String ,String > params=new HashMap<>();
                params.put("action","addCaller");
                params.put("vName",DashboardActivity.name);
                params.put("vId",DashboardActivity.id);
                params.put("vDate",res[1]);
                params.put("vPhone",res[0]);
                params.put("vDuration",res[2]);
                params.put("vType",type);
                params.put("sheet",DashboardActivity.call_name);
                params.put("url",DashboardActivity.call_url);
                params.put("call","Hello! I am Rahul, I placed an order on swiggy 20 mins ago and I cannot reach my delivery executive." +
                        "Give me a moment sir, let me just check. Sure! Thank you sir for being on hold, your order will take 15 more minutes to get ready as there is a rush at the restaurant. Your delivery executive is at the restaurant to collect the order as soon as he does it, he will contact you himself."+
                        "But why can’t I reach my delivery executive? As our delivery man hasnt received your order yet from the restaurant and your delivery code hasnt been generated so that’s why you are are unable to reach the delivery guy.But I wanted to customize my order a little. sir for the same you can contact the restaurant directly. The contact details are provided below your order." +
                        "sure! Thank you for your help Executive: It was my pleasure sir and we apologize for the inconvenience caused. I hope you enjoy your order and have a good day forth");
                params.put("tags",tags.toString());
                return params;
            }
        };
        int socketTimeOut=5000;
        RetryPolicy retryPolicy=new DefaultRetryPolicy(socketTimeOut,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue= Volley.newRequestQueue(DashboardActivity.getInstance());
        requestQueue.add(stringRequest);


    }

    private void updateDashboard() {

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
