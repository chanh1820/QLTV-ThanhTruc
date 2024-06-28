package com.example.librarymanagerment.core.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.librarymanagerment.core.constants.DBConstants;

import java.io.IOException;
import java.io.InputStream;

public class AssetUtils {

    static String pathTemplate = "{1}/{2}/{3}/{4}";

    public static final Drawable getImageFromAsset(int pathType, String fileName, Context context) {
        InputStream imageStream = null;
        Drawable drawable;
        try {
            switch (pathType) {
                case DBConstants.TYPE_ASSET_BOOK_LINK:
                    imageStream = context.getAssets().open("book/" + fileName);
                    break;
                default:
                    imageStream = context.getAssets().open("/" + fileName);
                    break;
            }
            if (imageStream == null){
                Log.e("imageStream", "null");
            }

            drawable = Drawable.createFromStream(imageStream, null);
        } catch (IOException ex) {
            return null;
        }
        return drawable;
    }
}
