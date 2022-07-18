package com.xevensolutions.chat.networking;

import android.content.Intent;


import com.xevensolutions.baseapp.interfaces.ApiCallback;
import com.xevensolutions.baseapp.models.BaseResponse;
import com.xevensolutions.chat.models.ChatItem;
import com.xevensolutions.chat.models.ChatMessage;
import com.xevensolutions.chat.models.CreateInboxRequest;
import com.xevensolutions.chat.models.DeleteChatRequest;
import com.xevensolutions.chat.models.FavChatRequest;
import com.xevensolutions.chat.models.ReqArchive;
import com.xevensolutions.chat.models.SendMessageRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebServices {

    @GET("/api/chat/get/inboxList")
    Single<BaseResponse<ArrayList<ChatItem>>> getAllConversationsByUserId(@Query("PageNo") int pageNo,
                                                                          @Query("Size") long userId);

    @GET("/api/chat/message/getAll")
    Single<BaseResponse<List<ChatMessage>>> getChatMessages(@Query("inboxId") int chatId, @Query("PageNo") int pageNo,
                                                            @Query("Size") long userId);

    @POST("/api/chat/message/post")
    Single<BaseResponse<ChatMessage>> sendMessage(@Body SendMessageRequest sendMessageRequest);

    @GET("/api/chat/message/isRead")
    Single<BaseResponse<Object>> markAsRead(@Query("UserId") int userId, @Query("InboxId") int inboxId);

    @POST("/api/Chat/FavrioteChat")
    Single<BaseResponse<Object>> toggleChatFav(@Body FavChatRequest favChatRequest);

    @GET("/api/Chat/GetAllArchiveConversationUserByUserId")
    Single<BaseResponse<List<ChatItem>>> getArchiveChats(
            @Query("userId") int userId, @Query("pageNumber") int pageNo);

    @POST("/api/Chat/ArchiveChat")
    Single<BaseResponse<Boolean>> setArchiveChat(@Body ReqArchive reqArchive);

    @HTTP(method = "DELETE", path = "/api/Chat/InboxDelete", hasBody = true)
    Single<BaseResponse<Object>> deleteChat(@Body DeleteChatRequest deleteChatRequest);

    @POST("/api/chat/create/inbox")
    Single<BaseResponse<Object>> createInbox(@Body CreateInboxRequest createInboxRequest);


/*    @GET("/employees")
    Single<BaseResponse<ArrayList<TestEmployee>>> getTestEmployees();*/

}
