package com.delfinerija.baristaApp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delfinerija.baristaApp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import es.dmoral.toasty.Toasty;

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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        find_locations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMapServiceOk()){
                    Intent intent = new Intent(MainMenuActivity.this,FindCoffeshopsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }


    private boolean isMapServiceOk(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if(available == ConnectionResult.SUCCESS) {
            return true;
        }
        Toasty.error(MainMenuActivity.this,"An error occurred. Please try again!", Toast.LENGTH_SHORT,true).show();
        return false;
    }


}
