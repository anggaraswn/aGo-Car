package edu.bluejack22_2.agocar.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Calculator {
    public static long getImageSize(Uri uri, Context context) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.SIZE};
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int sizeColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
                return cursor.getLong(sizeColumnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }
}
