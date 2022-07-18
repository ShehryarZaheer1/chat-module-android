package com.xevensolutions.chat.viewmodels;

import android.content.Context;

import androidx.lifecycle.GeneratedAdapter;
import androidx.lifecycle.MutableLiveData;

import com.xevensolutions.baseapp.MyBaseApp;
import com.xevensolutions.baseapp.interfaces.ApiCallback;
import com.xevensolutions.baseapp.models.BaseResponse;
import com.xevensolutions.baseapp.presenters.BaseFragmentView;
import com.xevensolutions.baseapp.utils.GenericUtils;
import com.xevensolutions.baseapp.viewmodels.BaseViewModel;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.models.ChatItem;
import com.xevensolutions.chat.models.CreateInboxRequest;
import com.xevensolutions.chat.models.User;
import com.xevensolutions.chat.presenters.InboxView;
import com.xevensolutions.chat.repos.RepoCentral;
import com.xevensolutions.chat.utils.CacheManager;

import java.util.ArrayList;
import java.util.List;

public class VmInbox extends BaseViewModel {

    InboxView inboxView;
    RepoCentral repoCentral;
    MutableLiveData<List<ChatItem>> chatMutableLiveData;
    int pageNo;
    int lastResultsPageNo;


    @Override
    public Context getContext() {
        return MyBaseApp.getContext();
    }

    public void setInboxView(InboxView inboxView) {
        this.inboxView = inboxView;
        setBaseFragmentView(inboxView);
        repoCentral = new RepoCentral(this);
        chatMutableLiveData = new MutableLiveData<>();
        if (pageNo < 1) {
            pageNo = 1;
            lastResultsPageNo = 1;
        }
    }

    public void loadNextPage() {
        pageNo = pageNo + 1;
        loadInbox();
    }

    public void createInbox(int toUserId, int toRoleId) {
        inboxView.showLoading(getString(R.string.loading));
        User user = CacheManager.getCurrentUser();
        repoCentral.createInbox(new CreateInboxRequest(user.getUserId(), toUserId, user.getUserRoleId(), toRoleId),
                new ApiCallback<BaseResponse<ChatItem>>(this) {
                    @Override
                    public void onSuccess(BaseResponse<ChatItem> data, String msg, int status) {
                        super.onSuccess(data, msg, status);
                        inboxView.dismissLoading();
                        inboxView.onChatCreated(data.getData());
                    }
                });

    }

    public void loadInbox() {
        if (pageNo == 1 && GenericUtils.isArrayEmpty(getChatMutableLiveData().getValue()))
            inboxView.showLoading("");
        repoCentral.loadAllChats(pageNo, new ApiCallback<BaseResponse<ArrayList<ChatItem>>>(this) {
            @Override
            public void onSuccess(BaseResponse<ArrayList<ChatItem>> data, String msg, int status) {
                super.onSuccess(data, msg, status);
                inboxView.dismissLoading();

                List<ChatItem> chatItems = chatMutableLiveData.getValue();
                if (chatItems == null)
                    chatItems = new ArrayList<>();

                if (lastResultsPageNo != pageNo)
                    chatItems.addAll(data.getData());
                else
                    chatItems = data.getData();

                lastResultsPageNo = pageNo;

                chatMutableLiveData.setValue(chatItems);


            }
        });
    }


    public void chatAddToFav(ChatItem chatItem, int pos) {
        int inboxId = chatItem.getInboxId();

        repoCentral.toggleChatFavorite(inboxId, !chatItem.isFavorite(), new ApiCallback<BaseResponse<Object>>(this) {
            @Override
            public void onSuccess(BaseResponse<Object> data, String msg, int status) {
                super.onSuccess(data, msg, status);
                chatItem.setFavorite(!chatItem.isFavorite());
                inboxView.onAddToFavSuccessful(chatItem, pos);
                chatMutableLiveData.setValue(chatMutableLiveData.getValue());

            }
        });

    }


    public MutableLiveData<List<ChatItem>> getChatMutableLiveData() {
        return chatMutableLiveData;
    }

    public void archieveChat(int pos, int chatId, Boolean moveToArchive) {
        int realPosition = getChatItemPos(chatId);
        MutableLiveData<List<ChatItem>> mutableLiveData = getChatMutableLiveData();
        List<ChatItem> chatItems = mutableLiveData.getValue();
        ChatItem chatItem = chatItems.get(realPosition);
        if (moveToArchive == null) {
            moveToArchive = !chatItem.isArchived();
        } else {
            if (chatItem.isArchived() == moveToArchive)
                return;
        }
        if (moveToArchive) {
            chatItem = chatItems.get(realPosition);
            chatItem.setArchived(true);
        } else {
            chatItem.setArchived(false);
        }


        final ChatItem[] finalChatItem = {chatItem};
        baseFragmentView.showLoading(context.getString(R.string.moving_chat_to_archive));

        repoCentral.setArchiveChat(chatItem, new ApiCallback<BaseResponse<Boolean>>(this) {
            @Override
            public void onSuccess(BaseResponse<Boolean> data, String msg, int status) {
                super.onSuccess(data, msg, status);

                finalChatItem[0] = chatItems.remove(realPosition);
                if (finalChatItem[0].isArchived()) {

                }
                getChatMutableLiveData().setValue(chatItems);
                baseFragmentView.showSuccessMessage(context.getString(R.string.moved_to_archived_success), false, 0,
                        true);

                baseFragmentView.dismissLoading();

            }
        });


    }

    private int getChatItemPos(int chatId) {
        MutableLiveData<List<ChatItem>> chatMutableLiveData = getChatMutableLiveData();
        List<ChatItem> chatItems = chatMutableLiveData.getValue();
        int index = -1;
        if (chatItems != null) {
            for (int i = 0; i < chatItems.size(); i++) {
                if (chatItems.get(i).getInboxId() == chatId) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

}


