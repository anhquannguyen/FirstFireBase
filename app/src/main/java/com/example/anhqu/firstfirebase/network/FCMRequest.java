package com.example.anhqu.firstfirebase.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FCMRequest {

    @POST("tokenId")
    @FormUrlEncoded
    Call<Void> postToken(@Field("tokenId") String token);
}
