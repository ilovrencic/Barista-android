package com.delfinerija.baristaApp.network;

import com.delfinerija.baristaApp.entities.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //TODO dodati token
    @GET("/api/v1/qr_codes/find_table/{qr_hash}")
    Call<ResponseBody> sendQRcode(@Path("qr_hash") String QRcode);

    @POST("/api/v1/users")
    Call<ResponseBody> registerUser(@Body User user);

    @GET("/api/v1/users/check_email")
    Call<ResponseBody> checkEmail(@Query("email") String email);

    @POST("/api/v1/users/resend_confirmation")
    Call<ResponseBody> resendEmail(@Query("email") String email);


    @DELETE("/api/v1/session")
    Call<ResponseBody> signOutUser(@Header("Authorization") String token);

    //TODO napravit user-a kojeg mi vraća
    @POST("/api/v1/session")
    Call<ResponseBody> signInUser();

    //TODO napravit entity lokacija
    @GET("/api/v1/locations")
    Call<ResponseBody> getLocations();

}
