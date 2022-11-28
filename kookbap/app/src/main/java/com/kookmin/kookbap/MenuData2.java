package com.kookmin.kookbap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuData2 {
    @SerializedName("menu_Id")
    @Expose
    private int menu_Id;

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

    @SerializedName("menu_appearance_id")
    @Expose
    private int menu_appearance_id;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("price")
    @Expose
    private int price;

//    @SerializedName("reviewLikeTrueFalse")
//    @Expose
//    private boolean reviewLikeTrueFalse;
//
//    @SerializedName("subMenuName")
//    @Expose
//    private String subMenuName;
//
//    @SerializedName("image")
//    @Expose
//    private String image;

//    public boolean isReviewLikeTrueFalse() {
//        return reviewLikeTrueFalse;
//    }
//
//    public String getSubMenuName() {
//        return subMenuName;
//    }
//
//    public String getImage() {
//        return image;
//    }

    public int getMenu_Id() {
        return menu_Id;
    }

    public int getPrice() {
        return price;
    }

    public int getMenu_id() {
        return menu_Id;
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

    public int getMenu_appearance_id() {
        return menu_appearance_id;
    }

    public String getDate() {
        return date;
    }
}
