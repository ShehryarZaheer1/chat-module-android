package com.xevensolutions.chat.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.xevensolutions.chat.R;
import com.xevensolutions.chat.databinding.ActivityInboxBinding;
import com.xevensolutions.chat.fragments.FragmentInboxList;
import com.xevensolutions.chat.models.ChatMessage;

public class ActivityInbox extends BaseSignalActivity<ActivityInboxBinding> {

    public static final String KEY_ENABLE_SENDING = "key_enable_sending";
    FragmentInboxList fragmentInboxList;

    boolean enableSending;


    @Override
    public void receiveExtras(Bundle arguments) {
        enableSending = arguments.getBoolean(KEY_ENABLE_SENDING);
    }

    @Override
    public void exitActivity() {

    }

    @Override
    public void showToast(String error) {

    }

    public static void launch(Activity activity, boolean enableSending) {
        Intent intent = new Intent(activity, ActivityInbox.class);
        intent.putExtra(KEY_ENABLE_SENDING, enableSending);
        activity.startActivity(intent);
    }

    @Override
    public ActivityInboxBinding getViewBinding() {
        return ActivityInboxBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentInboxList = FragmentInboxList.newInstance(enableSending);
        changeFragment(fragmentInboxList, R.id.main_layout, true, false);
    }

    @Override
    public int getActivityName() {
        return R.string.inbox;
    }

    @Override
    public void onMessageRecevied(ChatMessage chatMessage) {
        super.onMessageRecevied(chatMessage);
        runOnUiThread(() -> fragmentInboxList.onMessageReceived(chatMessage));
    }
}