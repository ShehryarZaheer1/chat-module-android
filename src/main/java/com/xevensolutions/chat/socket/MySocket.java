package com.xevensolutions.chat.socket;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.xevensolutions.baseapp.MyBaseApp;
import com.xevensolutions.baseapp.interfaces.CountDownTimerListener;
import com.xevensolutions.chat.interfaces.SocketConnectionListener;
import com.xevensolutions.chat.models.ReqMessage;
import com.xevensolutions.baseapp.models.UserAvailability;
import com.xevensolutions.baseapp.utils.MyCountDown;
import com.xevensolutions.chat.models.ChatMessage;
import com.xevensolutions.chat.utils.CacheManager;
import com.xevensolutions.chat.utils.Constants;
import com.xevensolutions.chat.utils.NotificationUtils;


import org.json.JSONException;
import org.json.JSONObject;


import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;

import static io.socket.client.Socket.EVENT_CONNECT;
import static io.socket.client.Socket.EVENT_CONNECT_ERROR;
import static io.socket.client.Socket.EVENT_DISCONNECT;
import static io.socket.client.Socket.EVENT_ERROR;


public class MySocket {


    private static MySocket INSTANCE = null;
    MySocketListener socketListener;
    boolean isConnected;
    Socket mSocket;
    private boolean isUserJoined;
    Handler handler;
    MyCountDown socketReconnectCountDown;

    private static final String KEY_TAG = "Socket";

    public boolean isUserJoined() {
        return isUserJoined;
    }

    public void setUserJoined(boolean userJoined) {
        isUserJoined = userJoined;
        if (!isUserJoined)
            connectUserToRoom();
    }


    public boolean isConnected() {
        return mSocket != null && mSocket.connected() && mSocket.id() != null;
    }

    private MySocket() {
        handler = new Handler();
        initConnection();

    }

