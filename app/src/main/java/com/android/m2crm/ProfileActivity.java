package com.android.m2crm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.m2crm.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=binding.name.getText().toString();
                String id=binding.id.getText().toString();
                String call_url=binding.callSheetUrl.getText().toString();
                String call_sheet=binding.callSheetName.getText().toString();
                String wa_url=binding.waSheetUrl.getText().toString();
                String wa_sheet=binding.waSheetName.getText().toString();

                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(id)||TextUtils.isEmpty(call_sheet)||TextUtils.isEmpty(call_url)||TextUtils.isEmpty(wa_sheet)||TextUtils.isEmpty(wa_url)){
                    Toast.makeText(ProfileActivity.this, "Empty Field", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sharedPreferences=getSharedPreferences("pref",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();

                editor.putString("name",name);
                editor.putString("id",id);
                editor.putString("call_url",call_url);
                editor.putString("call_sheet",call_sheet);
                editor.putString("wa_url",wa_url);
                editor.putString("wa_sheet",wa_sheet);

                editor.apply();

                startActivity(new Intent(ProfileActivity.this,DashboardActivity.class));
                finish();
            }
        });

    }
}