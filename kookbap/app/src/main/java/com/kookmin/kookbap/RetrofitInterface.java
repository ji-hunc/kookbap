package com.kookmin.kookbap;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
    @GET("/review/{menuName}") // 주소를 /review/:menu_name 으로 쿼리 넣어서
    Call<ArrayList<ReviewData>> getReviewData(
            @Path("menuName") String menuName
    );

    // 메뉴이름으로 post 요청 보낼 때
//    @FormUrlEncoded
//    @POST("/review/{menu_name}")
//    Call<Result> saveReview(@Field("menuName") String title,
//               @Field("note") String note);

    // post 요청 테스트
    @FormUrlEncoded
    @POST("/review/post")
    Call<Result> saveReview(
        @Field("reviewUserId") String reviewUserId,
        @Field("menuName") String menuName,
        @Field("writeDate") String writeDate,
        @Field("star") float star,
        @Field("reviewLike") int reviewLike,
        @Field("description") String description,
        @Field("image") String image,
        @Field("restaurantName") String restaurantName
    );
}