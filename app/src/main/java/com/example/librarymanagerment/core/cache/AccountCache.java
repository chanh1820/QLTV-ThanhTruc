package com.example.librarymanagerment.core.cache;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.librarymanagerment.core.constants.GoogleSheetConstants;
import com.example.librarymanagerment.core.dto.AccountDTO;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class AccountCache {
    static ObjectMapper objectMapper =  new ObjectMapper();
    public static AccountDTO getCache(Context context) {
        SharedPreferences accountCache = context.getSharedPreferences("account", MODE_PRIVATE);
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setDisplayName(accountCache.getString("display_name", ""));
        accountDTO.setUserName(accountCache.getString("user_name", ""));
        accountDTO.setClassRoom(accountCache.getString("class_room", ""));
        accountDTO.setPassWord(accountCache.getString("pass_word", ""));
        accountDTO.setRole(accountCache.getString("role", ""));
        accountDTO.setFlagLogin(accountCache.getString("flag_login", GoogleSheetConstants.FLAG_NONE_LOGIN));
        try {
            Log.e("accountDTO",objectMapper.writeValueAsString(accountDTO));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accountDTO;
    }
    public static AccountDTO setCache(Context context, AccountDTO accountDTO) {
        Log.e("accountDTO", ObjectMapperUtils.dtoToString(accountDTO));
        SharedPreferences accountCache = context.getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor accountEdit = accountCache.edit();
        accountEdit.putString("display_name", accountDTO.getDisplayName());
        accountEdit.putString("user_name", accountDTO.getUserName());
        accountEdit.putString("pass_word", accountDTO.getPassWord());
        accountEdit.putString("class_room", accountDTO.getClassRoom());
        accountEdit.putString("role", accountDTO.getRole());
        accountEdit.putString("flag_login", GoogleSheetConstants.FLAG_IS_LOGIN);
        accountEdit.commit();
        return accountDTO;
    }
    public static void removeCache(Context context) {
        SharedPreferences accountCache = context.getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor accountEdit = accountCache.edit();
        accountEdit.putString("display_name", "");
        accountEdit.putString("user_name", "");
        accountEdit.putString("pass_word", "");
        accountEdit.putString("class_room", "");
        accountEdit.putString("role", "");
        accountEdit.putString("flag_login", GoogleSheetConstants.FLAG_NONE_LOGIN);
        accountEdit.commit();
    }
}
