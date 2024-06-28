package com.example.librarymanagerment.core.constants;

import java.util.HashMap;
import java.util.Map;

public class GoogleSheetConstants {

    /**
     * Url
     */
    public static final String END_POINT_URL = "https://script.google.com/macros/s/AKfycbzgwkroblbW9xv2g_rFvDaDroIfBEaW-S1JkS58d9XRbBRByaP8QU2jPmlqCMeoR2GX/exec";

    /**
     * action
     */

    public static final String ACTION_LOGIN = "login";

    public static final String ACTION_CHANGE_PASSWORD = "change_password";

    public static final String ACTION_SAVE_TRANSACTION = "save_transaction";

    public static final String ACTION_FIND_TRANSACTION_BY_FROM_AND_TO= "find_transaction_by_from_and_to";

    public static final String ACTION_UPDATE_STATUS_TRANSACTION_BY_ID= "update_status_transaction_by_id";

    public static final String ACTION_SAVE_ORDER = "save_order";
    public static final String ACTION_FIND_ORDER_BY_STATUS = "find_order_by_status";
    public static final String ACTION_UPDATE_STATUS_ORDER_BY_ID= "update_status_order_by_id";
    public static final String ACTION_GET_ALL_VERSION= "GET_ALL_VERSION";
    public static final String ACTION_GET_ALL_BOOK = "GET_ALL_BOOK";
    public static final String ACTION_GET_ALL_CATEGORY = "GET_ALL_CATEGORY";
    public static final String ACTION_GET_ALL_VIDEO= "GET_ALL_VIDEO";
    public static final String ACTION_GET_ALL_STUDENT = "GET_ALL_STUDENT";
    public static final String ACTION_GET_ALL_BOOK_READING = "GET_ALL_BOOK_READING";
    public static final String ACTION_FIND_BOOK = "FIND_BOOK";
    public static final String ACTION_FIND_ACCOUNT = "FIND_ACCOUNT";
    public static final String ACTION_DELETE_ACCOUNT = "DELETE_ACCOUNT";
    public static final String ACTION_SAVE_ACCOUNT = "SAVE_ACCOUNT";

    /**
     * Type
     */
    public static final String ACCOUNT_TYPE_ADMIN = "0";

    public static final String ACCOUNT_TYPE_USER = "1";

    public static final String FLAG_IS_LOGIN = "1";

    public static final String FLAG_NONE_LOGIN = "0";

    /**
     * Status Code
     */
    public static final String STATUS_SUCCESS = "200";

    public static final String MES_CODE_LOGIN_PASSWORD_FAIL = "ERR_01";

    public static final String MES_CODE_LOGIN_USER_FAIL = "ERR_02";

    public static final Map<String, String> MESSAGE_CODE_MAP = new HashMap<String, String>(){{
        put(MES_CODE_LOGIN_PASSWORD_FAIL, "Sai mật khẩu");
        put(MES_CODE_LOGIN_USER_FAIL, "Tài khoản không tồn tại");
    }};


}
