package com.kookmin.kookbap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewData {

    @SerializedName("review_number")
    @Expose
    private int review_number;

    @SerializedName("review_user_id")
    @Expose
    private String review_user_id;

    @SerializedName("review_menu_id_reviewd")
    @Expose
    private int review_menu_id_reviewd;

    @SerializedName("menu_name")
    @Expose
    private String menu_name;

    @SerializedName("write_date")
    @Expose
    private Object write_date;

    @SerializedName("star")
    @Expose
    private float star;

    @SerializedName("review_like")
    @Expose
    private int review_like;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("image")
    @Expose
    private String image;

    public int getReview_number() {
        return review_number;
    }

    public String getReview_user_id() {
        return review_user_id;
    }

    public int getReview_menu_id_reviewd() {
        return review_menu_id_reviewd;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public Object getWrite_date() {
        return write_date;
    }

    public float getStar() {
        return star;
    }

    public int getReview_like() {
        return review_like;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}