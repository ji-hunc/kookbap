package com.kookmin.kookbap.LoginAndSignup;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kookmin.kookbap.Retrofits.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserData {
    @SerializedName("user_id")
    @Expose
    String userId;

    @SerializedName("nickname")
    @Expose
    String nickname;

    @SerializedName("E_mail")
    @Expose
    String  E_mail;

    private static UserData userData;


    public UserData(String userId, String Email){
        this.userId = userId;
        this.E_mail = Email;
        this.nickname = "unknown";
    }

    public static synchronized UserData getUserData(Context context){
        if (userData ==null){
            SharedPreferences userDataPref = context.getSharedPreferences("userData",0);
            String prefUserId = userDataPref.getString("ID","");
            String prefUserEmail = userDataPref.getString("Email","");
            userData = new UserData(prefUserId, prefUserEmail);

            // 앱 실행 시 프래그먼트에 저장된 user_id를 통해서 db에서 닉네임, 이메일을 받아옴.
            Call <ArrayList<UserData>> callUserData = RetrofitClient.getApiService().getUserInfo(prefUserId);
            callUserData.enqueue(new Callback<ArrayList<UserData>>() {
                @Override
                public void onResponse(Call<ArrayList<UserData>> call, Response<ArrayList<UserData>> response) {
                    if (response.code() == 200) {
                        userData = response.body().get(0);
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<UserData>> call, Throwable t) {

                }
            });

        }
        return userData;
    }
    public String getUserId(){return userId;}

    public String getNickname() {
        return nickname;
    }

    public String getUserE_mail() {return E_mail; }
}
