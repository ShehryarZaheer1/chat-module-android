package com.xevensolutions.chat.interfaces;

import android.app.Activity;

import com.xevensolutions.chat.models.ChatItem;

public interface PatientOptionsListener {

    void onViewProfileTapped(Activity activity, ChatItem chatItem);

    void onViewEmrTapped(Activity activity, ChatItem chatItem);

    void onCreateTreatmentPlanTapped(Activity activity, ChatItem
            chatItm);
}
