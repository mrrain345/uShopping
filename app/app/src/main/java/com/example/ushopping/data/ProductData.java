package com.example.ushopping.data;

import android.content.Intent;

import java.util.UUID;

public class ProductData {
    public UUID id;
    public UUID listId;
    public String name;
    public Integer count;
    public Boolean isPurchased;

    public ProductData () {}
    public ProductData(String name, Integer count) {
        this.name = name;
        this.count = count;
    }

    public ProductData(UUID listId, UUID id, boolean isPurchased) {
        this.listId = listId;
        this.id = id;
        this.isPurchased = isPurchased;
    }

}
