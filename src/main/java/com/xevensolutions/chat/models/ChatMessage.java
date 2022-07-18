package com.xevensolutions.chat.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xevensolutions.chat.utils.CacheManager;
import com.xevensolutions.chat.utils.FileUtils;


import java.io.Serializable;
import java.util.Objects;

import static com.xevensolutions.chat.utils.Constants.ChatViewTypes.DOCUMENT_MESSAGE;
import static com.xevensolutions.chat.utils.Constants.ChatViewTypes.IMAGE_MESSAGE;
import static com.xevensolutions.chat.utils.Constants.ChatViewTypes.TEXT_MESSAGE;


public class ChatMessage implements Serializable, Cloneable {

    @SerializedName("messageId")
    @Expose
    private Integer conversationId;
    @SerializedName("fromUserId")
    @Expose
    private Integer userId;

    @SerializedName("toUserId")
    private Integer toUserId;
    @SerializedName("message")
    @Expose
    private String mesage;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("toUserProfilePath")
    private String toUserProfilePath;
    @SerializedName("inboxId")
    private Integer inboxId;
    @SerializedName("createdById")
    private Integer createdById;
    @SerializedName("fromUserName")
    private String fromUser;
    @SerializedName("photo")
    private String fromUserProfilePath;
    private String localFilePath;
    @SerializedName("fileURL")
    private String filePath;
    @SerializedName("toUserName")
    private String toUser;
    @SerializedName("toPhoto")
    private String profilePath;
    @SerializedName("badgeCount")
    private Integer badgeCount;

    public ChatMessage() {

    }

    public ChatMessage(int fromUserId, String msg) {
        this.userId = fromUserId;
        this.fromUser = "Test user";
        this.mesage = msg;
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

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }

    public String getToUserProfilePath() {
        return toUserProfilePath;
    }

    public void setToUserProfilePath(final String toUserProfilePath) {
        this.toUserProfilePath = toUserProfilePath;
    }

    public Integer getInboxId() {
        return inboxId;
    }

    public void setInboxId(final Integer inboxId) {
        this.inboxId = inboxId;
    }

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(final Integer createdById) {
        this.createdById = createdById;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(final String fromUser) {
        this.fromUser = fromUser;
    }

    public String getFromUserProfilePath() {
        return fromUserProfilePath;
    }

    public void setFromUserProfilePath(final String fromUserProfilePath) {
        this.fromUserProfilePath = fromUserProfilePath;
    }

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean showAsSent() {
        return userId.equals(CacheManager.getInstance().getCurrentUser().getUserId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(conversationId, that.conversationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conversationId);
    }

    @Override
    public ChatMessage clone() {

        ChatMessage clone;
        try {
            clone = (ChatMessage) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); //should not happen
        }
        return clone;
    }

    public boolean isSent() {
        return conversationId != null && conversationId > 0;
    }

    public String getEitherFilePath() {
        if (localFilePath != null && localFilePath.length() > 0) {
            return localFilePath;

        } else if (filePath != null && filePath.length() > 0) {
            return filePath;
        } else {
            return null;
        }
    }


    public boolean isFileAudio() {
        return FileUtils.isFileAudio(getEitherFilePath());
    }

    public boolean isFileImage() {
        return FileUtils.isFileImage(getEitherFilePath());
    }

    public boolean isFileVideo() {
        return FileUtils.isFileVideo(getEitherFilePath());

    }

    public boolean isFileAttachment() {
        String fileNameTemp = getFileName();
        return FileUtils.isFileDocument(fileNameTemp) || FileUtils.isFileZip(fileNameTemp)
                || FileUtils.isFileImage(fileNameTemp) || FileUtils.isFileAudio(fileNameTemp) ||
                FileUtils.isFileVideo(fileNameTemp);
    }

    public boolean isLocalFile() {
        if (localFilePath != null && localFilePath.length() > 0) {
            return true;
        } else
            return false;
    }

    public String getFileName() {
        return FileUtils.getFileNameFromURL(filePath);
    }

    public boolean isSentByMe() {
        return showAsSent();
    }


    public int getMessageType() {
        int messageType = 0;
        if (isFileAttachment()) {
            if (isFileAudio())
                messageType = DOCUMENT_MESSAGE;
            else if (isFileVideo() || isFileImage())
                messageType = IMAGE_MESSAGE;
            else
                messageType = DOCUMENT_MESSAGE;
        } else
            messageType = TEXT_MESSAGE;
        return messageType;
    }


    public boolean hasQuotedMessage() {
        return false;
    }

    public boolean isFileZip() {
        return FileUtils.isFileZip(getEitherFilePath());
    }
}