package com.delfinerija.baristaApp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    }
}
