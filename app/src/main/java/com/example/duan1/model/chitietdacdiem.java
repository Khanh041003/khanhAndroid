package com.example.duan1.model;


public class chitietdacdiem {
    private String name,mota;

    public chitietdacdiem() {
    }

    public chitietdacdiem(String name, String mota) {
        this.name = name;
        this.mota = mota;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }
}
