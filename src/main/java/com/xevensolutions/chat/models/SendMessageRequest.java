package com.xevensolutions.chat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SendMessageRequest implements Serializable {

    @SerializedName("toUserId")
    @Expose
    private Integer toUserId;
    @SerializedName("postId")
    @Expose
    private Integer postId;
    @SerializedName("inboxId")
    private Integer inboxId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("fileURL")
    private String filePath;
    @SerializedName("fromUserId")
    private Integer fromUserId;
    @SerializedName("fromRoleId")
    private Integer fromRoleId;
    @SerializedName("toRoleId")
    private Integer toRoleId;





    public SendMessageRequest(Integer toUserId, Integer inboxId, String message, String filePath, Integer fromUserId,
                              Integer fromRoleId,Integer toRoleId) {
        this.toUserId = toUserId;
        this.inboxId = inboxId;
        this.message = message;
        this.filePath = filePath;
        this.fromUserId = fromUserId;
        this.fromRoleId = fromRoleId;
        this.toRoleId = toRoleId;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getInboxId() {
        return inboxId;
    }
}