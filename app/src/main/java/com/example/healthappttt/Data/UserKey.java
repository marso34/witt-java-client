package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;
public class UserKey {
    @SerializedName("userkey")
    String userkey;

    public UserKey(String userkey) {
        this.userkey = userkey;
    }


//    public String getKey() {
//        return userkey;
//    }

}
