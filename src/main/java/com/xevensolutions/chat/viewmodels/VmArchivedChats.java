package com.xevensolutions.chat.viewmodels;

import android.content.Context;
import android.graphics.pdf.PdfDocument;

import androidx.lifecycle.MutableLiveData;


import com.xevensolutions.baseapp.MyBaseApp;
import com.xevensolutions.baseapp.interfaces.ApiCallback;
import com.xevensolutions.baseapp.models.BaseResponse;
import com.xevensolutions.baseapp.utils.CollectionUtils;
import com.xevensolutions.baseapp.viewmodels.BaseViewModel;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.models.ChatItem;
import com.xevensolutions.chat.presenters.ArchivedChatView;
import com.xevensolutions.chat.repos.RepoCentral;
import com.xevensolutions.chat.utils.CacheManager;

import java.util.List;

public class VmArchivedChats extends BaseViewModel {
    RepoCentral repoConversations;
    ArchivedChatView archivedChatView;
    MutableLiveData<List<ChatItem>> chatsLiveData;
    int pageNo;


    public void setArchivedChatView(ArchivedChatView archivedChatView) {
        this.archivedChatView = archivedChatView;
        baseFragmentView = archivedChatView;
        repoConversations = new RepoCentral(this);
        chatsLiveData = new MutableLiveData<>();
        pageNo = 1;
    }

    public void loadNextPage() {
        pageNo = pageNo + 1;
        getArchivedChats(CacheManager.getCurrentUser().getUserId());
    }

    public void getArchivedChats(int userId) {
        if (pageNo == 1)
            archivedChatView.showLoading(context.getString(R.string.loading));
        repoConversations.getArchivedChats(userId, pageNo, new ApiCallback<BaseResponse<List<ChatItem>>>(this) {
            @Override
            public void onSuccess(BaseResponse<List<ChatItem>> data, String msg, int status) {
                super.onSuccess(data, msg, status);
                archivedChatView.dismissLoading();


                if (data != null) {
                    chatsLiveData.setValue(data.getData());
                }
            }
        });
    }

    public void deleteChat(int pos) {
        ChatItem chatItem = getChatsLiveData().getValue().get(pos);
        baseFragmentView.showLoading(getString(R.string.deleting_chat));
        repoConversations.deleteChat(chatItem, new ApiCallback<BaseResponse<Object>>(this) {
            @Override
            public void onSuccess(BaseResponse<Object> data, String msg, int status) {
                super.onSuccess(data, msg, status);
                baseFragmentView.dismissLoading();
                List<ChatItem> chatItems = getChatsLiveData().getValue();
                chatItems.remove(pos);
                getChatsLiveData().setValue(chatItems);
            }
        });
    }


    public void setArchive(int index, boolean showLoading) {
        List<ChatItem> allChats = chatsLiveData.getValue();
        if (CollectionUtils.isEmpty(allChats))
            return;

        ChatItem chatItem = allChats.get(index);
        chatItem.setArchived(!chatItem.isArchived());

        if (showLoading)
            baseFragmentView.showLoading(context.getString(chatItem.isArchived() ? R.string.moving_chat_to_archive :
                    R.string.removing_chat_from_archives));
        repoConversations.setArchiveChat(chatItem, new ApiCallback<BaseResponse<Boolean>>(this) {
            @Override
            public void onSuccess(BaseResponse<Boolean> data, String msg, int status) {
                super.onSuccess(data, msg, status);
                allChats.remove(index);
                chatsLiveData.setValue(allChats);
                if (showLoading)
                    baseFragmentView.dismissLoading();
                baseFragmentView.showSuccessMessage(context.getString(chatItem.isArchived() ?
                                R.string.chat_archived : R.string.chat_unarchived), false,
                        1, false);

            }


        });

    }

    public MutableLiveData<List<ChatItem>> getChatsLiveData() {
        return chatsLiveData;
    }


    @Override
    public Context getContext() {
        return MyBaseApp.getContext();
    }
}
