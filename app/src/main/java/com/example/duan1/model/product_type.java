package com.example.duan1.model;

public class product_type {
    private String typeName;

    public product_type(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return  typeName ;
    }
}
