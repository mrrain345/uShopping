package com.example.ushopping.api;

import com.example.ushopping.data.IdData;
import com.example.ushopping.data.ProductData;

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

public interface ProductsAPI {

    @GET("/lists/{listId}/products")
    Call<List<ProductData>> get(
            @Path("listId") UUID listId,
            @Header("Authorization") UUID authorization
    );

    @POST("/lists/{listId}/products")
    Call<ProductData> post(
            @Path("listId") UUID listId,
            @Body ProductData data,
            @Header("Authorization") UUID authorization
    );

    @PATCH("/lists/{listId}/products/{id}")
    Call<ProductData> patch(
            @Path("listId") UUID listId,
            @Path("id") UUID id,
            @Body ProductData data,
            @Header("Authorization") UUID authorization
    );

    @DELETE("/lists/{listId}/products/{id}")
    Call<IdData> delete(
            @Path("listId") UUID listId,
            @Path("id") UUID id,
            @Header("Authorization") UUID authorization
    );
}
