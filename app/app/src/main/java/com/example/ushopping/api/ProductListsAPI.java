package com.example.ushopping.api;

import com.example.ushopping.data.IdData;
import com.example.ushopping.data.ProductListData;
import com.example.ushopping.data.TitleData;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductListsAPI {

    @GET("/lists")
    Call<List<ProductListData>> getAll(
            @Header("Authorization") UUID authorization
    );

    @GET("/lists/{id}")
    Call<ProductListData> getOne(
            @Path("id") UUID id,
            @Header("Authorization") UUID authorization
    );

    @POST("/lists")
    Call<ProductListData> post(
            @Body TitleData data,
            @Header("Authorization") UUID authorization
    );

    @PATCH("/lists/{id}")
    Call<ProductListData> patch(
            @Path("id") UUID id,
            @Body ProductListData list,
            @Header("Authorization") UUID authorization
    );

    @DELETE("/lists/{id}")
    Call<IdData> delete(
            @Path("id") UUID id,
            @Header("Authorization") UUID authorization
    );
}
