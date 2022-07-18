package com.xevensolutions.chat.interfaces;

public interface SocketConnectionListener {

    void onSocketConnected();

    default void onSocketConnectError(){

    }

    void onSocketDisconnected();
}
