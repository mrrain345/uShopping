package com.example.ushopping.data;

import java.util.Date;
import java.util.UUID;

public class ProductListData {
    public UUID id;
    public String title;
    public Date createdAt;

    public ProductListData() {}

    public ProductListData(UUID id, String title, Date createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }
}
