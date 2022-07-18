package com.xevensolutions.chat.chatAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.xevensolutions.baseapp.adapters.BaseRecyclerViewFilterAdapter;
import com.xevensolutions.baseapp.presenters.BaseFragmentView;
import com.xevensolutions.baseapp.utils.DateUtils;
import com.xevensolutions.baseapp.utils.ImageUtils;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.R2;
import com.xevensolutions.chat.chatAdapter.viewHolders.BaseViewHolder;
import com.xevensolutions.chat.interfaces.InboxListlListener;
import com.xevensolutions.chat.models.ChatItem;

import java.util.ArrayList;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChat extends BaseRecyclerViewFilterAdapter<ChatItem> {

    InboxListlListener inboxListlListener;
    boolean isArchives;

    public AdapterChat(Context context, ArrayList<ChatItem> items, BaseFragmentView baseFragmentView,
                       boolean isArchives,
                       InboxListlListener onListItemClickListener) {
        super(context, items, baseFragmentView, onListItemClickListener);
        this.inboxListlListener = onListItemClickListener;
        this.isArchives = isArchives;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, ChatItem val) {
        ChatViewHolder chatViewHolder = (ChatViewHolder) holder;
        chatViewHolder.tvChatTitle.setText(val.getUserName());
        chatViewHolder.tvLastMessage.setText(val.getMessage());

        int previousIndex = holder.getAdapterPosition() - 1;
        if (previousIndex == -1) {
            chatViewHolder.tvHeader.setVisibility(View.VISIBLE);
        } else if (previousIndex >= 0) {
            ChatItem previousChat = getItem(previousIndex);
            if (previousChat.isFavorite() == val.isFavorite())
                chatViewHolder.tvHeader.setVisibility(View.GONE);
            else
                chatViewHolder.tvHeader.setVisibility(View.VISIBLE);
        }

        //Showing or hiding the badge counter...
        if (val.getBadgeCount() != null) {
            if (val.getBadgeCount() > 0) {
                chatViewHolder.tvUnreadCounter.setText(val.getBadgeCount().toString());
                chatViewHolder.layoutBadge.setVisibility(View.VISIBLE);
            } else {
                chatViewHolder.layoutBadge.setVisibility(View.GONE);
            }
        } else {
            chatViewHolder.layoutBadge.setVisibility(View.GONE);
        }

        chatViewHolder.tvHeader.setText(val.isFavorite() ? getContext().getString(R.string.favorites) : getContext().getString(R.string.all_chats));

        if (val.isFavorite())
            chatViewHolder.btnToggleFav.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_favorite_24));
        else
            chatViewHolder.btnToggleFav.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_favorite_border_24));

        ImageUtils.setRemoteImage(getContext(), val.getProfilePath(), chatViewHolder.ivChat,
                R.drawable.ic_profile_placeholder);

        if (val.getCreatedDateTime() != null)
            chatViewHolder.tvTimeAgo.setText(DateUtils.getFormattedDateToShow(DateUtils.getLocalDateStringFromUTC(val.getCreatedDateTime()))/*DateUtils.getTimeAgo(val.getLastUpdateDate(), true)*/);

        chatViewHolder.main_layout.setOnClickListener(view -> {
            inboxListlListener.onListItemClicked(holder.getAdapterPosition());
        });

        chatViewHolder.btnToggleFav.setOnClickListener(v -> inboxListlListener.onFavToggled(holder.getAdapterPosition()));

        chatViewHolder.btnToggleFav.setVisibility(isArchives ? View.GONE : View.VISIBLE);

        chatViewHolder.btnToggleFav.setVisibility(View.GONE);
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(int viewType, ViewGroup parent) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox, parent, false));
    }

    public static class ChatViewHolder extends BaseViewHolder {

        @BindView(R2.id.main_layout)
        ConstraintLayout main_layout;
        @BindView(R2.id.tvChatTitle)
        public TextView tvChatTitle;
        @BindView(R2.id.ivChat)
        CircleImageView ivChat;
        @BindView(R2.id.tvTimeAgo)
        TextView tvTimeAgo;
        @BindView(R2.id.tvUnreadCounter)
        TextView tvUnreadCounter;
        @BindView(R2.id.tvLastMessage)
        TextView tvLastMessage;
        @BindView(R2.id.btnToggleFav)
        AppCompatImageView btnToggleFav;
        @BindView(R2.id.tv_chat_header)
        TextView tvHeader;
        @BindView(R2.id.layoutBadge)
        LinearLayout layoutBadge;


        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
