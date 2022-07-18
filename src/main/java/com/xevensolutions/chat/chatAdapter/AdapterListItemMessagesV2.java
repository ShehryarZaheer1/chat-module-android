package com.xevensolutions.chat.chatAdapter;

import android.content.Context;
import android.os.Trace;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.xevensolutions.baseapp.adapters.BaseRecyclerViewFilterAdapter;
import com.xevensolutions.baseapp.presenters.BaseFragmentView;
import com.xevensolutions.baseapp.utils.DateUtils;
import com.xevensolutions.baseapp.utils.TextUtils;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.chatAdapter.viewHolders.BaseMessageViewHolder;
import com.xevensolutions.chat.chatAdapter.viewHolders.VHImageMessage;
import com.xevensolutions.chat.chatAdapter.viewHolders.VHTextMessage;
import com.xevensolutions.chat.chatAdapter.viewHolders.VhDocumentMessage;
import com.xevensolutions.chat.interfaces.MyDiffUtilAdapterCallback;
import com.xevensolutions.chat.models.ChatMessage;
import com.xevensolutions.chat.utils.DataProvider;

import java.util.ArrayList;
import java.util.List;

import static com.xevensolutions.baseapp.utils.TextUtils.isStringEmpty;
import static com.xevensolutions.chat.utils.Constants.ChatViewTypes.AUDIO_MESSAGE;
import static com.xevensolutions.chat.utils.Constants.ChatViewTypes.DOCUMENT_MESSAGE;
import static com.xevensolutions.chat.utils.Constants.ChatViewTypes.IMAGE_MESSAGE;
import static com.xevensolutions.chat.utils.Constants.ChatViewTypes.TEXT_MESSAGE;
import static com.xevensolutions.chat.utils.Constants.MessageSide.RECEIVED;
import static com.xevensolutions.chat.utils.Constants.MessageSide.SENT;


public class AdapterListItemMessagesV2 extends BaseRecyclerViewFilterAdapter<ChatMessage> {

    ChatAdapterListener chatAdapterListener;

    AudioPlayBackState audioPlayBackState;
    MessageDiffCalculator messageDiffCalculator;


    public AdapterListItemMessagesV2(Context context, ArrayList<ChatMessage> items, BaseFragmentView baseFragmentView,
                                     ChatAdapterListener chatAdapterListener) {
        super(context, items, baseFragmentView, chatAdapterListener);
        this.chatAdapterListener = chatAdapterListener;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, ChatMessage val) {
        Log.i("adapterMessages", "onBind started");
        BaseMessageViewHolder baseMessageViewHolder = (BaseMessageViewHolder) holder;
        ChatMessage nextMessage = null;
        int nextIndex = holder.getAdapterPosition() + 1;
        if (nextIndex < getItemCount())
            nextMessage = getItem(nextIndex);
        baseMessageViewHolder.onBindData(val, nextMessage);
    /*    if (baseMessageViewHolder instanceof VHAudioMessage) {
            if (holder.getAdapterPosition() == VmChat.currentAudioPlayingIndex)
                ((VHAudioMessage) baseMessageViewHolder).updateAudioPlayback(audioPlayBackState);
            *//*else if (audioPlayBackState != null && audioPlayBackState.getPlaybackState() != STOPPED)
                stopAudio(holder.getAdapterPosition());*//*
        }
        Log.i("adapterMessages", "onBind Ended");*/
    }



    @Override
    public RecyclerView.ViewHolder getViewHolder(int viewType, ViewGroup parent) {
        Log.i("adapterMessages", "onCreateView started " + viewType);

        Trace.beginSection("onCreate view calc started");
        int shouldAddDate = viewType % 10;
        int c = viewType % 100;
        int hasQuotedMessage = c / 10;
        int d = viewType % 1000;
        int messageSide = d / 100;
        int innerViewType = viewType / 1000;

        Trace.endSection();

        Trace.beginSection("onCreate inflate main containers");
        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext()).
                inflate(messageSide == SENT ?
                                R.layout.item_chat_send_container : R.layout.item_chat_receive_container,
                        parent, false);
        Trace.endSection();
        Trace.beginSection("onCreate inflate main ui");

