package com.delfinerija.baristaApp.entities;

import com.squareup.moshi.Json;

public class UserResponse {

    @Json(name = "id")
    private long id;

    @Json(name = "type")
    private String type;

    @Json(name = "attributes")
    private User user;

    public UserResponse(long id,String type,User user){
        this.id = id;
        this.type = type;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
