package com.example.duan1.model;

public class giaiTriNews {
    private int id;
    private String title;
    private String description;
    private String address;
    private double price;
    private String typeProduct;
    private int idUser;
    private String nameUser;
    private String tenDanhMuc;
    private String danhmucchinh;
    private String date;
    private String image;

    public giaiTriNews() {
    }

    public giaiTriNews(int id, String title, String description, String address, double price, String typeProduct, int idUser, String nameUser, String tenDanhMuc, String danhmucchinh, String date, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.address = address;
        this.price = price;
        this.typeProduct = typeProduct;
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.tenDanhMuc = tenDanhMuc;
        this.danhmucchinh = danhmucchinh;
        this.date = date;
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTypeProduct() {
        return typeProduct;
    }

    public void setTypeProduct(String typeProduct) {
        this.typeProduct = typeProduct;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDanhmucchinh() {
        return danhmucchinh;
    }

    public void setDanhmucchinh(String danhmucchinh) {
        this.danhmucchinh = danhmucchinh;
    }
}
