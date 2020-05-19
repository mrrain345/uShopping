package com.example.ushopping.data;

import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import retrofit2.Response;

public class ErrorData {
    public int status;
    public String title;

    public ErrorData() {};

    public ErrorData(int status, String title) {
        this.status = status;
        this.title = title;
    }

    public static <T> ErrorData getError(Response<T> response) {
        if (response.isSuccessful()) return null;

        if (response.code() == 500) {
            return new ErrorData(500, "Internal server error");
        }

        Gson gson = new Gson();
        ErrorData error = gson.fromJson(response.errorBody().charStream(), ErrorData.class);

        if (error == null || error.title == null) {
            return new ErrorData(response.code(), "Unknown error: " + response.code());
        }

        return new ErrorData(response.code(), error.title);
    }

    public static <T> boolean show(Response<T> response, View view) {
        ErrorData error = getError(response);
        if (error == null) return false;

        Snackbar.make(view, error.title, Snackbar.LENGTH_LONG).show();
        return true;
    }

    public static <T> boolean show(ErrorData error, View view) {
        if (error == null) return false;
        Snackbar.make(view, error.title, Snackbar.LENGTH_LONG).show();
        return true;
    }
}
