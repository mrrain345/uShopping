package com.example.ushopping.api;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.ushopping.data.ErrorData;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APICall <T> {
    View view;
    Call<T> restCall;
    APICallback<T> callback;
    APIError error;
    APIException exception;

    public APICall(View view, Call<T> restCall, APICallback call) {
        this.view = view;
        this.restCall = restCall;
        this.callback = call;
    }

    public APICall error(APIError error) {
        this.error = error;
        return this;
    }

    public APICall exception(APIException t) {
        this.exception = t;
        return this;
    }

    public static <T> APICall makeCall(View view, Call<T> restCall, APICallback<T> call) {
        return new APICall(view, restCall, call);
    }

    public void call() {
        restCall.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                ErrorData err = ErrorData.getError(response);
                if (err == null) callback.onCall(response.body());
                else if (error != null) error.onError(err);
                else ErrorData.show(response, view);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (exception != null) exception.onException(t);
                else {
                    Log.wtf("RETROFIT", t);
                    Snackbar.make(view, "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
