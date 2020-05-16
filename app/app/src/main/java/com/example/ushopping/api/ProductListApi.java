package com.example.ushopping.api;

import com.example.ushopping.data.ListData;
import com.example.ushopping.data.ListSendData;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ProductListApi {

    /*@GET("/users/{user}")
    Call<UserData> getUser(
            @Path("user") int userId
    );*/

    @POST("/lists")
    Call<ListData> post(
            @Header("Authorisation") UUID authorisation,
            @Body ListSendData data
    );


}
