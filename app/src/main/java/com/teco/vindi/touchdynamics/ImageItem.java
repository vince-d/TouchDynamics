package com.teco.vindi.touchdynamics;

import android.graphics.Bitmap;
import android.graphics.Color;


public class ImageItem {
    private Bitmap image;
    private String title;
    private int color;

    public ImageItem(Bitmap image, String title, int color) {
        super();
        this.image = image;
        this.title = title;
        this.color = color;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }
}