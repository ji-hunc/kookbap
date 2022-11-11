package com.kookmin.kookbap;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {

//    @GET("info/{UserId}")
//    Call<DataModel> test_my_server(
//            @Path("UserId") String userid);
//
//    @GET("/")
//    Call<List<ReviewModel>> test_my_server_with_android();
//
//    @FormUrlEncoded
//    @POST("/review")
//    Call<Note> saveNote(
//            @Field("title") String title,
//            @Field("note") String note
//    );


    // index 페이지 들어갔을 때. http://kookbap.run.goorm.io/
    // http://10.0.2.2:3000/";
    @GET("/")
    Call<JSONObject> getIndex();

    // menu 페이지 들어갔을 때. http://kookbap.run.goorm.io/menu
    // http://10.0.2.2:3000/menu";
    @GET("/menu")
    Call<Object> getMenuData();

    // review 페이지 들어갔을 때. http://kookbap.run.goorm.io/review
    // http://10.0.2.2:3000/review";
    @GET("/review") // 주소를 /review/:menu_name 으로 쿼리 넣어서
    Call<ArrayList<ReviewData>> getReviewData();
}