package com.delfinerija.baristaApp.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.delfinerija.baristaApp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class QRActivitiy extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_qrscan);

        if(checkPermission()){

        }else{
            requestPermission();
        }

        CodeScannerView scannerView = findViewById(R.id.qr_scanner);
        mCodeScanner = new CodeScanner(this, scannerView);
        startScanning();
    }

    private void startScanning(){
        BarcodeFormat qr_code = BarcodeFormat.QR_CODE;
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(qr_code);
        mCodeScanner.setAutoFocusEnabled(false);
        mCodeScanner.setFormats(formats);
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QRActivitiy.this,result.getText(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private boolean checkPermission() {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
