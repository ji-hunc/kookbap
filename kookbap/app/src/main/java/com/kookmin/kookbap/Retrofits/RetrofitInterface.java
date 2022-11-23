package com.kookmin.kookbap.Retrofits;

import com.kookmin.kookbap.MenuData;
import com.kookmin.kookbap.ReviewData;
import com.kookmin.kookbap.ReviewRank.UserRankData;
import com.kookmin.kookbap.testRecommendMenuData;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {

    // index 페이지 들어갔을 때.
    // http://kookbap.run.goorm.io/
    // http://10.0.2.2:3000/
    @GET("/")
    Call<JSONObject> getIndex();


    // menu 페이지 들어갔을 때.
    // http://kookbap.run.goorm.io/menu
    // http://10.0.2.2:3000/menu"
    @GET("/menu")
    Call<Object> getMenuData();


    // review 페이지에 메뉴 이름으로 들어갔을 때
    // http://kookbap.run.goorm.io/review/김치찌개
    // http://10.0.2.2:3000/review/김치찌개
    // 쿼리로 어떻게 정렬할 것인지를 받아옴
    @GET("/review/{menuName}") // 주소를 /review/:menu_name 으로 쿼리 넣어서
    Call<ArrayList<ReviewData>> getReviewData(
            @Path("menuName") String menuName,
            @Query("orderBy") String orderBy
    );

    // recommendMenu/users 페이지에 유저 이름으로 들어갔을 때.
    // http://kookbap.run.goorm.io/recommendMenu/jihun
    // http://10.0.2.2:3000/recommendMenu/jihun"
    @GET("/recommendMenu/{userName}")
    Call<ArrayList<testRecommendMenuData>> getRecommendMenuData(
            @Path("userName") String userName
    );


    // review/users 페이지에 유저 이름으로 들어갔을 때
    // http://kookbap.run.goorm.io/review/users/jihun
    // http://10.0.2.2:3000/review/users/jihun
    @GET("/review/users/{userName}") // 주소를 /review/users/:userName 으로 쿼리 넣어서
    Call<ArrayList<ReviewData>> getUserReviewData(
            @Path("userName") String userName
    );


    // review 업로드. 이미지와 다른 변수들을 같이 보내려면 다른 변수들은 HashMap으로 묶어서 보내야만 함.
    // http://kookbap.run.goorm.io/review/post
    // http://10.0.2.2:3000/review/post
    @Multipart
    @POST("/review/post")
    Call<Result> uploadFileWithPartMap(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);


    // review 수정. 이미지와 다른 변수들을 같이 보내려면 다른 변수들은 HashMap으로 묶어서 보내야만 함.
    // http://kookbap.run.goorm.io/review/modify
    // http://10.0.2.2:3000/review/modify
    @Multipart
    @POST("/review/modify")
    Call<Result> modifyReview(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);


    // review 삭제. reviewNumber 보내주면, 서버에서 알아서 지워줌
    // http://kookbap.run.goorm.io/review/delete
    // http://10.0.2.2:3000/review/delete
    @FormUrlEncoded
    @POST("/review/delete")
    Call<Result> deleteReview(
            @Field("reviewNumber") int reviewNumber
    );


    //Rank 관련 함수
    //review와 관련된 순위
    @GET("/rank/review/total_review")
    Call<ArrayList<UserRankData>> getUserReviewRankData(
            //데이터 갯수제한
            @Query("endR") int endR
    );


    @GET("/rank/menu/{category}")
    Call<ArrayList<MenuData>> getMenuReviewRankData(
            //review_like or total_review
            @Path("category") String category,
            //데이터 갯수 제한
            @Query("endR") int endR

    );

    //제네릭타입으로 만들어서 리팩토링할때 쓸 주소
    @GET("/rank/{section}}/{category}")
    Call<ArrayList<MenuData>> getRankData(
            //
            @Path("section") String section,
            //review_like or total_review
            @Path("category") String category,
            //데이터 갯수 제한
            @Query("endR") int endR

    );

//    @Multipart
////    @FormUrlEncoded
//    @POST("/review/post")
//    Call<Result> saveReview(
//            @Part("reviewUserId") String reviewUserId,
//            @Part("menuName") String menuName,
//            @Part("writeDate") String writeDate,
//            @Part("star") float star,
//            @Part("reviewLike") int reviewLike,
//            @Part("description") String description,
//            @Part("image") String image,
//            @Part("restaurantName") String restaurantName,
//            @Part("image")MultipartBody.Part file
//            );

}