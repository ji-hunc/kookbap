package com.kookmin.kookbap;

// bestReviewer class
public class BestReviewerData {

    private int rank;
    private String name;
    private int count;

    public BestReviewerData(int rank, String name, int count){
        this.rank= rank;
        this.name = name;
        this.count = count;
    }
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
