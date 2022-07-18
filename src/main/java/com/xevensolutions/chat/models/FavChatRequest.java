package com.xevensolutions.chat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavChatRequest {

    @SerializedName("inboxId")
    @Expose
    private Integer inboxId;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("isFavriote")
    @Expose
    private Boolean isFavriote;

    public FavChatRequest(Integer inboxId, Integer userId, Boolean isFavriote) {
        this.inboxId = inboxId;
        this.userId = userId;
        this.isFavriote = isFavriote;
    }

    public Integer getInboxId() {
        return inboxId;
    }

    public void setInboxId(Integer inboxId) {
        this.inboxId = inboxId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getIsFavriote() {
        return isFavriote;
    }

    public void setIsFavriote(Boolean isFavriote) {
        this.isFavriote = isFavriote;
    }

}