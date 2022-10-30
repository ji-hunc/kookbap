package com.kookmin.kookbap;

public class ReviewData {
    String menuName;
    String price;
    String reviewText;
    int image;
    float stars;
    int heart;

    public ReviewData(String menuName, String price, String reviewText, int image, float stars, int heart) {
        this.menuName = menuName;
        this.price = price;
        this.reviewText = reviewText;
        this.image = image;
        this.stars = stars;
        this.heart = heart;
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
}
