package com.android.m2crm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.m2crm.databinding.ActivitySheetBinding;

public class SheetActivity extends AppCompatActivity {

    ActivitySheetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivitySheetBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl("file:///android_asset/"+"sheetHtml.html");
    }
}