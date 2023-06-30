package com.example.duan1.model;

public class thoiTrangNews {
    private int id;
    private String title;
    private String description;
    private String typeproduct;
    private double price;
    private String address;
    private int idUser;
    private String nameUser;
    private String tenDanhMuc;
    private String date;
    private String image;

    public thoiTrangNews() {
    }

    public thoiTrangNews(int id, String title, String description, String typeproduct, double price,
                         String address, int idUser, String nameUser, String tenDanhMuc, String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.typeproduct = typeproduct;
        this.price = price;
        this.address = address;
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.tenDanhMuc = tenDanhMuc;
        this.date = date;
    }

    //    public thoiTrangNews(int id, String title, String description, String typeproduct
//            , double price, String address, int idUser, String nameUser, String tenDanhMuc , String date) {
//        this.id = id;
//        this.title = title;
//        this.description = description;
//        this.typeproduct = typeproduct;
//        this.price = price;
//        this.address = address;
//        this.idUser = idUser;
//        this.nameUser = nameUser;
//        this.tenDanhMuc = tenDanhMuc;
//        this.date = date;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeproduct() {
        return typeproduct;
    }

    public void setTypeproduct(String typeproduct) {
        this.typeproduct = typeproduct;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
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
}
