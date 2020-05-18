package com.example.ushopping.api;

import com.example.ushopping.data.UserData;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ListMembersAPI {

    @GET("/lists/{listId}/users")
    Call<List<UserData>> get(
            @Path("listId") UUID listId,
            @Header("Authorization") UUID authorization
    );

    @POST("/lists/{listId}/users")
    Call<UserData> post(
            @Path("listId") UUID listId,
            @Body UserData data,
            @Header("Authorization") UUID authorization
    );
}
