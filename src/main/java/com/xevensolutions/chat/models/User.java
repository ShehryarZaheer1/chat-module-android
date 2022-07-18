package com.xevensolutions.chat.models;

import com.google.gson.annotations.SerializedName;

public class User {


    String userName;

    String userProfileImage;

    String authToken;

    public User(int userId, int userRoleId, String userName, String userProfileImage, String authToken) {
        this.userId = userId;
        this.userRoleId = userRoleId;
        this.userName = userName;
        this.userProfileImage = userProfileImage;
        this.authToken = authToken;
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    @SerializedName("userId")
    int userId;

    int userRoleId;


    public int getUserRoleId() {
        return userRoleId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public String getAuthToken() {
        return authToken;
    }
}
