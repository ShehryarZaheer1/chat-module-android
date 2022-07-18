package com.xevensolutions.chat.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;


import com.xevensolutions.baseapp.MyBaseApp;
import com.xevensolutions.baseapp.interfaces.ApiCallback;
import com.xevensolutions.baseapp.models.BaseResponse;
import com.xevensolutions.baseapp.utils.BlobImageUploadListener;
import com.xevensolutions.baseapp.utils.BlobImageUploader;
import com.xevensolutions.baseapp.utils.DateUtils;
import com.xevensolutions.baseapp.viewmodels.BaseViewModel;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.models.ChatItem;
import com.xevensolutions.chat.models.ChatMessage;
import com.xevensolutions.chat.models.SendMessageRequest;
import com.xevensolutions.chat.presenters.ChatView;
import com.xevensolutions.chat.repos.RepoCentral;
import com.xevensolutions.chat.socket.MySocket;
import com.xevensolutions.chat.utils.CacheManager;
import com.xevensolutions.chat.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.xevensolutions.baseapp.utils.CollectionUtils.isEmpty;
import static com.xevensolutions.baseapp.utils.TextUtils.isStringEmpty;

public class VmChat extends BaseViewModel {


    RepoCentral repoChat;

    ChatItem chatItem;

    ChatView chatView;

    MutableLiveData<List<ChatMessage>> chatMessagesResponseMutableLiveData;
    private int pageNo;
    private String cameraImagePath;

    public VmChat() {
        this.repoChat = new RepoCentral(this);
        chatMessagesResponseMutableLiveData = new MutableLiveData<>();
        if (pageNo < 1)
            pageNo = 1;
    }

    @Override
    public Context getContext() {
        return MyBaseApp.getContext();
    }

    public void setChatView(final ChatView chatView) {
        this.chatView = chatView;
    }

    public ChatItem getChatItem() {
        return chatItem;
    }

    public int getPageNo() {
        return pageNo;
    }

    public MutableLiveData<List<ChatMessage>> getChatMessagesResponseMutableLiveData() {
        return chatMessagesResponseMutableLiveData;
    }

    public void setChatItem(final ChatItem chatItem) {
        this.chatItem = chatItem;
    }


    public void sendMessage(String message, String fileUrl) {
        if (isStringEmpty(message) && isStringEmpty(fileUrl)) {
            chatView.onMsgMissing();
            return;
        }
        baseFragmentView.showLoading(context.getString(R.string.sending_message));
        repoChat.sendMessage(new SendMessageRequest(chatItem.getUserId(),
                        chatItem.getInboxId(), message, fileUrl,
                        CacheManager.getInstance().getCurrentUser().getUserId(),
                        CacheManager.getInstance().getCurrentUser().getUserRoleId(),chatItem.getRoleId()),
                new ApiCallback<BaseResponse<ChatMessage>>(this) {

                    @Override
                    public void onSuccess(BaseResponse<ChatMessage> data, String msg, int status) {
                        super.onSuccess(data, msg, status);
                        addMessage(data.getData());

                        MySocket.getInstance().sendMessage(Constants.SignalTypes.NEW_MESSAGE,
                                chatItem.getToUserId() + "", data.getData());
                    }


                });
    }

    public void markAsRead(int userId, int inboxId) {

        repoChat.markAsRead(userId, inboxId, new ApiCallback<BaseResponse<Object>>(this) {
            @Override
            public void onSuccess(BaseResponse<Object> data, String msg, int status) {
                super.onSuccess(data, msg, status);

            }

            @Override
            public void onFailure(String msg, Integer code) {
                super.onFailure(msg, code);
            }
        });
    }

    private void addMessage(ChatMessage data) {
        List<ChatMessage> messages = null;
        if (chatMessagesResponseMutableLiveData.getValue() != null)
            messages = chatMessagesResponseMutableLiveData.getValue();
        if (messages == null)
            messages = new ArrayList<>();

        if (messages.contains(data))
            return;

        messages.add(0, data);


        chatMessagesResponseMutableLiveData.setValue(messages);
    }

   /* public void chatAddToFav(ChatItem chatItem) {
        int inboxId = chatItem.getInboxId();
        repoChat.chatAddToFav(inboxId, new ApiCallback<BaseResponse<Object>>(this) {
            @Override
            public void onSuccess(BaseResponse<Object> data) {
                chatItem.setFavorite(!chatItem.isFavorite());
                chatView.onAddToFavSuccess(chatItem);
            }
        });

    }*/

    public void onMessageReceived(ChatMessage chatMessage) {
        if (chatItem.getInboxId().equals(chatMessage.getInboxId())) {
            addMessage(chatMessage);
        }
    }

    public void sendFile(String path) {
        baseFragmentView.showLoading("");
        new BlobImageUploader(path, new BlobImageUploadListener() {
            @Override
            public void onCompletion(boolean isSuccess, String blobName, String blobPath) {
                baseFragmentView.dismissLoading();
                if (isSuccess) {
                    sendMessage(null, blobPath);
                }
                ;
            }
        }).execute();
    }

    public void loadMessages() {

        if (pageNo == 1)
            chatView.showLoading("");
        repoChat.getChatMessages(pageNo, chatItem.getInboxId(), new ApiCallback<BaseResponse<List<ChatMessage>>>(this) {
            @Override
            public void onSuccess(BaseResponse<List<ChatMessage>> data, String msg, int status) {
                super.onSuccess(data, msg, status);
                chatView.dismissLoading();
                if (isEmpty(data.getData()))
                    return;
                List<ChatMessage> chatMessages = chatMessagesResponseMutableLiveData.getValue();
                if (chatMessages == null)
                    chatMessages = new ArrayList<>();
                chatMessages.addAll(data.getData());
                chatMessagesResponseMutableLiveData.setValue(chatMessages);
            }
        });
    }


    public void loadNextPage() {
        pageNo = pageNo + 1;
        loadMessages();

    }

    public void setCameraImagePath(String path) {
        this.cameraImagePath = path;
    }

    public String getCameraImagePath() {
        return cameraImagePath;
    }
}
