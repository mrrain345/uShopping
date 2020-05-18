package com.example.ushopping.api;

public interface APICallback<T> {
    void onCall(T data);
}
