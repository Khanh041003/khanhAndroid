package com.example.duan1.model;

public class NewsTrangChu {
    private String title,descripsion, time ;
    private double fee;
    private boolean favorite;
    private String image;
    private String tenDanhMuc;

//    public int getsoluonganh(){
//        return getArrURL().size();
//    }âssaassasasasaas

    public NewsTrangChu() {
    }


    public NewsTrangChu(String title, String descripsion, String time, double fee, boolean favorite,
                        String image, String tenDanhMuc) {
        this.title = title;
        this.descripsion = descripsion;
        this.time = time;
        this.fee = fee;
        this.favorite = favorite;
        this.image = image;
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescripsion() {
        return descripsion;
    }

    public void setDescripsion(String descripsion) {
        this.descripsion = descripsion;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
