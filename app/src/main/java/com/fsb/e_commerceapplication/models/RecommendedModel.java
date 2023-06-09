package com.fsb.e_commerceapplication.models;

public class RecommendedModel {
    String name ;
    String Description;
    int price ;
    String rating ;
    String img_url ;

    public RecommendedModel() {
    }

    public RecommendedModel(String name, String description, int price, String rating, String img_url) {
        this.name = name;
        Description = description;
        this.price = price;
        this.rating = rating;
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }



}
