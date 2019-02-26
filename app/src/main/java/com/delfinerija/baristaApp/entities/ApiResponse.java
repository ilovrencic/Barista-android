package com.delfinerija.baristaApp.entities;

import com.squareup.moshi.Json;

public class ApiResponse<T> {

    @Json(name = "id")
    private long id;

    @Json(name = "type")
    private String type;

    @Json(name = "attributes")
    private T data;

    public ApiResponse(long id, String type, T data){
        this.id = id;
        this.type = type;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public T getData() {
        return data;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setData(T data) {
        this.data = data;
    }
}
