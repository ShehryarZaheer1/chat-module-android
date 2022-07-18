package com.xevensolutions.chat.models;


import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReqMessage<T> {

    @SerializedName("userIds")
    @Expose
    private List<UserId> userIds = null;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("type")
    private String type;

    @SerializedName("dataOject")
    private T data;
    @SerializedName("data")
    private String dataString;

    public List<UserId> getUserIds() {
        return userIds;
    }

    public ReqMessage(String toUserId, String message) {
        this.userIds = new ArrayList<>();
        this.userIds.add(new UserId(toUserId));
        this.message = message;
    }

    public ReqMessage(List<String> userIds, String type, T data) {

        this.userIds = new ArrayList<>();
        for (String id : userIds) {
            this.userIds.add(new UserId(id));
        }

        this.type = type;
        this.data = data;
        this.dataString = new Gson().toJson(data);
        this.message = type;
    }

    public void setUserIds(List<UserId> userIds) {
        this.userIds = userIds;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class UserId {

        public UserId(String id) {
            this.id = id;
        }

        public UserId() {
        }

        @SerializedName("id")
        @Expose
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public String getPushPayload() {
        JSONObject jsonObject = toJsonObject();
        String dataString = new Gson().toJson(data);
        try {
            jsonObject.put("data", dataString);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    public JSONObject toJsonObject() {


        String jsonString = new Gson().toJson(this);
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}