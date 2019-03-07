package com.delfinerija.baristaApp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.delfinerija.baristaApp.R;
import com.delfinerija.baristaApp.entities.ApiResponse;
import com.delfinerija.baristaApp.entities.MapLocation;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CoffeeshopAdapter extends RecyclerView.Adapter<CoffeeshopAdapter.ViewHolder> {

    private List<ApiResponse<MapLocation>> locations;


    public CoffeeshopAdapter(List<ApiResponse<MapLocation>> locations){
        this.locations = locations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_coffeshop,parent,false);
        return new CoffeeshopAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeshopAdapter.ViewHolder viewHolder, int i) {
        ApiResponse<MapLocation> location = locations.get(i);
        ImageView coffeshop_image = viewHolder.itemView.findViewById(R.id.coffeeshop_image);
        TextView distance = viewHolder.itemView.findViewById(R.id.distance);
        TextView coffeeshop_name  = viewHolder.itemView.findViewById(R.id.coffeeshop_name);
        TextView coffeeshop_adress = viewHolder.itemView.findViewById(R.id.coffeeshop_address);

        if(location.getData().getImage_url() != null){
            Picasso.get().load(location.getData().getImage_url()).into(coffeshop_image);
        }

        Double distance_in_km = location.getData().getDistance()/1000;
        distance_in_km = Double.valueOf(Math.round(distance_in_km));

        distance.setText(distance_in_km.toString());
        coffeeshop_name.setText(location.getData().getCoffee_shop_name());
        coffeeshop_adress.setText(location.getData().getAddress());
    }

    public void setLocations(List<ApiResponse<MapLocation>> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private View itemView;

        public ViewHolder(View itemView){
            super(itemView);
            this.itemView = itemView;
        }
    }

}
