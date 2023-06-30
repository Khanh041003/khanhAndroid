package com.example.duan1.model;

public class historyNews {
    private int id;
    private String title_historyNews;
    private String desc_historyNews;
    private String time_historyNews;
    private String tenDanhMuc;
    private String image;

    public historyNews(int id, String title_historyNews, String desc_historyNews, String time_historyNews, String tenDanhMuc, String image) {
        this.id = id;
        this.title_historyNews = title_historyNews;
        this.desc_historyNews = desc_historyNews;
        this.time_historyNews = time_historyNews;
        this.tenDanhMuc = tenDanhMuc;
        this.image = image;
    }

//    public historyNews(int id, String title_historyNews, String desc_historyNews, String time_historyNews, String tenDanhMuc) {
//        this.id = id;
//        this.title_historyNews = title_historyNews;
//        this.desc_historyNews = desc_historyNews;
//        this.time_historyNews = time_historyNews;
//        this.tenDanhMuc = tenDanhMuc;
//    }

    public historyNews() {
    }

    public String getTime_historyNews() {
        return time_historyNews;
    }

    public void setTime_historyNews(String time_historyNews) {
        this.time_historyNews = time_historyNews;
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

    public String getTitle_historyNews() {
        return title_historyNews;
    }

    public void setTitle_historyNews(String title_historyNews) {
        this.title_historyNews = title_historyNews;
    }

    public String getDesc_historyNews() {
        return desc_historyNews;
    }

    public void setDesc_historyNews(String desc_historyNews) {
        this.desc_historyNews = desc_historyNews;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
