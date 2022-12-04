package com.kookmin.kookbap.LoginAndSignup;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kookmin.kookbap.Retrofits.Result;
import com.kookmin.kookbap.Retrofits.RetrofitClient;

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

    @SerializedName("E-mail")
    @Expose
    String  E_mail;

    private static UserData userData;


    public UserData(String userId){
        this.userId = userId;
        this.nickname = "unknown";
    }

    public static synchronized UserData getUserData(Context context){
        if (userData ==null){
            SharedPreferences userDataPref = context.getSharedPreferences("userData",0);
            String prefUserId = userDataPref.getString("ID","");
            userData = new UserData(prefUserId);
        }
        return userData;
    }
    public String getUserId(){return userId;}

    public String getNickname() {
        return nickname;
    }

    public String getUserE_mail() {return E_mail; }
}
