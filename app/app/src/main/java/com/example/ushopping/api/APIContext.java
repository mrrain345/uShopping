package com.example.ushopping.api;

import com.example.ushopping.data.ErrorData;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIContext {

    private static Retrofit api;
    private static final String BASE_URL = "http://10.0.2.2:5000";

    public static Retrofit getContext() {
        if (api == null) {
            api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return api;
    }
}
