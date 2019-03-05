package com.delfinerija.baristaApp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.adapters.SectionsPagerAdapter;

public class pickDrinksFragment extends Fragment {
    private TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pick_drinks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new WarmDrinksFragment()); //index 0
        adapter.addFragment(new NonAlcoholFragment()); //index 1
        adapter.addFragment(new BeersFragment()); //index 2
        adapter.addFragment(new OtherAlcoholFragment()); //index 3

        ViewPager viewPager = view.findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        int[] navIcons = {
                R.drawable.ea_bag,
                R.drawable.ilkshake,
                R.drawable.offee_cup_1,
                R.drawable.rappe_1
        };


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            FrameLayout tab = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.nav_tab, null);
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
