package com.xevensolutions.chat.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xevensolutions.baseapp.utils.DateUtils;

import java.util.Calendar;

public class ChatItem implements Parcelable {

    @SerializedName("postId")
    @Expose
    private int postId;

    @SerializedName("inboxId")
    @Expose
    private Integer inboxId;
    @SerializedName("toUserName")
    @Expose
    private String toUser;
    @SerializedName("photo")
    @Expose
    private String toUserPhoto;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("createdDate")
    @Expose
    private String createdDateTime;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("toUserId")
    private Integer toUserId;
    @SerializedName("unreadCount")
    private Integer unread;

    @SerializedName("isFavriote")
    private boolean isFavorite;
    @SerializedName("propertyName")
    private String propertyName;
    @SerializedName("isVerified")
    private boolean isVerified;
    @SerializedName("isArchive")
    private boolean isArchived;
    @SerializedName("fileUrl")
    private String filePath;
    @SerializedName("isDeleted")
    private boolean isDeleted;
    @SerializedName("profilePath")
    private String profilePath;
    @SerializedName("toUserProfilePath")
    private String toUserProfilePath;
    @SerializedName("badgeCount")
    private Integer badgeCount;
    @SerializedName("roleId")
    private Integer roleId;
    @SerializedName("userId")
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public Integer getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(Integer badgeCount) {
        this.badgeCount = badgeCount;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getToUserProfilePath() {
        return toUserProfilePath;
    }

    public void setToUserProfilePath(String toUserProfilePath) {
        this.toUserProfilePath = toUserProfilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(final boolean verified) {
        isVerified = verified;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(final String propertyName) {
        this.propertyName = propertyName;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(final boolean favorite) {
        isFavorite = favorite;
    }

    public Integer getUnread() {
        return unread;
    }

    public void setUnread(final Integer unread) {
        this.unread = unread;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(final Integer toUserId) {
        this.toUserId = toUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public void setPostId(final int postId) {
        this.postId = postId;
    }

    public int getPostId() {
        return postId;
    }

    public Integer getInboxId() {
        return inboxId;
    }

    public void setInboxId(Integer inboxId) {
        this.inboxId = inboxId;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getToUserPhoto() {
        return toUserPhoto;
    }

    public void setToUserPhoto(String toUserPhoto) {
        this.toUserPhoto = toUserPhoto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeValue(this.inboxId);
            dest.writeString(this.toUser);
            dest.writeString(this.toUserPhoto);
            dest.writeString(this.message);
            dest.writeString(this.createdDateTime);
            dest.writeString(this.userName);
            dest.writeString(this.profilePath);
            dest.writeString(this.toUserProfilePath);
            dest.writeInt(postId);
            dest.writeInt(badgeCount);
            dest.writeInt(toUserId);
            dest.writeString(toUser);
            dest.writeInt(isFavorite ? 1 : 0);
            dest.writeString(propertyName);
            dest.writeInt(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ChatItem() {
        this.toUser = "";
        this.toUserId = 0;
        this.toUserPhoto = "";
        this.message = "Last message";
        this.createdDateTime = DateUtils.getFormattedDateTime(Calendar.getInstance());
        this.userName = "Coach";
        this.profilePath = "";
        this.toUserProfilePath = "";
        this.postId = 1;
        this.badgeCount = 0;
        this.isFavorite = false;
        this.propertyName = "";
        this.inboxId = 1;

    }

    public ChatItem(int inboxId, int postId) {
        this();
        this.inboxId = inboxId;
        this.postId = postId;
    }

    public ChatItem(ChatMessage chatMessage) {
        this();
        this.inboxId = chatMessage.getInboxId();
        this.userName = chatMessage.getFromUser();
        this.toUserId = chatMessage.getUserId();
        this.toUserPhoto = chatMessage.getFromUserProfilePath();
        this.createdDateTime = chatMessage.getCreatedDate();
        this.userName = chatMessage.getFromUser();
        this.profilePath = chatMessage.getProfilePath();
        this.badgeCount = chatMessage.getBadgeCount();
        this.toUserProfilePath = chatMessage.getToUserProfilePath();


    }

    protected ChatItem(Parcel in) {
        this.inboxId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.toUser = in.readString();
        this.toUserPhoto = in.readString();
        this.message = in.readString();
        this.createdDateTime = in.readString();
        this.userName = in.readString();
        this.profilePath = in.readString();
        this.toUserProfilePath = in.readString();
        this.postId = in.readInt();
        this.badgeCount = in.readInt();
        toUserId = in.readInt();
        toUser = in.readString();
        isFavorite = in.readInt() == 1;
        propertyName = in.readString();
        userId = in.readInt();

    }

    public static final Parcelable.Creator<ChatItem> CREATOR = new Parcelable.Creator<ChatItem>() {
        @Override
        public ChatItem createFromParcel(Parcel source) {
            return new ChatItem(source);
        }

        @Override
        public ChatItem[] newArray(int size) {
            return new ChatItem[size];
        }
    };

    @Override
    public String toString() {
        return message + " " + userName + " " + toUser;
    }
}