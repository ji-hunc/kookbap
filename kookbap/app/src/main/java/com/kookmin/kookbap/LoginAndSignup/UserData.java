package com.kookmin.kookbap.LoginAndSignup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("restaurant_name")
    @Expose
    String mUserName;
    @SerializedName("restaurant_name")
    @Expose
    String  mUserAderss;


    public UserData(String userAderss, String userName) {
        this.mUserAderss = userAderss;
        this.mUserName = userName;
    }


    public String getmUserName() {
        return mUserName;
    }

    public String getmUserAderss() {return mUserAderss; }

}
