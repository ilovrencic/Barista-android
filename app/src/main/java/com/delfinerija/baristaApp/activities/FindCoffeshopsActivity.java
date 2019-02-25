package com.delfinerija.baristaApp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.fragments.BeersFragment;
import com.delfinerija.baristaApp.fragments.MapsFragment;

public class FindCoffeshopsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findcoffeshops);

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
