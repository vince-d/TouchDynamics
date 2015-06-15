package com.teco.vindi.touchdynamics;

public class ScrollViewData {

    private String title;
    private int imageUrl;

    public ScrollViewData(String title,int imageUrl) {

        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return  this.title;
    }

    public int getImageUrl() {
        return this.imageUrl;
    }
}
