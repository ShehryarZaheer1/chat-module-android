package com.xevensolutions.chat.utils;

import android.app.Activity;

import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.xevensolutions.baseapp.utils.GenericUtils;
import com.xevensolutions.chat.interfaces.UriToPathListener;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.models.ChatItem;
import com.xevensolutions.chat.models.ChatMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DataProvider {


    public static PickiT getPickIt(Activity activity, UriToPathListener uriToPathListener) {
        return new PickiT(activity, (PickiTCallbacks) uriToPathListener, activity);
    }

    public static HashMap<String, Integer> getFilesIcons() {
        HashMap<String, Integer> fileIcons = new HashMap<>();
        fileIcons.putAll(getDocumentIcons());
        fileIcons.put("zip", R.drawable.ic_pdf);
        return fileIcons;
    }

    public static List<ChatItem> moveFavsToTop(List<ChatItem> newChats) {
        if (GenericUtils.isArrayEmpty(newChats))
            return new ArrayList<>();
        List<ChatItem> unFavItems = new ArrayList<>();
        List<ChatItem> favItems = new ArrayList<>();
        for (ChatItem chatItem : newChats) {
            if (chatItem.isFavorite()) {
                favItems.add(chatItem);

            } else
                unFavItems.add(chatItem);
        }
        newChats.clear();
        newChats.addAll(favItems);
        newChats.addAll(unFavItems);

        return newChats;
    }
    public static HashMap<String, Integer> getDocumentIcons() {
        HashMap<String, Integer> documentIcons = new HashMap<>();
        documentIcons.put("pdf", R.drawable.ic_pdf);
        documentIcons.put("doc", R.drawable.icon_file_doc);
        documentIcons.put("docx", R.drawable.icon_file_doc);
        documentIcons.put("ppt", R.drawable.icon_file_ppt);
        documentIcons.put("pptx", R.drawable.icon_file_ppt);
        documentIcons.put("xls", R.drawable.icon_file_xls);
        documentIcons.put("xlsx", R.drawable.icon_file_xls);
        return documentIcons;
    }

    public static List<String> getSupportedVideoFormats() {
        List<String> list = new ArrayList<>();
        list.add("mp4");
        list.add("avi");
        list.add("mkv");
        list.add("m4a");
        list.add("3gp");
        list.add("mov");
        return list;
    }

    public static List<String> getSupportedAudioFormats() {
        List<String> list = new ArrayList<>();
        list.add("mp3");
        list.add("wav");
        list.add("3gp");

        return list;
    }

    public static int getDocumentIcon(String fileName) {

        Integer icon = getFilesIcons().get(getFileExtension(fileName));
        if (icon == null)
            icon = R.drawable.icon_file_unknown;

        return icon;
    }

    public static String getFileExtension(String fullNameWithExtension) {
        if (fullNameWithExtension == null)
            return "";
        List<String> nameDetails = Arrays.asList(fullNameWithExtension.split("\\.(?=[^\\.]+$)"));
        if (nameDetails.size() == 2) {
            return nameDetails.get(1);
        } else {
            return nameDetails.get(0);
        }

    }

    public static List<ChatMessage> cloneMessages(List<ChatMessage> items) {
        List<ChatMessage> clonedList = new ArrayList<>();
        for (ChatMessage t : items) {
            clonedList.add(t.clone());
        }
        return clonedList;
    }
}
