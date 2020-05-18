package com.example.ushopping.data;

import java.util.Date;
import java.util.UUID;

public class ListData {

    public String title;
    public Date created_at;
    public UUID id;

    public ListData(){}

    public ListData(String title, Date createdAt, UUID id) {
        this.title = title;
        this.created_at = createdAt;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return created_at;
    }

    public UUID getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.created_at = date;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
