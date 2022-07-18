package com.xevensolutions.chat.chatAdapter.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.xevensolutions.baseapp.utils.DateUtils;
import com.xevensolutions.chat.R2;
import com.xevensolutions.chat.chatAdapter.ChatAdapterListener;
import com.xevensolutions.chat.models.ChatMessage;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

public class VhMessageDate extends BaseMessageViewHolder {

    @BindView(R2.id.tv_message_date)
    TextView tvMessageDate;

    public VhMessageDate(@NonNull @NotNull View itemView, Context context, ChatAdapterListener chatAdapterListener) {
        super(context, chatAdapterListener, itemView);
    }

    @Override
    public View getRetryView() {
        return null;
    }


    @Override
    public void onBindData(ChatMessage chatMessage, ChatMessage nextMessage) {
        super.onBindData(chatMessage,nextMessage);
        tvMessageDate.setText(DateUtils.getFormattedDateToShow(chatMessage.getCreatedDate()));
    }
}
