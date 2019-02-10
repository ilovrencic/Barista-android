package com.delfinerija.baristaApp.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
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

    private LottieAnimationView animationView;
    private CodeScanner mCodeScanner;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_qrscan);

        if(checkPermission()){
            initActivity();
        }
    }

    private void initActivity(){
        animationView = findViewById(R.id.animation_scan);
        animationView.setSpeed((float) 0.5);
        animationView.playAnimation();

        CodeScannerView scannerView = findViewById(R.id.qr_scanner);
        mCodeScanner = new CodeScanner(this, scannerView);
        startScanning();
    }

    //method for scanning
    private void startScanning() {
        BarcodeFormat qr_code = BarcodeFormat.QR_CODE;
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(qr_code);
        mCodeScanner.setFormats(formats);
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        animationView.pauseAnimation();
                        vibratePhone();
                        Toast.makeText(QRActivitiy.this, "Succesfully scanned!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void vibratePhone(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initActivity();
                } else {
                    finish();
                }
            }
        }
    }
}










