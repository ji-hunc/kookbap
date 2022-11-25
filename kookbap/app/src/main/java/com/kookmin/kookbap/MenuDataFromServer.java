package com.kookmin.kookbap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuDataFromServer {
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
    private String count_review;

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
    private String price;

//    서버(디비)에 이미지 추가되면 이미지도 받아와야 함.
//    @SerializedName("image")
//    @Expose
//    private int image;

    public int get_menu_Id(){
        return menu_Id;
    }
    public String get_restaurant_name(){
        return restaurant_name;
    }
    public String get_menu_name(){
        return menu_name;
    }
    public String get_count_review(){
        if (count_review == null){
            return ("아직 작성된 리뷰가 없습니다");
        }
        return count_review+"건의 리뷰가 있습니다";
    }
    public float get_star_avg(){
        return star_avg;
    }
    public int get_total_like(){
        return total_like;
    }
    public int get_menu_appearance_id(){
        return menu_appearance_id;
    }
    public String get_date(){
        return date;
    }
    public String get_price(){
        int r = price.length() % 3;
        price = price.substring(0, r) + "," + price.substring(r);
        return price;
    }

//    public int get_image(){
//        if (image == 0){
//            return (R.drawable.ic_spoon);
//        }
//        else{
//            return image;
//        }
//    }


}