package com.xevensolutions.chat.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.activities.ActivityChat;
import com.xevensolutions.chat.models.ChatItem;
import com.xevensolutions.chat.models.ChatMessage;


import org.json.JSONObject;


public class NotificationUtils {

    static String CHANNEL_ID = "Main_Channel";
    private static NotificationManager notificationManager;


    public static void showNotification(Context context, JSONObject jsonObject) {


        try {
            Intent intent;

            String title = null;
            String body = null;
            String type = null;
            String dataJson = null;
            if (jsonObject.has("title"))
                title = jsonObject.getString("title");
            if (jsonObject.has("body"))
                body = jsonObject.getString("body");
            if (jsonObject.has("type"))
                type = jsonObject.getString("type");
            if (jsonObject.has("data")) {
                dataJson = jsonObject.getString("data");
            }
            if (type == null || dataJson == null)
                return;

            Gson gson = new Gson();

            if (type.equals(Constants.SignalTypes.NEW_MESSAGE)) {
                ChatMessage receivedItem = gson.fromJson(dataJson, ChatMessage.class);

                intent = ActivityChat.createIntent(context,
                        new ChatItem(receivedItem), false, true, false);
                /*if (Constants.IS_LOGGED_IN)
                    intent = ActivityChat.createIntent(context, receivedItem.getChatTypeId() == GROUP_CHAT,
                            new ChatItem(receivedItem), null, false, false);
                else
                    intent = ActivityLogin.createIntent(context, receivedItem);*/
                if (receivedItem == null || SharedData.getInstance().getActiveChatId() == -1) {
                    if (title == null)
                        title = receivedItem.getFromUser();
                    body = receivedItem.getMessage();

                    NotificationUtils.showNotification(context, intent, title, body/*, receivedItem.getMessageId()*/);
                } else {
                    NotificationUtils.vibrateForNotification(context);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void vibrateForNotification(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }


    private static void initNotificationManager(Context context) {

        if (notificationManager == null)
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public static void dismissNotification(Context context, int notId) {
        initNotificationManager(context);
        notificationManager.cancel(notId);
    }

    public static void showNotification(Context context, Intent intent, String title, String description) {

        createNotificationChannel(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(description)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setTicker(description)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.mini_logo)

                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)

                .setAutoCancel(true)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});


// notificationId is a unique int for each notification that you must define
        notificationManager.notify(Constants.Notifications.NOTIFICATION_ID, builder.build());
    }

    private static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        initNotificationManager(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "DocApp";
            String description = "DocApp LLC";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }
    }
}
