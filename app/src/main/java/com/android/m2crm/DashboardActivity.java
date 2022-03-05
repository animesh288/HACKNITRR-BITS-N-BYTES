package com.android.m2crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.android.m2crm.databinding.ActivityDashboardBinding;

import java.security.PublicKey;

public class DashboardActivity extends AppCompatActivity {

    public static int incoming_count=0;
    public static int outgoing_count=0;

    ActivityDashboardBinding binding;
    private static DashboardActivity instance;
    int PERMISSION_ALL = 1;

    String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityDashboardBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        instance=this;

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
//        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)!=PackageManager.PERMISSION_GRANTED){
//            Log.i("kat gaya","phirse");
//            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS" ) ;
//            startActivity(intent);
//        }

        binding.incomingCount.setText(incoming_count+"");
        binding.outgoingingCount.setText(outgoing_count+"");
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static DashboardActivity getInstance(){
        return instance;
    }
}