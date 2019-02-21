package com.delfinerija.baristaApp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delfinerija.baristaApp.R;

public class MainMenuActivity extends AppCompatActivity {

    private RelativeLayout qr_scan;
    private RelativeLayout find_locations;
    private RelativeLayout edit_profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        qr_scan = findViewById(R.id.qr_scanner_layout);
        find_locations = findViewById(R.id.location_layout);
        edit_profile = findViewById(R.id.profile_layout);
        TextView username = findViewById(R.id.user_name);

        username.setText(getSharedPreferences("UserData", MODE_PRIVATE).getString("name",""));
        initListeners();
    }

    private void initListeners(){
        qr_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this,QRActivitiy.class);
                startActivity(intent);
            }
        });
    }


}
