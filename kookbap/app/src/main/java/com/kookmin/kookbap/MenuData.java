package com.kookmin.kookbap;

public class MenuData {
    String menuName;
    String subMenuName;
    String price;
    String reviewText;
    String restaurantName;
    int image;
    float stars;
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
}
