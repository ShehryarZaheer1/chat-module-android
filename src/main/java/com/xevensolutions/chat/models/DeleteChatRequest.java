package com.xevensolutions.chat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteChatRequest {

    @SerializedName("inboxId")
    @Expose
    private Integer inboxId;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;

    public DeleteChatRequest(Integer inboxId, Integer userId, Boolean isDeleted) {
        this.inboxId = inboxId;
        this.userId = userId;
        this.isDeleted = isDeleted;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}