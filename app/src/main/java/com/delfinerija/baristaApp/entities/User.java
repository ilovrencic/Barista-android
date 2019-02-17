package com.delfinerija.baristaApp.entities;

import com.squareup.moshi.Json;

public class User {

    @Json(name = "first_name")
    private String first_name;

    @Json(name = "last_name")
    private String last_name;

    @Json(name = "email")
    private String email_address;

    @Json(name = "password")
    private String password;

    @Json(name = "token")
    private String token;

    public User(String first_name, String last_name, String email_address, String password,String token){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email_address = email_address;
        this.password = password;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail_address() {
        return email_address;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
