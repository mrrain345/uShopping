package com.example.ushopping.api;

import com.example.ushopping.data.SignUpData;
import com.example.ushopping.data.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersAPI {

    @GET("/users/{user}")
    Call<UserData> getUser(
            @Path("user") int userId
    );

    @POST("/users")
    Call<UserData> post(
            @Body SignUpData data
    );
}
