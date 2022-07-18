package com.xevensolutions.chat.chatAdapter;

import android.os.AsyncTask;

import androidx.recyclerview.widget.DiffUtil;



import com.xevensolutions.chat.models.ChatMessage;
import com.xevensolutions.chat.utils.DataProvider;

import java.util.List;

public class MessageDiffCalculator extends AsyncTask<Object, Object, DiffUtil.DiffResult> {
    List<ChatMessage> oldMessages;
    List<ChatMessage> newMessages;
    List<ChatMessage> newClonedMessages;

    DiffCalListener diffCalListener;

    boolean isStopped;

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    public MessageDiffCalculator(List<ChatMessage> oldMessages, List<ChatMessage> newMessages, DiffCalListener diffCalListener) {
        this.oldMessages = oldMessages;
        this.newMessages = newMessages;
        this.diffCalListener = diffCalListener;
    }

    @Override
    protected DiffUtil.DiffResult doInBackground(Object... objects) {
        newClonedMessages = DataProvider.cloneMessages(newMessages);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new AdapterListItemMessagesV2.ChatMessagesDiffCallback(oldMessages, newClonedMessages));
        return diffResult;
    }

    @Override
    protected void onPostExecute(DiffUtil.DiffResult diffResult) {
        super.onPostExecute(diffResult);
        if (isStopped)
            return;
        diffCalListener.onDiffCalculated(diffResult, newClonedMessages);
    }
}
