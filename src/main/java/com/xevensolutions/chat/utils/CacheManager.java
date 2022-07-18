package com.xevensolutions.chat.utils;

import static com.xevensolutions.chat.utils.Constants.IS_DUMMY_DATA;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.xevensolutions.chat.models.User;

import java.nio.file.attribute.UserDefinedFileAttributeView;

public class CacheManager {


    private static final String KEY_USER = "user";
    Context context;
    TinyDB tinyDB;
    Gson gson;
    private static CacheManager instance = new CacheManager();


    public void setContext(Context context) {
        this.context = context;
        tinyDB = new TinyDB(context);
        gson = new Gson();
    }

    public static CacheManager getInstance() {
        return instance;
    }


    public void setCurrentUser(User user) {
        String json = gson.toJson(user);
        tinyDB.putString(KEY_USER, json);
    }

    public static User getCurrentUser() {

        if (IS_DUMMY_DATA) {
            return new User(100, 3, "Coach", "","");
        }
        if (getInstance().tinyDB == null || getInstance().gson == null)
            return null;
        String json = getInstance().tinyDB.getString(KEY_USER);
        User user = null;
        if (json != null) {
            user = getInstance().gson.fromJson(json, User.class);
        }
        return user;
    }


}
