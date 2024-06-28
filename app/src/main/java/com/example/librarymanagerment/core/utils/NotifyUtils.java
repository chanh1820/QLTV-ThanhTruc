package com.example.librarymanagerment.core.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.librarymanagerment.core.constants.GoogleSheetConstants;


public class NotifyUtils {

    public static final void defaultNotify(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static final void notifyByMessage(Context context, String messageCode){
        String message = GoogleSheetConstants.MESSAGE_CODE_MAP.get(messageCode);
        defaultNotify(context, message);
    }


}
