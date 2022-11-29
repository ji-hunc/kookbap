package com.kookmin.kookbap.LoginAndSignup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("user_Name")
    @Expose
    private String userName;

    @SerializedName("user_Adress")
    @Expose
    private String userAdress;

    public UserData (String username , String useradress){
        this.userAdress = useradress;
        this.userName = username;
    }
    public String getUserName(){
        return userName;
    }
    public String getUserAdress(){
        return userAdress;
    }
}
