package com.delfinerija.baristaApp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.network.ApiService;
import com.delfinerija.baristaApp.network.InitApiService;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmEmailActivity extends AppCompatActivity {

    private LottieAnimationView animationView;
    private Button resend_email;
    private Button login;
    private TextView email;
    private ApiService apiService;
    private Call<ResponseBody> resendEmail;
    private String email_address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_email);


        if(savedInstanceState != null){
            InitApiService.initApiService();
        }



        email_address = getIntent().getStringExtra("email");
        apiService = InitApiService.apiService;
        animationView = findViewById(R.id.email_animation);
        resend_email = findViewById(R.id.resend_button);
        login = findViewById(R.id.login_me_button);
        email = findViewById(R.id.user_email);
        email.setText(email_address);
        animationView.setSpeed((float)0.8);
        animationView.playAnimation();



        initListeners();
    }

    private void initListeners(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmEmailActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        resend_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend_email_api();
            }
        });
    }

    private void resend_email_api(){
        resendEmail = apiService.resendEmail(email_address);
        resendEmail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    animationView.playAnimation();
                }else{
                    Toasty.error(ConfirmEmailActivity.this,"An error occurred. Try again!", Toast.LENGTH_SHORT,true).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(ConfirmEmailActivity.this,"Unexpected error occurred!",Toast.LENGTH_SHORT,true).show();
            }
        });
    }



}
