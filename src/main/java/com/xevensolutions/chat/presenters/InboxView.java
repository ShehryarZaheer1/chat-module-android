package com.xevensolutions.chat.presenters;


import com.xevensolutions.baseapp.presenters.BaseFragmentView;
import com.xevensolutions.chat.models.ChatItem;

import java.util.List;

public interface InboxView extends BaseFragmentView {

    default void onAddToFavSuccessful(ChatItem chatItem, int pos){

    };

    default void onChatsFiltered(List<ChatItem> filteredChats, boolean favorites){

    };

    default void onChatCreated(ChatItem chatItem){

    };
}
