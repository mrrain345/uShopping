package com.example.ushopping.data;

import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import retrofit2.Response;

public class ErrorData {
    public int status;
    public int code;
    public String title;

    public ErrorData() {};

    public ErrorData(int status, int code, String title) {
        this.status = status;
        this.code = code;
        this.title = title;
    }

    public static <T> ErrorData getError(Response<T> response) {
        if (response.isSuccessful()) return null;

        if (response.code() == 500) {
            return new ErrorData(500, 0, "Internal server error");
        }

        Gson gson = new Gson();
        ErrorData error = gson.fromJson(response.errorBody().charStream(), ErrorData.class);
        error.status = response.code();
        return error;
    }

    public static <T> boolean show(Response<T> response, View view) {
        if (response.isSuccessful()) return false;

        if (response.code() == 500) {
            Snackbar.make(view, "Internal server error", Snackbar.LENGTH_LONG).show();
            return true;
        }

        Gson gson = new Gson();
        ErrorData error = gson.fromJson(response.errorBody().charStream(), ErrorData.class);
        Snackbar.make(view, error.title, Snackbar.LENGTH_LONG).show();
        return true;
    }
}
