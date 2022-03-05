package com.android.m2crm;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.android.m2crm.databinding.ActivityDashboardBinding;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
//import com.jaiselrahman.filepicker.activity.FilePickerActivity;
//import com.jaiselrahman.filepicker.config.Configurations;

import java.security.PublicKey;

public class DashboardActivity extends AppCompatActivity {

    public static int incoming_count=0;
    public static int outgoing_count=0;
    public static int missed_count=0;
    public static int queries_count=0;
    public static String name="";
    public static String id="";
    public static String call_url="";
    public static String call_name="";
    public static String wa_url="";
    public static String wa_name="";
    public static boolean permitted=false;

    int requestcode=1;

    ActivityDashboardBinding binding;
    private static DashboardActivity instance;
    int PERMISSION_ALL = 1;

    String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityDashboardBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        instance=this;

        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }

        Python py=Python.getInstance();

//        PyObject pyObject=py.getModule("spmodel");

//        PyObject obj=pyObject.callAttr("mecrm");

//        Log.i("python",obj.toString());

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)!=PackageManager.PERMISSION_GRANTED && !permitted){
            permitted=true;
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS" ) ;
            startActivity(intent);
        }

        binding.incomingCount.setText(incoming_count/2+"");
        binding.outgoingingCount.setText(outgoing_count/2+"");
        binding.missedCount.setText(missed_count/2+"");
        binding.queriesCount.setText(queries_count+"");


        SharedPreferences sharedPreferences=getSharedPreferences("pref",MODE_PRIVATE);
        name=sharedPreferences.getString("name","");
        id=sharedPreferences.getString("id","");
        call_url=sharedPreferences.getString("call_url","");
        call_name=sharedPreferences.getString("call_sheet","");
        wa_url=sharedPreferences.getString("wa_url","");
        wa_name=sharedPreferences.getString("wa_sheet","");

        binding.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,ProfileActivity.class));
            }
        });

        binding.addfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(DashboardActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                    openFile();
                }else{
                    Log.i("animesh","storage");
                }
            }
        });

        binding.sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,SheetActivity.class));
            }
        });
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

        public void openFile(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        intent=Intent.createChooser(intent,"Choose a file");
        sActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> sActivityResultLauncher=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==Activity.RESULT_OK){
                        Intent data=result.getData();
                        Uri uri=data.getData();
                        Log.i("animesh",UriUtils.getPathFromUri(DashboardActivity.this,uri));
                    }
                }
            });


    @Override
    protected void onResume() {
        super.onResume();
        binding.incomingCount.setText(incoming_count/2+"");
        binding.outgoingingCount.setText(outgoing_count/2+"");
        binding.missedCount.setText(missed_count/2+"");
        binding.queriesCount.setText(queries_count+"");
    }
}