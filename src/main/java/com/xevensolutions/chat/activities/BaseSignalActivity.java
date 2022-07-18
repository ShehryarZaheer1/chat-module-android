package com.xevensolutions.chat.activities;

import androidx.viewbinding.ViewBinding;

import com.xevensolutions.baseapp.activities.BaseActivity;
import com.xevensolutions.chat.models.ChatMessage;
import com.xevensolutions.chat.socket.MySocket;
import com.xevensolutions.chat.utils.CacheManager;

public abstract class BaseSignalActivity<Vb extends ViewBinding> extends BaseActivity<Vb> implements MySocket.MySocketListener {


    private MySocket socketManager;


    private void checkSocketConnection() {
        socketManager = MySocket.getInstance();
        if (!socketManager.isConnected()) {
            socketManager.connect(null);
        } else {
            if (CacheManager.getCurrentUser() != null)
                socketManager.checkUserRoomStatus(CacheManager.getCurrentUser().getUserId());

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSocketConnection();
        MySocket.getInstance().setListener(this);
    }

    @Override
    public void onMessageRecevied(ChatMessage chatMessage) {

    }

    @Override
    public void onSocketConnected() {

    }

    @Override
    public void onSocketDisconnected() {

    }
}
