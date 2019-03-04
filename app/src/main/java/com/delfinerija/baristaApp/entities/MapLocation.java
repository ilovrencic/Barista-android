package com.delfinerija.baristaApp.entities;

import com.squareup.moshi.Json;

public class MapLocation {

    @Json(name = "coordinates")
    private String coordinates;

    @Json(name = "address")
    private String address;

    @Json(name = "coffee_shop_name")
    private String coffee_shop_name;

    @Json(name = "distance")
    private Double distance;

    @Json(name = "image_url")
    private String image_url;

    public MapLocation(String coordinates, String address, String coffee_shop_name){
        this.address = address;
        this.coffee_shop_name = coffee_shop_name;
        this.coordinates = coordinates;
    }

    public String getAddress() {
        return address;
    }

    public String getCoffee_shop_name() {
        return coffee_shop_name;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCoffee_shop_name(String coffee_shop_name) {
        this.coffee_shop_name = coffee_shop_name;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
