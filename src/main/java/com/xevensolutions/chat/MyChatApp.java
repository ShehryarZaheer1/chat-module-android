package com.xevensolutions.chat;

import android.content.Context;

import com.xevensolutions.baseapp.MyBaseApp;
import com.xevensolutions.chat.interfaces.PatientOptionsListener;
import com.xevensolutions.chat.models.User;
import com.xevensolutions.chat.networking.WebServicesHandler;
import com.xevensolutions.chat.socket.MySocket;
import com.xevensolutions.chat.utils.CacheManager;

public class MyChatApp {


    public static PatientOptionsListener patientOptionsListener;

    public static void setPatientOptionsListener(PatientOptionsListener patientOptionsListener) {
        MyChatApp.patientOptionsListener = patientOptionsListener;
    }

    public static PatientOptionsListener getPatientOptionsListener() {
        return patientOptionsListener;
    }

    public static void init(Context context, User user,String baseUrl) {
        MyBaseApp.setContext(context);
        com.xevensolutions.baseapp.utils.CacheManager.getInstance().setContext(context);
        CacheManager.getInstance().setContext(context);
        CacheManager.getInstance().setCurrentUser(user);
        MySocket.getInstance().connect(null);
        initWebServices(baseUrl);

    }


    public static void initWebServices( String baseUrl) {
        WebServicesHandler.getInstance().initInstance( baseUrl);
    }

}
