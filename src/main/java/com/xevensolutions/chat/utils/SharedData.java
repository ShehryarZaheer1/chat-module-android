package com.xevensolutions.chat.utils;

public class SharedData {

    private Integer activeChatId;

    private SharedData(){

        activeChatId = -1;
    }

    private  static SharedData instance = new SharedData();

    public static SharedData getInstance() {
        return instance;
    }

    public void setActiveChatId(Integer activeChatId) {

        this.activeChatId = activeChatId;
    }

    public Integer getActiveChatId() {
        return activeChatId;
    }
}


