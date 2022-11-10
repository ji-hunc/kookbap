package com.kookmin.kookbap;

import org.json.JSONObject;

import java.util.List;

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

    @GET("/")
    Call<JSONObject> getIndex();

    @GET("/menu")
    Call<Object> getMenuData();
}