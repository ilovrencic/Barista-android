package com.delfinerija.baristaApp.network;

import com.delfinerija.baristaApp.entities.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/api/v1/qr_codes/find_table/{qr_hash}")
    Call<ResponseBody> sendQRcode(@Path("qr_hash") String QRcode);

    @POST("/api/v1/users")
    Call<ResponseBody> registerUser(@Body User user);
}
