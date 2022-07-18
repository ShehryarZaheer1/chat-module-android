package com.xevensolutions.chat.presenters;


import com.xevensolutions.baseapp.presenters.BaseFragmentView;
import com.xevensolutions.chat.models.ChatItem;

public interface ChatView extends BaseFragmentView {

    void onMsgMissing();

    void onAddToFavSuccess(ChatItem chatItem);
}
