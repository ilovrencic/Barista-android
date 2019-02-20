package com.delfinerija.baristaApp.entities;

import com.squareup.moshi.Json;

public class Session {

    @Json(name = "email")
    private String email;

    @Json(name = "password")
    private String password;

    public Session(String email,String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
