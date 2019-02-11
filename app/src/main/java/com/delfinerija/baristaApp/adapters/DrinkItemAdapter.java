package com.delfinerija.baristaApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.entities.Drink;

import java.util.ArrayList;

public class DrinkItemAdapter extends ArrayAdapter<Drink> {
    public DrinkItemAdapter(Context context, ArrayList<Drink> drinks) {
        super(context, R.layout.drink_item2, drinks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Drink drink=getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drink_item2, parent, false);
        }
        TextView nameTextView=convertView.findViewById(R.id.itemName);
        nameTextView.setText(drink.getName());
        return convertView;
    }
}
