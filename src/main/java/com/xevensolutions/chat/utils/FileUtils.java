package com.xevensolutions.chat.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;


import com.xevensolutions.chat.R;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static com.xevensolutions.chat.utils.TextUtils.isStringEmpty;


/**
 * Created by umair on 8/19/17.
 */

public class FileUtils {


    public static boolean isFileImage(String filePath) {
        if (filePath == null)
            return false;

        else {
            filePath = filePath.toLowerCase();
            if (filePath.endsWith(".png") || filePath.endsWith(".jpeg")
                    || filePath.endsWith(".jpg"))
                return true;
        }
        return false;


    }

    public static boolean isFileZip(String filePath) {
        if (filePath == null)
            return false;
        else if (filePath.endsWith(".zip"))
            return true;

        return false;


    }

    public static boolean isFileDocument(String filePath) {
        if (filePath == null)
            return false;
        HashMap<String, Integer> documentExtensions = DataProvider.getDocumentIcons();
        String extension = DataProvider.getFileExtension(filePath);
        return documentExtensions.get(extension) != null;

    }

    public static boolean isFileVideo(String eitherFilePath) {
        if (eitherFilePath == null)
            return false;
        String extension = DataProvider.getFileExtension(eitherFilePath);
        if (isStringEmpty(extension))
            return false;
        else
            return DataProvider.getSupportedVideoFormats().contains(extension.toLowerCase());
    }

    public static boolean isFileAudio(String eitherFilePath) {
        if (eitherFilePath == null)
            return false;
        String extension = DataProvider.getFileExtension(eitherFilePath);
        if (isStringEmpty(extension))
            return false;
        else
            return DataProvider.getSupportedAudioFormats().contains(extension.toLowerCase());


    }

    public static String getUploadPathForFile(String localPath, String folderName) {
        return Constants.FILES_BASE_URL + folderName + getFileNameFromPath(localPath);
    }

    public static String getFileNameFromUri(Context activity, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static void shareTextAcrossApps(Activity activity, String text) {
        String shareBody = text;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        activity.startActivity(Intent.createChooser(sharingIntent,
                activity.getResources().getString(R.string.share_using)));
    }

    public static void downloadFile(Context context, String url, String title, String destination) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downlaodUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(downlaodUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("Downloading " + title);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(destination, title);
        downloadManager.enqueue(request);


    }

    public static String getImageRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public static String getFileNameFromPath(String path) {
        String filename = path.substring(path.lastIndexOf("/") + 1);
        return filename;
    }

    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        if (!url.startsWith("http"))
            url = Constants.BLOB_PATH + url;
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        } catch (MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }


    public static Uri getUriFromFilePath(String path) {
        return Uri.fromFile(new File(path));
    }


}
