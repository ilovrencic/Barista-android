package com.delfinerija.baristaApp.network;

import com.delfinerija.baristaApp.entities.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/api/v1/qr_codes/find_table/{qr_hash}")
    Call<ResponseBody> sendQRcode(@Path("qr_hash") String QRcode);

    @POST("/api/v1/users")
    Call<ResponseBody> registerUser(@Body User user);

    @GET("/api/v1/users/check_email")
    Call<ResponseBody> checkEmail(@Query("email") String email);

    @POST("/api/v1/users/resend_confirmation")
    Call<ResponseBody> resendEmail(@Query("email") String email);

//  CILJ JE BIO MOZDA OLAKSAT TI, METODE I PATH-OVI SU DOBRI A OVAJ RED ISPOD PRILAGODI OPISU UZ SVAKI CALL STO TI NAPISEM

//  tu mi nemoras nista slat, bitno je da je token u header pod kljucem Authorization
    @DELETE("/api/v1/session")
    Call<ResponseBody> signInUser();

//  tu mi saljes email i password, ja ti vratim cijelog usera koji ce imat i token pa ti to spremas kak vec dalje, to ti znas
    @POST("/api/v1/session")
    Call<ResponseBody> signOutUser();

//  tu mi ne saljes nista dodatno, on ti na ovo vrati sve lokacije s imenima kafica
    @GET("/api/v1/locations")
    Call<ResponseBody> getLocations();

}
