package com.delfinerija.baristaApp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.adapters.DrinkCategoryAdapter;
import com.delfinerija.baristaApp.adapters.DrinkItemAdapter;
import com.delfinerija.baristaApp.entities.Category;
import com.delfinerija.baristaApp.entities.Drink;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class NonAlcoholFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_non_alchohol, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView=view.findViewById(R.id.list);
        Drink drink= new Drink("Coca-cola");
        Drink drink2= new Drink("Pelin");
        Drink drink3= new Drink("Pivkan");
        Drink drink4= new Drink("aaa");
        Drink drink5= new Drink("osakd");
        Drink drink6= new Drink("sta si mucko kolu");

        ArrayList<Drink> drinks=new ArrayList<>();
        drinks.add(drink);
        drinks.add(drink2);
        drinks.add(drink3);
        drinks.add(drink4);
        drinks.add(drink5);
        drinks.add(drink6);

        DrinkItemAdapter drinkItemAdapter= new DrinkItemAdapter(getContext(), drinks);
        listView.setAdapter(new SlideExpandableListAdapter(
                drinkItemAdapter,
                R.id.expandable_toggle_button,
                R.id.expandable
        ));
    }
}
