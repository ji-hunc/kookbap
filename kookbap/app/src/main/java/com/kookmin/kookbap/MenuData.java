package com.kookmin.kookbap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuData {
    @SerializedName("menu_id")
    @Expose
    private String menu_id;

    @SerializedName("menu_name")
    @Expose
    String menuName;

    String subMenuName;
    String price;
    String reviewText;

    @SerializedName("restaurant_name")
    @Expose
    String restaurantName;
    int image;
    @SerializedName("star_avg")
    @Expose
    float stars;
    @SerializedName("total_like")
    @Expose
    int heart;

    public MenuData(String menuName, String subMenuName, String price, String reviewText, int image, float stars, int heart, String restaurantName) {
        this.menuName = menuName;
        this.subMenuName = subMenuName;
        this.price = price;
        this.reviewText = reviewText;
        this.image = image;
        this.stars = stars;
        this.heart = heart;
        this.restaurantName = restaurantName;
    }

    public MenuData(String a, String subMenuName, int i, String delicious, int ic_setting, float stars, int heart) {
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public String getSubMenuName() {
        return subMenuName;
    }

    public void setSubMenuName(String subMenuName) {
        this.subMenuName = subMenuName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getMenu_id() {
        return menu_id;
    }



}
