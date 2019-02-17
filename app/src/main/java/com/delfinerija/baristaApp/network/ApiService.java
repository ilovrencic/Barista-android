package com.delfinerija.baristaApp.network;

import com.delfinerija.baristaApp.entities.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/api/v1/qr_codes/find_table/{qr_hash}")
    Call<ResponseBody> sendQRcode(@Path("qr_hash") String QRcode);

    @POST("/api/v1/users")
    Call<ResponseBody> registerUser(@Body User user);

    @GET("/api/v1/check_email")
    Call<ResponseBody> checkEmail(@Query("email") String email);

    @GET("/api/v1/users/resend_confiramtion")
    Call<ResponseBody> resendEmail(@Query("email") String email);

}
