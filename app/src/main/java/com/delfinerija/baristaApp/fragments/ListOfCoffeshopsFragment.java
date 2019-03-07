package com.delfinerija.baristaApp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.adapters.CoffeeshopAdapter;
import com.delfinerija.baristaApp.entities.ApiResponse;
import com.delfinerija.baristaApp.entities.MapLocation;
import com.google.android.gms.common.api.Api;

import java.util.List;

@SuppressLint("ValidFragment")
public class ListOfCoffeshopsFragment extends Fragment {

    private List<ApiResponse<MapLocation>> locations;
    private RecyclerView recyclerView;
    private CoffeeshopAdapter adapter;

    @SuppressLint("ValidFragment")
    public ListOfCoffeshopsFragment(List<ApiResponse<MapLocation>> locations){
        this.locations = locations;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coffeshop_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_coffeshops);

        initRecyclerView();
        initAdapter(locations);
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initAdapter(List<ApiResponse<MapLocation>> locations){
        adapter = new CoffeeshopAdapter(locations);
        recyclerView.setAdapter(adapter);
    }
}
