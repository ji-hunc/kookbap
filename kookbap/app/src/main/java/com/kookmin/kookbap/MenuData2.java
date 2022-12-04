package com.kookmin.kookbap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuData2 {
    @SerializedName("menu_id")
    @Expose
    private int menu_id;

    @SerializedName("restaurant_name")
    @Expose
    private String restaurant_name;

    @SerializedName("menu_name")
    @Expose
    private String menu_name;

    @SerializedName("count_review")
    @Expose
    private int count_review;

    @SerializedName("star_avg")
    @Expose
    private float star_avg;

    @SerializedName("total_like")
    @Expose
    private int total_like;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("subMenu")
    @Expose
    private String subMenu;

    @SerializedName("menu_like_id")
    @Expose
    private int menu_like_id;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("Mliked_user_id")
    @Expose
    private String Mliked_user_id;

    @SerializedName("userLikeTrueFalse")
    @Expose
    private int userLikeTrueFalse;


    public String getSubMenu() {
        return subMenu;
    }

    public String getImage() {
        return image;
    }

    public int isUserLikeTrueFalse() {
        return userLikeTrueFalse;
    }

    public int getPrice() {
        return price;
    }

    public int getMenu_id() {
        return menu_id;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public int getCount_review() {
        return count_review;
    }

    public float getStar_avg() {
        return star_avg;
    }

    public int getTotal_like() {
        return total_like;
    }

    public int getMenu_like_id() {
        return menu_like_id;
    }

    public String getMliked_user_id() {
        return Mliked_user_id;
    }

    public String getDate() {
        return date;
    }
}
