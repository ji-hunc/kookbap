package com.kookmin.kookbap.Retrofits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kookmin.kookbap.URLConnector;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = URLConnector.URL;


    public static RetrofitInterface getApiService() {
        return getInstance().create(RetrofitInterface.class);
    }

    private static Retrofit getInstance(){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}