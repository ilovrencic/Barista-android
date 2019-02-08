package com.delfinerija.baristaApp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.delfinerija.baristaApp.R;

public class SplashLoginActivity extends AppCompatActivity {

    private Button google_login;
    private Button register_button;
    private TextView sign_in;
    private TextView terms_of_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashlogin);

        //parameter initialisation
        google_login = findViewById(R.id.google_button);
        register_button = findViewById(R.id.register_button);
        sign_in = findViewById(R.id.sign_in_text);
        terms_of_service = findViewById(R.id.terms_of_service);

        initListeners();
    }

    private void initListeners(){
        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashLoginActivity.this,QRActivitiy.class);
                startActivity(intent);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        terms_of_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
