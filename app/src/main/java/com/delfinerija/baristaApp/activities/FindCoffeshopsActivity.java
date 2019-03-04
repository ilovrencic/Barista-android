package com.delfinerija.baristaApp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.fragments.BeersFragment;
import com.delfinerija.baristaApp.fragments.MapsFragment;
import com.delfinerija.baristaApp.network.ApiService;
import com.delfinerija.baristaApp.network.InitApiService;

public class FindCoffeshopsActivity extends AppCompatActivity {

    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findcoffeshops);

        if(savedInstanceState != null){
            InitApiService.initApiService();
        }

        apiService = InitApiService.apiService;

        setupViewPager();
    }


    private void setupViewPager(){
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new MapsFragment());
        sectionsPagerAdapter.addFragment(new BeersFragment());
        ViewPager viewPager = findViewById(R.id.holder);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
    }
}
