package com.example.duan1.model;

public class direction {
    private String direction;

    public direction(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return direction ;
    }
}
