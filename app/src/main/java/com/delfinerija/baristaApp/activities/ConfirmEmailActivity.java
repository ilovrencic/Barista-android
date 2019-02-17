package com.delfinerija.baristaApp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.network.ApiService;
import com.delfinerija.baristaApp.network.InitApiService;

public class ConfirmEmailActivity extends AppCompatActivity {

    private LottieAnimationView animationView;
    private Button resend_email;
    private Button login;
    private TextView email;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_email);

        if(savedInstanceState != null){
            InitApiService.initApiService();
        }

        String email_address = getIntent().getStringExtra("email");

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
                //TODO login intent
            }
        });

        resend_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO api poziv za ponovo slanje
            }
        });
    }



}
