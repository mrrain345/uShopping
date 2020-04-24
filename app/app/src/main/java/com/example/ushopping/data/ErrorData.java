package com.example.ushopping.data;

import com.google.gson.Gson;

import retrofit2.Response;

public class ErrorData {
    public int status;
    public int code;
    public String title;

    public static <T> ErrorData getError(Response<T> response) {
        if (response.isSuccessful()) return null;

        Gson gson = new Gson();
        ErrorData error = gson.fromJson(response.errorBody().charStream(), ErrorData.class);
        error.status = response.code();
        return error;
    }
}
