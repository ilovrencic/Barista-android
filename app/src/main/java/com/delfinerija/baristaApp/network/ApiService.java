package com.delfinerija.baristaApp.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/nes random/cekam buhu")
    Call<GenericResponse<String>> sendQRcode(String QRcode);


}