        LinearLayout view = constraintLayout.findViewById(R.id.chat_message_container);

        int innerLayoutId;

        if (innerViewType == TEXT_MESSAGE) {
            innerLayoutId = R.layout.item_text_message;
        } else if (innerViewType == AUDIO_MESSAGE) {
            innerLayoutId = R.layout.chat_audio_layout;
        } else if (innerViewType == DOCUMENT_MESSAGE) {
            innerLayoutId = R.layout.item_attachment;
        } else {
            innerLayoutId = R.layout.chat_image_layout;
        }


        View mainView = LayoutInflater.from(parent.getContext()).inflate(innerLayoutId, parent, false);
        ((LinearLayout) view).addView(mainView, 0);
        Trace.endSection();

        if (shouldAddDate == 1) {
            Trace.beginSection("onCreate inflate date");
            LinearLayout contDate = constraintLayout.findViewById(R.id.cont_date);
            View dateView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_date,
                    parent, false);
            contDate.addView(dateView, 0);
            Trace.endSection();
        }


        Trace.beginSection("onCreate init VH");

        BaseMessageViewHolder baseMessageViewHolder = (BaseMessageViewHolder) getViewHolder(constraintLayout, innerViewType);
        baseMessageViewHolder.onCreateViewHolder(parent);
        Trace.endSection();
        Log.i("adapterMessages", "onCreateView ended");

        return baseMessageViewHolder;
    }


    public RecyclerView.ViewHolder getViewHolder(View view, int viewType) {
        switch (viewType) {
            case TEXT_MESSAGE:
                return new VHTextMessage(view, getContext(), chatAdapterListener);
            case IMAGE_MESSAGE:
                return new VHImageMessage(view, getContext(), chatAdapterListener);

            case DOCUMENT_MESSAGE:
                return new VhDocumentMessage(view, getContext(), chatAdapterListener);
        }
        return new VHImageMessage(view, getContext(), chatAdapterListener);
    }

    @Override
    public int getItemViewType(int position) {
        int nextIndex = position + 1;
        ChatMessage nextMessage = null;
        if (nextIndex < getItemCount())
            nextMessage = getItem(nextIndex);
        String viewType = "";
        //viewType = viewType + position;
        ChatMessage chatMessage = getItem(position);
        viewType = viewType + "" + chatMessage.getMessageType();
        viewType = chatMessage.isSentByMe() ? viewType + SENT : viewType + RECEIVED;
        viewType = viewType + (chatMessage.hasQuotedMessage() ? ("" + 1) : ("" + 0));
        if (nextMessage == null || !TextUtils.compareStrings(com.xevensolutions.baseapp.utils.DateUtils.getFormattedDateToShow(nextMessage.getCreatedDate()),
                DateUtils.getFormattedDateToShow(chatMessage.getCreatedDate()))) {
            viewType = viewType + "" + 1;
        } else
            viewType = viewType + "" + 0;

        return Integer.parseInt(viewType);


/*
        ChatMessage chatMessage = getItem(position);
        if (!chatMessage.isFileAttached()) {
            return TEXT_MESSAGE;
        } else if (chatMessage.isFileImage())
            return IMAGE_MESSAGE;
        else if (chatMessage.isFileAudio())
            return AUDIO_MESSAGE;

        return IMAGE_MESSAGE;
*/

    }

    public void updateOldMessageOrAddNew(ChatMessage data) {

        int index = getMessageIndex(data);
        if (index >= 0)
            updateItem(index, data);
        else
            addItem(0, data);


    }

    public int getMessageIndex(ChatMessage data) {

        return items.indexOf(data);

    }


    public void deleteMessage(ChatMessage chatMessage) {
        int index = getMessageIndex(chatMessage);
        if (index >= 0)
            deleteItem(index);
    }

    public void deleteMessages(List<Integer> conversationIds) {
        for (Integer id : conversationIds) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setConversationId(id);
            int index = getMessageIndex(chatMessage);
            deleteItem(index);
        }
    }


    public void updateItems(ArrayList<ChatMessage> savedCardItemz, boolean scrollToBottom) {

        //List<ChatMessage> newList = DataProvider.cloneMessages(savedCardItemz);


        if (messageDiffCalculator != null)
            messageDiffCalculator.setStopped(true);


        messageDiffCalculator = new MessageDiffCalculator(items, savedCardItemz, (diffResult, newList1) -> {

            diffResult.dispatchUpdatesTo(new MyDiffUtilAdapterCallback(AdapterListItemMessagesV2.this));
            items.clear();
            items.addAll(newList1);
            itemsCopy = new ArrayList<>();
            itemsCopy.addAll(DataProvider.cloneMessages(newList1));
            if (scrollToBottom)
                chatAdapterListener.scrollToBottom();

        });
        messageDiffCalculator.execute();
        //super.updateItems(savedCardItemz);
    }

    public static class ChatMessagesDiffCallback extends DiffUtil.Callback {

        private final List<ChatMessage> messagesOldList;
        private final List<ChatMessage> messagesNewList;

        public ChatMessagesDiffCallback(List<ChatMessage> oldMessagesList, List<ChatMessage> newMessagesList) {
            this.messagesOldList = oldMessagesList;
            this.messagesNewList = newMessagesList;
        }

        @Override
        public int getOldListSize() {
            return messagesOldList.size();
        }

        @Override
        public int getNewListSize() {
            return messagesNewList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            boolean areItemsTheSame;
            ChatMessage oldMessage = messagesOldList.get(oldItemPosition);
            ChatMessage newMessage = messagesNewList.get(newItemPosition);
            if (oldMessage == null || newMessage == null)
                areItemsTheSame = false;

            else if (oldMessage.isSent() && newMessage.isSent()) {
                areItemsTheSame = oldMessage.getConversationId().equals(newMessage.getConversationId());
            } /*else if (!oldMessage.isSent() && !newMessage.isSent()) {
                if (GenericUtils.isStringEmpty(oldMessage.getUuId()) || GenericUtils.isStringEmpty(newMessage.getUuId()))
                    areItemsTheSame = false;
                else
                    areItemsTheSame = oldMessage.getUuId().equals(newMessage.getUuId());
            } */ else
                areItemsTheSame = false;

            return areItemsTheSame;

        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            boolean areContentsTheSame;
            ChatMessage oldMessage = messagesOldList.get(oldItemPosition);
            ChatMessage newMessage = messagesNewList.get(newItemPosition);
            if (oldMessage == null || newMessage == null) {
                areContentsTheSame = false;
                return areContentsTheSame;
            } else if (!isStringEmpty(oldMessage.getEitherFilePath()) && !isStringEmpty(newMessage.getEitherFilePath())) {
                areContentsTheSame = oldMessage.getEitherFilePath().equals(newMessage.getEitherFilePath());
            } else if (isStringEmpty(oldMessage.getEitherFilePath()) && isStringEmpty(newMessage.getEitherFilePath())) {
                if (isStringEmpty(oldMessage.getMessage()) || isStringEmpty(newMessage.getMessage()))
                    areContentsTheSame = false;
                else
                    areContentsTheSame = oldMessage.getMessage().equals(newMessage.getMessage());
            } else {
                areContentsTheSame = false;
            }

/*
            if (areContentsTheSame)
                areContentsTheSame = oldMessage.getSentStatus() == newMessage.getSentStatus();
*/

            if (areContentsTheSame && oldMessage.getEitherFilePath() != null && newMessage.getEitherFilePath() != null)
                areContentsTheSame = oldMessage.getEitherFilePath().equals(newMessage.getEitherFilePath());

            if (!areContentsTheSame)
                Log.i("TAG", "contents are not same");

            return areContentsTheSame;

        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            // Implement method if you're going to use ItemAnimator
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }

}
