package com.delfinerija.baristaApp.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.fragments.BeersFragment;
import com.delfinerija.baristaApp.fragments.NonAlcoholFragment;
import com.delfinerija.baristaApp.fragments.OtherAlcoholFragment;
import com.delfinerija.baristaApp.fragments.WarmDrinksFragment;

public class PickDrinksActivity extends AppCompatActivity {
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_drinks);

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WarmDrinksFragment()); //index 0
        adapter.addFragment(new NonAlcoholFragment()); //index 1
        adapter.addFragment(new BeersFragment()); //index 2
        adapter.addFragment(new OtherAlcoholFragment()); //index 3

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        int[] navIcons = {
                R.drawable.ea_bag,
                R.drawable.ilkshake,
                R.drawable.offee_cup_1,
                R.drawable.rappe_1
        };


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            FrameLayout tab = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.nav_tab, null);
            ImageView tab_icon = tab.findViewById(R.id.selectedImage);
            tab_icon.setImageResource(navIcons[i]);
            tab_icon = tab.findViewById(R.id.notSelectedImage);
            tab_icon.setImageResource(navIcons[i]);
            tabLayout.getTabAt(i).setCustomView(tab);
        }

        tabLayout.getTabAt(0).getCustomView().findViewById(R.id.selected).setVisibility(View.VISIBLE);
        tabLayout.getTabAt(0).getCustomView().findViewById(R.id.notSelected).setVisibility(View.INVISIBLE);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){
                    tabLayout.getTabAt(0).getCustomView().findViewById(R.id.selected).setVisibility(View.VISIBLE);
                    tabLayout.getTabAt(0).getCustomView().findViewById(R.id.notSelected).setVisibility(View.INVISIBLE);
                }else if (tab.getPosition()==1){
                    tabLayout.getTabAt(1).getCustomView().findViewById(R.id.selected).setVisibility(View.VISIBLE);
                    tabLayout.getTabAt(1).getCustomView().findViewById(R.id.notSelected).setVisibility(View.INVISIBLE);
                }else if (tab.getPosition()==2){
                    tabLayout.getTabAt(2).getCustomView().findViewById(R.id.selected).setVisibility(View.VISIBLE);
                    tabLayout.getTabAt(2).getCustomView().findViewById(R.id.notSelected).setVisibility(View.INVISIBLE);
                }else if (tab.getPosition()==3){
                    tabLayout.getTabAt(3).getCustomView().findViewById(R.id.selected).setVisibility(View.VISIBLE);
                    tabLayout.getTabAt(3).getCustomView().findViewById(R.id.notSelected).setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){
                    tabLayout.getTabAt(0).getCustomView().findViewById(R.id.selected).setVisibility(View.INVISIBLE);
                    tabLayout.getTabAt(0).getCustomView().findViewById(R.id.notSelected).setVisibility(View.VISIBLE);
                }else if (tab.getPosition()==1){
                    tabLayout.getTabAt(1).getCustomView().findViewById(R.id.selected).setVisibility(View.INVISIBLE);
                    tabLayout.getTabAt(1).getCustomView().findViewById(R.id.notSelected).setVisibility(View.VISIBLE);
                }else if (tab.getPosition()==2){
                    tabLayout.getTabAt(2).getCustomView().findViewById(R.id.selected).setVisibility(View.INVISIBLE);
                    tabLayout.getTabAt(2).getCustomView().findViewById(R.id.notSelected).setVisibility(View.VISIBLE);
                }else if (tab.getPosition()==3){
                    tabLayout.getTabAt(3).getCustomView().findViewById(R.id.selected).setVisibility(View.INVISIBLE);
                    tabLayout.getTabAt(3).getCustomView().findViewById(R.id.notSelected).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}
