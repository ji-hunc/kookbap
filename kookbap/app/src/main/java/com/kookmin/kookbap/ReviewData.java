package com.kookmin.kookbap;

import java.util.ArrayList;

public class ReviewData {
    String reviewContext;
    String reviewMenu;
    String reviewRestaurant;
    String reviewReviewerName;
    ArrayList<String> reviewComments;
    ArrayList<String> reviewTags;
    int reviewImage;
    float reviewStars;
    int reviewLikes;

    public ReviewData(String reviewContext, String reviewMenu, String reviewRestaurant, String reviewReviewerName, ArrayList<String> reviewComments, ArrayList<String> reviewTags, int reviewImage, float reviewStars, int reviewLikes) {
        this.reviewContext = reviewContext;
        this.reviewMenu = reviewMenu;
        this.reviewRestaurant = reviewRestaurant;
        this.reviewReviewerName = reviewReviewerName;
        this.reviewComments = reviewComments;
        this.reviewTags = reviewTags;
        this.reviewImage = reviewImage;
        this.reviewStars = reviewStars;
        this.reviewLikes = reviewLikes;
    }

    public String getReviewReviewerName() {
        return reviewReviewerName;
    }

    public void setReviewReviewerName(String reviewReviewerName) {
        this.reviewReviewerName = reviewReviewerName;
    }

    public String getReviewContext() {
        return reviewContext;
    }

    public void setReviewContext(String reviewContext) {
        this.reviewContext = reviewContext;
    }

    public String getReviewMenu() {
        return reviewMenu;
    }

    public void setReviewMenu(String reviewMenu) {
        this.reviewMenu = reviewMenu;
    }

    public String getReviewRestaurant() {
        return reviewRestaurant;
    }

    public void setReviewRestaurant(String reviewRestaurant) {
        this.reviewRestaurant = reviewRestaurant;
    }

    public ArrayList<String> getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(ArrayList<String> reviewComments) {
        this.reviewComments = reviewComments;
    }

    public ArrayList<String> getReviewTags() {
        return reviewTags;
    }

    public void setReviewTags(ArrayList<String> reviewTags) {
        this.reviewTags = reviewTags;
    }

    public int getReviewImage() {
        return reviewImage;
    }

    public void setReviewImage(int reviewImage) {
        this.reviewImage = reviewImage;
    }

    public float getReviewStars() {
        return reviewStars;
    }

    public void setReviewStars(float reviewStars) {
        this.reviewStars = reviewStars;
    }

    public int getReviewLikes() {
        return reviewLikes;
    }

    public void setReviewLikes(int reviewLikes) {
        this.reviewLikes = reviewLikes;
    }
}
