package com.example.ushopping.data;

public class ListData {

    public String title;
    public String createdAt;
    public int id;

    public ListData(){}

    public ListData(String title, String createdAt, int id) {
        this.title = title;
        this.createdAt = createdAt;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return createdAt;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.createdAt = date;
    }

    public void setId(int id) {
        this.id = id;
    }
}
