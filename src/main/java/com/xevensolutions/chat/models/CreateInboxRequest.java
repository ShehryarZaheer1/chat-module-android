package com.xevensolutions.chat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateInboxRequest {
    @SerializedName("fromUserId")
    @Expose
    private Integer fromUserId;
    @SerializedName("toUserId")
    @Expose
    private Integer toUserId;
    @SerializedName("fromRoleId")
    @Expose
    private Integer fromRoleId;
    @SerializedName("toRoleId")
    @Expose
    private Integer toRoleId;

    public CreateInboxRequest(Integer fromUserId, Integer toUserId, Integer fromRoleId, Integer toRoleId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.fromRoleId = fromRoleId;
        this.toRoleId = toRoleId;
    }
}
