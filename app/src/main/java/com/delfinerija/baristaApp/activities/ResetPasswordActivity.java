package com.delfinerija.baristaApp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class ResetPasswordActivity extends AppCompatActivity {

    private Button login;
    private Button resend_email;
    private ApiService apiService;
    private LottieAnimationView animationView;
    private String email_intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_email);


        if(savedInstanceState != null){
            InitApiService.initApiService();
        }


        email_intent = getIntent().getStringExtra("email");
        apiService = InitApiService.apiService;
        animationView = findViewById(R.id.email_animation);
        TextView email = findViewById(R.id.user_email);
        email.setText(email_intent);
        TextView text = findViewById(R.id.reset_pass_text);
        login = findViewById(R.id.login_me_button);
        resend_email = findViewById(R.id.resend_button);


        text.setText(getString(R.string.reset_password));
        initListeners();

        animationView.setSpeed((float)0.8);
        animationView.playAnimation();
    }

    private void initListeners(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
        Call<ResponseBody> reset_password = apiService.resetPassword(email_intent);
        reset_password.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    animationView.playAnimation();
                }else{
                    Toasty.error(ResetPasswordActivity.this,"An error occurred. Try again!", Toast.LENGTH_SHORT,true).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toasty.error(ResetPasswordActivity.this,"An error occurred. Try again!", Toast.LENGTH_SHORT,true).show();
            }
        });


    }
}
