package com.xevensolutions.chat.chatAdapter.viewHolders;

import android.content.Context;
import android.os.Trace;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.material.textview.MaterialTextView;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.R2;
import com.xevensolutions.chat.chatAdapter.ChatAdapterListener;
import com.xevensolutions.chat.models.ChatMessage;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;

public class VHTextMessage extends BaseMessageViewHolder {

    @BindView(R2.id.tv_message)
    MaterialTextView tvMessage;
    @BindView(R2.id.main_layout)
    LinearLayout mainLayout;

    public VHTextMessage(@NonNull @NotNull View itemView, Context context, ChatAdapterListener chatAdapterListener) {
        super(context, chatAdapterListener, itemView);
    }

    @Override
    public void onBindData(ChatMessage chatMessage, ChatMessage nextMessage) {
        super.onBindData(chatMessage, nextMessage);
        Trace.beginSection("onBind Text");
        tvMessage.setText(chatMessage.getMessage());




       Trace.endSection();
        /*

         */
        /*if (ViewUtil.isViewOverlapping(tvMessage, tvTime)) {
            ViewGroup.LayoutParams layoutParams = tvMessage.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = (int) GenericUtils.convertDpToPixel(20);
                tvTime.setLayoutParams(layoutParams);

            }
        }*/
    }

    @Override
    public View getRetryView() {
        return tvMessage;
    }

    @Override
    public void onMessageSending(ChatMessage chatMessage) {
        super.onMessageSending(chatMessage);
        setTextViewIcon(tvMessage, R.drawable.ic_baseline_access_time_24, chatMessage, true, true);
    }

    @Override
    protected void onMessageSentError(ChatMessage chatMessage) {
        super.onMessageSentError(chatMessage);
        setTextViewIcon(tvMessage, R.drawable.ic_baseline_error_24, chatMessage, true, true);
    }


    @Override
    public void onMessageSent(ChatMessage chatMessage) {
        super.onMessageSent(chatMessage);
        setTextViewIcon(tvMessage, 0, chatMessage, false, false);
    }

    @Override
    public ArrayList<View> getOnReceiveViews() {
        ArrayList<View> views = super.getOnReceiveViews();
        views.add(tvMessage);
        return views;
    }

    @Override
    public View getLongClickView() {
        return tvMessage;
    }
}
