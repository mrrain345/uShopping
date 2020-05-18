package com.example.ushopping.api;

import com.example.ushopping.data.ErrorData;

public interface APIError {
    void onError(ErrorData error);
}
