package com.xevensolutions.chat.chatAdapter;

import android.widget.ImageView;

import com.xevensolutions.baseapp.interfaces.OnListItemClickListener;
import com.xevensolutions.chat.models.ChatMessage;


public interface ChatAdapterListener extends OnListItemClickListener {

    void onAudioPlayToggled(int index);

    void onImageTapped(int pos, ImageView imageView);

    void onVideoTapped(int adapterPosition, ImageView imageView);

    void onProfileTapped(int pos, ImageView imageView);

    void onResendTapped(int pos);

    void onEditMessage(int position);

    void onQuoteTapped(int position);

    void onQuotedMessageTapped(int adapterPosition);

    void onForwardTapped(int position);

    void onDeleteForMeTapped(int position);

    void onDeleteForEveryOneTapped(int position);

    void onDocumentTapped(int position);

    void scrollToBottom();

    void onDownloadTapped(int position);

    default boolean handleLinkClick(ChatMessage chatMessage, String url){
        return false;
    };
}
