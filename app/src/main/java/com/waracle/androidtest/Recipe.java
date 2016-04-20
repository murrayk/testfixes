package com.waracle.androidtest;


/**
 * This class is a Value Object containing the Recipe
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
