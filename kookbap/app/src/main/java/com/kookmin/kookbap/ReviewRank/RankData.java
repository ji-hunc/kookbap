package com.kookmin.kookbap.ReviewRank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RankData {
    public RankData(String user_id, String nickname, String total){
        this.nickname=nickname;
        this.user_id=user_id;
        this.total=total;
    }

    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("total")
    @Expose
    private String total;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
