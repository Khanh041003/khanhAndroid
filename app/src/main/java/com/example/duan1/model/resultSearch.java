package com.example.duan1.model;

public class resultSearch {
    private String title, date, image;
    private double price;

    public resultSearch() {
    }

    public resultSearch(String title, String date, String image, double price) {
        this.title = title;
        this.date = date;
        this.image = image;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
