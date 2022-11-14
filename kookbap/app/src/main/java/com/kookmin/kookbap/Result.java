package com.kookmin.kookbap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// 서버로 POST 했을 때, 통신 결과로 success 와 message 를 받아오는 객체
public class Result{

    @Expose
    @SerializedName("success")
    private Boolean success;
    @Expose
    @SerializedName("message")
    private String message;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}