package com.xevensolutions.chat.utils;

import java.util.function.BinaryOperator;

public class Constants {

    public static final boolean IS_DUMMY_DATA = false;
    public static final String FILES_BASE_URL = "https://s3.ca-central-1.amazonaws.com/images-themarche/";
    public static final String BLOB_PATH = "https://redhandedstorage.blob.core.windows.net/assets";
    public static final long REQUEST_TIMEOUT = 30;
    public static final String FILE_PROVIDER = "com.ihakeem.app.fileprovider";
    public static String SOCKET_URL = "https://stealthsocket.ghostlayers.com:3000/chat";


    public interface ChatViewTypes {
        int TEXT_MESSAGE = 0;
        int IMAGE_MESSAGE = 1;
        int AUDIO_MESSAGE = 2;
        int DOCUMENT_MESSAGE = 3;
    }

    public interface MessageSide {
        int NO_SIDE = 0;
        int RECEIVED = 1;
        int SENT = 2;
    }

    public interface SignalTypes {
        String NEW_MESSAGE = "new_message_contestant";
    }

    public interface Notifications {
        String NOTIFICATION_TITLE = "title";
        String NOTIFICATION_BODY = "msg";
        String NOTIFICATION_TYPE = "type";
        int NOTIFICATION_ID = 1;
    }


}
