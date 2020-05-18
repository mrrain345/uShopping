package com.example.ushopping.api;

import com.example.ushopping.data.ProductListData;
import com.example.ushopping.data.TitleData;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductListsApi {

    @GET("/lists")
    Call<List<ProductListData>> getAll(
            @Header("Authorization") UUID authorisation
    );

    @GET("/lists/{id}")
    Call<List<ProductListData>> getOne(
            @Path("id") UUID id,
            @Header("Authorization") UUID authorisation
    );

    @POST("/lists")
    Call<ProductListData> post(
            @Body TitleData data,
            @Header("Authorization") UUID authorisation
    );

    @PATCH("/lists/{id}")
    Call<ProductListData> patch(
            @Path("id") UUID id,
            @Body ProductListData list,
            @Header("Authorization") UUID authorisation
    );
}
