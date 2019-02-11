package com.delfinerija.baristaApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.entities.Category;

import java.util.ArrayList;

public class DrinkCategoryAdapter extends ArrayAdapter<Category> {
    public DrinkCategoryAdapter(Context context, ArrayList<Category> categories) {
        super(context, R.layout.drink_category, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category=getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drink_category, parent, false);
        }
//
//
//        ListView listView=convertView.findViewById(R.id.innerListView);
//        listView.setNestedScrollingEnabled(true);
//        DrinkItemAdapter drinkItemAdapter= new DrinkItemAdapter(getContext(),category.getStrings());
//        listView.setAdapter(drinkItemAdapter);
        return convertView;
    }
}