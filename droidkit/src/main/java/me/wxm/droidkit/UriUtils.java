package me.wxm.droidkit;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by wuxm on 06/05/2017.
 * Email 380510218@qq.com
 */

public class UriUtils {
    public static InputStream openInputStream(Uri uri, Context context) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // See https://developer.android.com/training/secure-file-sharing/retrieve-info.html#RetrieveFileInfo
    public static OpenableInfo queryOpenableInfo(Uri uri, Context context) {

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        if (cursor == null || !cursor.moveToFirst()) {
            // Fallback: Check if it is a file Uri.
            if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
                File file = new File(uri.getPath());
                return new OpenableInfo(file.getName(), file.length());
            }
            return null;
        }

        OpenableInfo info = new OpenableInfo(
                cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)),
                cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE)));
        cursor.close();
        return info;
    }

    public static class OpenableInfo {

        private String displayName;
        private long size;

        public OpenableInfo(String displayName, long size) {
            this.displayName = displayName;
            this.size = size;
        }

        public String getDisplayName() {
            return displayName;
        }

        public long getSize() {
            return size;
        }
    }
}
