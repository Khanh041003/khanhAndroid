package com.example.duan1.model;

import java.io.Serializable;

public class Users implements Serializable {

    private int id;
    private String name;
    private String email;
    private String sdt;
    private String image;
    private String password;
    private String sex;
    private String dateofbirth;
    private String cccd;
    private String address;


    public Users() {
    }


    public Users(int id, String name, String email, String sdt, String image, String password, String sex, String dateofbirth, String cccd, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.sdt = sdt;
        this.image = image;
        this.password = password;
        this.sex = sex;
        this.dateofbirth = dateofbirth;
        this.cccd = cccd;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



}