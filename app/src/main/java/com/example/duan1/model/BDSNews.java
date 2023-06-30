package com.example.duan1.model;

public class BDSNews {
    private int id;
    private String title;
    private String description;
    private double price;
    private String dienTich ;
    private String adress;
    private String tenPhanKhu;
    private String loaiDat;
    private String direction;
    private String soPhongNgu;
    private String soPhongWc;
    private int idUser;
    private String nameUser;
    private String tenDanhMuc;
    private String danhMucChinh;
    private String date;
    private String image;

    public BDSNews() {
    }

    public BDSNews(int maxID, String strTitlePost, String strDescription, double dbPrice, String strDienTich,
                   String strAddress, String strTenKhu, String strLoaiHinhDat,
                   String strSoPhongNgu, String strSoPhongWc, int idUser, String nameUser , String tenDanhMuc, String danhMucChinh, String date ) {
        this.id = maxID;
        this.title = strTitlePost;
        this.description = strDescription;
        this.price = dbPrice;
        this.dienTich = strDienTich;
        this.adress = strAddress;
        this.tenPhanKhu = strTenKhu;
        this.loaiDat = strLoaiHinhDat;
        this.soPhongNgu = strSoPhongNgu;
        this.soPhongWc = strSoPhongWc;
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.tenDanhMuc = tenDanhMuc;
        this.danhMucChinh = danhMucChinh;
        this.date = date;
    }


    public BDSNews(int id, String title, String description, double price,
                   String dienTich, String adress
                  , int idUser, String nameUser , String tenDanhMuc, String date
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.dienTich = dienTich;
        this.adress = adress;
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.tenDanhMuc = tenDanhMuc;
        this.date = date;
    }

    public BDSNews(int id, String title, String description, double price,
                   String dienTich, String adress, String tenPhanKhu, String loaiDat, String direction
            , int idUser, String nameUser,String tenDanhMuc, String date ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.dienTich = dienTich;
        this.adress = adress;
        this.tenPhanKhu = tenPhanKhu;
        this.loaiDat = loaiDat;
        this.direction = direction;
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.tenDanhMuc = tenDanhMuc;
        this.date = date;
    }

    public BDSNews(String date, int idUser, double dbPrice, String strDescription, String strLoaiHinhDat, String strTitlePost, String strSoPhongWc, String strTenKhu, String strTenKhu1, String bds, String strDienTich, String nameUser, String strSoPhongNgu, String tenDanhMuc, int maxID) {

    }

    public BDSNews(int idEdit, String strTitlePost, int idUser, String bds, String strAddress, String date, String date1, String strDescription, String tenDanhMuc, String strDienTich, String strSoPhongNgu, String nameUser, String strSoPhongWc, String strLoaiHinhDat, String strTenKhu) {
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

    public String getSoPhongNgu() {
        return soPhongNgu;
    }

    public void setSoPhongNgu(String soPhongNgu) {
        this.soPhongNgu = soPhongNgu;
    }

    public String getSoPhongWc() {
        return soPhongWc;
    }

    public void setSoPhongWc(String soPhongWc) {
        this.soPhongWc = soPhongWc;
    }

    public String getTenPhanKhu() {
        return tenPhanKhu;
    }

    public void setTenPhanKhu(String tenPhanKhu) {
        this.tenPhanKhu = tenPhanKhu;
    }

    public String getLoaiDat() {
        return loaiDat;
    }

    public void setLoaiDat(String loaiDat) {
        this.loaiDat = loaiDat;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDienTich() {
        return dienTich;
    }

    public void setDienTich(String dienTich) {
        this.dienTich = dienTich;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDanhMucChinh() {
        return danhMucChinh;
    }

    public void setDanhMucChinh(String danhMucChinh) {
        this.danhMucChinh = danhMucChinh;
    }
}