    private void initConnection() {

        IO.Options opts = new IO.Options();
        opts.transports = new String[]{WebSocket.NAME};
        opts.reconnection = false;
        try {
            mSocket = IO.socket(Constants.SOCKET_URL, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        addListenersToSocket();
    }

    private void addListenersToSocket() {
        mSocket.on(EVENT_ERROR, args -> {
            Log.i("Socket error", new Gson().toJson(args));

        });


        mSocket.on(EVENT_DISCONNECT, args -> {
            isConnected = false;
            startReconnection();
            if (socketListener != null)
                socketListener.onSocketDisconnected();
            try {
                Log.e("Socket Status - > ", "Socket disconnected" + new Gson().toJson(args));
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });


        /*mSocket.on(EVENT_CONNECT_TIMEOUT, args -> {
            isConnected = false;
            Log.e("Socket Status - > ", "Socket connect timedout");
            connect(null);
        });

        mSocket.on(EVENT_RECONNECT, args -> {
            isConnected = false;
            Log.e("Socket Status - > ", "Reconnecting:");

        });
*/
        mSocket.on("logs", args -> {
            try {
                Log.i("Socket logs", new Gson().toJson(args));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        mSocket.on("exception", args -> {
            isConnected = false;
            try {
                Log.e("Socket Status - > ", "Exception:" + new Gson().toJson(args));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        /*mSocket.on(EVENT_CONNECTING, args -> {
            isConnected = false;
            Log.e("Socket Status - > ", "Socket connecting");

        });
*/

        mSocket.on("roomConStatus", args -> {
            JSONObject jsonObject = (JSONObject) args[0];
            if (jsonObject.has("status")) {
                try {
                    boolean isOnline = jsonObject.getBoolean("status");
                    setUserJoined(isOnline);
                    Log.i("Socket event room", new Gson().toJson(args));
                    Log.e("Socket Status - > ", jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("userConStatus", args -> {
            try {
                Log.i("Socket event userCon", new Gson().toJson(args));
                JSONObject jsonObject = (JSONObject) args[0];
                if (jsonObject.has("data")) {

                    JSONObject data = jsonObject.getJSONObject("data");
                    UserAvailability userAvailability = new Gson().fromJson(data.toString(), UserAvailability.class);
                    if (CacheManager.getCurrentUser() != null && CacheManager.getCurrentUser().getUserId() == userAvailability.getUserId()) {
                        setUserJoined(userAvailability.isOnline());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        mSocket.on("messageData", args -> {
            JSONObject jsonObject = (JSONObject) args[0];


            try {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                onSignalReceived(dataObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //socketListener.onMessageReceivedFromSocket(respMessage);

        });

        mSocket.on("newConStatus", args -> {
            Log.e("Socket Status - > ", "new user conn");

        });


    }

    ;

    public void setListener(MySocketListener socketListener) {
        this.socketListener = socketListener;

    }

    public static MySocket getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MySocket();
        }
        return (INSTANCE);
    }

    public Socket getmSocket() {
        return mSocket;
    }

    public void addNewConnection(long userId) {
        String[] userIds = new String[]{CacheManager.getCurrentUser().getUserId() + "", userId + ""};
        mSocket.emit("newUserCon", userIds);
    }

    public void connect(SocketConnectionListener socketConnectionListener) {

        isConnected = isConnected();
        if (isConnected) {
            if (socketConnectionListener != null)
                socketConnectionListener.onSocketConnected();
            else
                connectUserToRoom();
            return;
        }

        mSocket.connect();


        mSocket.on(EVENT_CONNECT_ERROR, args -> {

            startReconnection();
            isConnected = false;
            if (socketConnectionListener != null) {
                socketConnectionListener.onSocketConnectError();
            }

            try {
                Log.e("Socket Status - > ", "Socket connect Error" + new Gson().toJson(args));
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });


        mSocket.on(EVENT_CONNECT, args -> {
            if (socketReconnectCountDown != null)
                socketReconnectCountDown.setStopped(true);
            isConnected = true;
            connectUserToRoom();
            if (socketListener != null)
                socketListener.onSocketConnected();
            if (socketConnectionListener != null)
                socketConnectionListener.onSocketConnected();
            Log.e("Socket Status - > ", "Socket connected with Id:" + mSocket.id());

        });

    }

    private void startReconnection() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (socketReconnectCountDown == null || socketReconnectCountDown.isStopped()) {
                socketReconnectCountDown = new MyCountDown(5000, 1000, new CountDownTimerListener() {
                    @Override
                    public void onTick(long millisInFuture) {

                    }

                    @Override
                    public void onFinish() {
                        socketReconnectCountDown = null;
                        connect(null);
                    }
                });
                socketReconnectCountDown.start();
            }
        });
    }


    public void disconnect() {
        if (mSocket == null)
            return;
        mSocket.disconnect();
    }

    private void connectUserToRoom() {
        if (CacheManager.getInstance().getCurrentUser() != null)
            connectToUserRoom(CacheManager.getInstance().getCurrentUser().getUserId() + "");
    }

    public void onSignalReceived(JSONObject dataObject) {
        try {
            String type = dataObject.optString("type");
            if (type == null || socketListener == null)
                return;

            String dataString = dataObject.optString("data");
            if (type.equalsIgnoreCase(Constants.SignalTypes.NEW_MESSAGE)) {
                ChatMessage chatMessage = new Gson().fromJson(dataString, ChatMessage.class);
                socketListener.onMessageRecevied(chatMessage);
                NotificationUtils.showNotification(MyBaseApp.getContext(), dataObject);
            }

        } catch (Exception e) {

        }
    }


    public void connectToUserRoom(String userId) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userId", userId);

        JSONObject a = new JSONObject();
        try {
            a.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Object[] objects = new Object[1];
        objects[0] = a;

        mSocket.emit("connectToUserRoom", objects, new Ack() {
            @Override
            public void call(Object... args) {
                Log.i("ack", "socket");
            }
        });
        Log.e("as", "a");
    }


    public <T> void sendMessage(String type, String userId, T data) {
        ArrayList<String> users = new ArrayList<>();
        users.add(userId);
        sendMessage(type, users, data);
    }

    public <T> void sendMessage(String type, ArrayList<String> userIds, T data) {
        MySocket.getInstance().connect(new SocketConnectionListener() {
            @Override
            public void onSocketConnected() {

                ReqMessage reqMessage = new ReqMessage(userIds, type, data);
                Object[] args = new Object[1];
                args[0] = reqMessage.toJsonObject();
                mSocket.emit("sendMessage", args, new Ack() {
                    @Override
                    public void call(Object... args) {
                        try {
                            Log.i("ack", new Gson().toJson(args[0]));
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onSocketDisconnected() {

            }
        });

    }

    public void emitTypingUpdate(boolean b) {

    }

    public void checkUserRoomStatus(long userId) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId + "");
            mSocket.emit("checkUserRoomConStatus",
                    jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public interface MySocketListener extends SocketConnectionListener {


        void onMessageRecevied(ChatMessage chatMessage);
    }


}
