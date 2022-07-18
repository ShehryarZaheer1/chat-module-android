package com.xevensolutions.chat.models;

public class ReqArchive {
    Integer inboxId;
    Integer userId;
    Boolean isArchive;

    public ReqArchive(Integer inboxId, Integer userId, Boolean isArchive) {
        this.inboxId = inboxId;
        this.userId = userId;
        this.isArchive = isArchive;
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

    public Boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(Boolean isArchive) {
        this.isArchive = isArchive;
    }
}