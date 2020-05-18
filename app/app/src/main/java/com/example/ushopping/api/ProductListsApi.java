package com.example.ushopping.api;

import com.example.ushopping.data.ListData;
import com.example.ushopping.data.ListSendData;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductListsApi {

    @GET("/lists")
    Call<List<ListData>> getAll(
            @Header("Authorization") UUID authorisation
    );

    @POST("/lists")
    Call<ListData> post(
            @Header("Authorization") UUID authorisation,
            @Body ListSendData data
    );


}
