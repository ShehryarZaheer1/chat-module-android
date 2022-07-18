package com.xevensolutions.chat.repos;

import static com.xevensolutions.chat.utils.Constants.IS_DUMMY_DATA;

import com.xevensolutions.baseapp.interactors.BaseInteractor;
import com.xevensolutions.baseapp.interfaces.ApiCallback;
import com.xevensolutions.baseapp.models.BaseResponse;
import com.xevensolutions.baseapp.repos.BaseRepository;
import com.xevensolutions.chat.models.ChatItem;
import com.xevensolutions.chat.models.ChatMessage;
import com.xevensolutions.chat.models.CreateInboxRequest;
import com.xevensolutions.chat.models.DeleteChatRequest;
import com.xevensolutions.chat.models.FavChatRequest;
import com.xevensolutions.chat.models.ReqArchive;
import com.xevensolutions.chat.models.SendMessageRequest;
import com.xevensolutions.chat.networking.WebServices;
import com.xevensolutions.chat.networking.WebServicesHandler;
import com.xevensolutions.chat.utils.CacheManager;
import com.xevensolutions.chat.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class RepoCentral extends BaseRepository {


    WebServices webServices;

    public RepoCentral(BaseInteractor baseInteractor) {
        super(baseInteractor);
        webServices = WebServicesHandler.getWebServices();
    }

    public void loadAllChats(int pageNo, ApiCallback<BaseResponse<ArrayList<ChatItem>>> baseResponseApiCallback) {
        if (IS_DUMMY_DATA) {

            ArrayList<ChatItem> chatItems = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                chatItems.add(new ChatItem(1, 23));
            }
            baseResponseApiCallback.onSuccess(new BaseResponse<ArrayList<ChatItem>>(true, "", 200, chatItems), "", 200);
        } else
            makeApiCall(webServices.getAllConversationsByUserId(pageNo, 10),
                    baseResponseApiCallback);
    }


    public void getChatMessages(int pageNo, long chatId, ApiCallback<BaseResponse<List<ChatMessage>>> apiCallback) {
        if (IS_DUMMY_DATA) {
            List<ChatMessage> dummyMessages = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                dummyMessages.add(new ChatMessage(i % 3 == 0 ? 101 : 100, "This is a test message from a test user"));
            }
            apiCallback.onSuccess(new BaseResponse<List<ChatMessage>>(true, "", 200, dummyMessages), "",
                    200);
        } else
            makeApiCall(webServices.getChatMessages((int) chatId, pageNo,10), apiCallback);
    }

    public void sendMessage(SendMessageRequest sendMessageRequest,
                            ApiCallback<BaseResponse<ChatMessage>> apiCallback) {
        makeApiCall(webServices.sendMessage(sendMessageRequest), apiCallback);
    }

    public void markAsRead(int userId, int inboxId, ApiCallback<BaseResponse<Object>> apiCallback) {
        makeApiCall(webServices.markAsRead(userId, inboxId), apiCallback);
    }


    @Override
    public void makeApiCall(Single single, ApiCallback apiCallback) {
        if (!IS_DUMMY_DATA)
            super.makeApiCall(single, apiCallback);
    }

    public void toggleChatFavorite(int inboxId, boolean isFav, ApiCallback<BaseResponse<Object>> apiCallback) {
        makeApiCall(webServices.toggleChatFav(new FavChatRequest(inboxId,
                CacheManager.getCurrentUser().getUserId(), isFav)), apiCallback);
    }


    public void getArchivedChats(int userId, int pageNo, ApiCallback<BaseResponse<List<ChatItem>>> listApiCallback) {
        makeApiCall(webServices.getArchiveChats(userId, pageNo), listApiCallback);
    }

    public void setArchiveChat(ChatItem chatItem, ApiCallback<BaseResponse<Boolean>> apiCallback) {
        setArchiveChat(CacheManager.getCurrentUser().getUserId(), chatItem.getInboxId(),
                chatItem.isArchived(), apiCallback);
    }

    public void setArchiveChat(int userId, int chatId, Boolean archive, ApiCallback<BaseResponse<Boolean>> apiCallback) {
        makeApiCall(webServices.setArchiveChat(new ReqArchive(chatId, userId, archive)), apiCallback);
    }

    public void deleteChat(ChatItem chatItem, ApiCallback<BaseResponse<Object>> apiCallback) {
        makeApiCall(webServices.deleteChat(new DeleteChatRequest(chatItem.getInboxId(),
                CacheManager.getCurrentUser().getUserId(), true)), apiCallback);
    }

    public void createInbox(CreateInboxRequest createInboxRequest, ApiCallback<BaseResponse<ChatItem>> baseResponseApiCallback) {
        makeApiCall(webServices.createInbox(createInboxRequest), baseResponseApiCallback);
    }
}
