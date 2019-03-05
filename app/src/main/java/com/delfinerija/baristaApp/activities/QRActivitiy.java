package com.delfinerija.baristaApp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.entities.ViewDialog;
import com.delfinerija.baristaApp.network.ApiService;
import com.delfinerija.baristaApp.network.InitApiService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRActivitiy extends BasicActivity {

    private LottieAnimationView animationView;
    private CodeScanner mCodeScanner;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private ApiService apiService;
    private Call<ResponseBody> sendQR;
    private ViewDialog viewDialog;
    private ImageView info_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_qrscan);
        if(savedInstanceState != null){
            InitApiService.initApiService();
        }

        apiService = InitApiService.apiService;
        viewDialog = new ViewDialog(this);
        info_button = findViewById(R.id.info);
        initListeners();

        if(checkPermission()){
            initActivity();
        }
    }

    private void initListeners(){
        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO promijenit da ti pokaze animaciju skeniranja QR
                logut_user();
            }
        });
    }


    //TODO change this to only reset boolean value
    private void logut_user(){
        getSharedPreferences("UserData", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
        finish();
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


    /**
     * Method for QR scanner setup
     */
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
                        checkQRCode(result.toString());
                    }
                });
            }
        });
    }

    /**
     * @param QRcode scanned QR code in string format
     * API call to validate QR code
     */
    private void checkQRCode(String QRcode){
        String token = getUserToken();

        viewDialog.showDialog();
        sendQR = apiService.sendQRcode(token,QRcode);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendQR.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            viewDialog.hideDialog();
                            Intent intent = new Intent(QRActivitiy.this,orderDrinksActivity.class);
                            startActivity(intent);
                        }else{

                            //TODO change error msg
                            viewDialog.hideDialog();
                            try {
                                showError(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            animationView.playAnimation();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        viewDialog.hideDialog();
                        showError(t.getMessage());
                        t.printStackTrace();
                        animationView.playAnimation();
                    }
                });
            }
        }, 500);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(mCodeScanner != null){
            mCodeScanner.startPreview();
        }
    }
}










