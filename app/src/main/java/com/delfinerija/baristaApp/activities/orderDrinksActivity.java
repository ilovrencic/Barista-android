package com.delfinerija.baristaApp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.adapters.SectionsPagerAdapter;
import com.delfinerija.baristaApp.adapters.VerticalViewPager;
import com.delfinerija.baristaApp.fragments.CheckoutFragment;
import com.delfinerija.baristaApp.fragments.pickDrinksFragment;

public class orderDrinksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_drinks);

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new pickDrinksFragment()); //index 0
        adapter.addFragment(new CheckoutFragment()); //index 1

        VerticalViewPager viewPager = findViewById(R.id.verticalViewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }
}
