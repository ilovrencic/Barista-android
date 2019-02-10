package com.delfinerija.baristaApp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.delfinerija.baristaApp.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.github.ybq.android.spinkit.style.Wave;
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
        final FrameLayout layout = findViewById(R.id.frame_layout);
        final ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = layout.getMeasuredWidth();
                int height = layout.getMeasuredHeight();
                ViewGroup.LayoutParams layoutParams = animationView.getLayoutParams();
                layoutParams.height = (int)(height*0.65);
                layoutParams.width = (int)(width*0.65);
                animationView.setLayoutParams(layoutParams);
            }
        });
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
        mCodeScanner.setAutoFocusEnabled(false);
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
                        showLoading();
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

    private void showLoading(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }
}










