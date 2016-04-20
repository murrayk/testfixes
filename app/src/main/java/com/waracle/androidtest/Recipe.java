package com.waracle.androidtest;

/**
 * Created by murray on 20/04/2016.
 */
public class Recipe {
    private String title;
    private String desc;
    private String imageUrl;

    public Recipe(String title, String desc, String imageUrl) {
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
