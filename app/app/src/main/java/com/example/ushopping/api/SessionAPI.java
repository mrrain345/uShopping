package com.example.ushopping.api;

import com.example.ushopping.data.LoginData;
import com.example.ushopping.data.SessionGetData;
import com.example.ushopping.data.SessionPostData;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SessionAPI {

    @GET("/session")
    Call<SessionGetData> get(
            @Header("Authorization") UUID authorization
    );

    @POST("/session")
    Call<SessionPostData> post(
            @Body LoginData data
    );

    @DELETE("/session")
    Call<SessionGetData> delete(
            @Header("Authorization") UUID authorization
    );
}
