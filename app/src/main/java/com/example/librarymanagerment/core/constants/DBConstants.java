package com.example.librarymanagerment.core.constants;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.dto.BookCollectionParentDTO;
import com.example.librarymanagerment.core.dto.BookLinkDTO;
import com.example.librarymanagerment.core.dto.ClassRoomDTO;
import com.example.librarymanagerment.core.dto.RoleDTO;
import com.example.librarymanagerment.core.dto.SettingDTO;
import com.example.librarymanagerment.core.dto.TransactionStatusDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConstants {

    public static final Integer TRANSACTION_STATUS_NOT_REFUNDED = 1;

    public static final Integer TRANSACTION_STATUS_REFUNDED = 2;

    public static final Integer STATUS_NONE_DELETE = 0;

    public static final Integer STATUS_DELETE = 1;

    public static final Integer STATUS_NONE_BORROWED = 0;

    public static final Integer STATUS_BORROWED = 1;

    public static final Integer STATUS_ORDER_PICKER_AVAILABLE = 1;

    public static final Integer STATUS_ORDER_PICKER_NONE_AVAILABLE = 2;

    public static final Integer STATUS_ORDER_WAITING_APPROVE = 1;

    public static final Integer STATUS_ORDER_APPROVED = 2;

    public static final Integer STATUS_ORDER_REJECT = 3;

    public static final Integer STATUS_ORDER_DELETED = 3;

    public static final Integer FLAG_NONE_DELETED = 0;

    public static final Integer FLAG_DELETED = 1;

    public static final Integer IS_ADMIN = 1;

    public static final Integer IS_NONE_ADMIN = 1;
    public static final Integer REQUEST_BY_ACTIVITY = 0;
    public static final Integer REQUEST_BY_ADAPTER = 1;

    public static final int TYPE_ASSET_BOOK_LINK = 1;
    public static final List<SettingDTO> listSettingDTO = new ArrayList<SettingDTO>() {{
        add(new SettingDTO("Sách", R.drawable.ic_baseline_add_circle_24_2));
        add(new SettingDTO("Danh Mục", R.drawable.ic_baseline_add_circle_24_2));
    }};

    public static final List<SettingDTO> listSettingAdmimDTO = new ArrayList<SettingDTO>() {{
        add(new SettingDTO("Tạo tài khoản", R.drawable.ic_baseline_password_24));
        add(new SettingDTO("Mật khẩu", R.drawable.ic_baseline_password_24));
        add(new SettingDTO("Đăng xuất", R.drawable.ic_baseline_logout_24));
    }};
    public static final List<SettingDTO> listSettingDTO2 = new ArrayList<SettingDTO>() {{
        add(new SettingDTO("Mật khẩu", R.drawable.ic_baseline_password_24));
        add(new SettingDTO("Đăng xuất", R.drawable.ic_baseline_logout_24));
    }};
    public static final List<TransactionStatusDTO> TRANSACTION_STATUS_DTO_LIST = new ArrayList<TransactionStatusDTO>() {{
        add(new TransactionStatusDTO(1, 0, "Tất cả"));
        add(new TransactionStatusDTO(2, TRANSACTION_STATUS_NOT_REFUNDED, "Chưa trả"));
        add(new TransactionStatusDTO(3, TRANSACTION_STATUS_REFUNDED, "Đã trả"));
    }};

//    public static final List<BookCollectionParentDTO> BOOK_COLLECTION_PARENT_DTO_LIST = new ArrayList<BookCollectionParentDTO>() {{
//        add(new BookCollectionParentDTO(0, "Tất cả", "('HH6', 'BH6', 'TL6', 'BT6', 'HH7', 'BH7', 'HH8', 'BH8', 'T9', '2B', 'HG6', 'HG7', 'HG8', 'TN', 'SBH', 'TK', 'PL', 'BC')"));
//        add(new BookCollectionParentDTO(1, "Sách giáo khoa", "('HH6', 'BH6', 'TL6', 'BT6', 'HH7', 'BH7', 'HH8', 'BH8', 'T9', '2B')"));
//        add(new BookCollectionParentDTO(2, "Sách nghiệp vụ", "('HG6', 'HG7', 'HG8')"));
//        add(new BookCollectionParentDTO(3, "Truyện thiếu nhi", "('TN')"));
//        add(new BookCollectionParentDTO(4, "Sách về Bác Hồ", "('SBH')"));
//        add(new BookCollectionParentDTO(5, "Sách Pháp luật", "('PL')"));
//        add(new BookCollectionParentDTO(6, "Sách tham khảo", "('TK')"));
//        add(new BookCollectionParentDTO(7, "Báo - Tạp chí", "('BC')"));
//    }};
    public static final List<String> CLASSES_OF_COLLECTION_ONLY = new ArrayList<String>() {{
        add(ITEM_ALL);
    }};


    public static final List<BookLinkDTO> BOOK_LINK_DTO_LIST = new ArrayList<BookLinkDTO>() {{
        add(new BookLinkDTO(1, "https://online.pubhtml5.com/wacwf/kubo/#p=1","Sách 1","ic_book_link_default.png",""));
        add(new BookLinkDTO(1, "https://bom.so/S40Xgb","Sách 2","ic_book_link_default.png",""));
        add(new BookLinkDTO(1, "https://drive.google.com/file/d/13gMAFsYkxkDkRuvaa-q2DUPciCQuEc1S/view?usp=sharing","Sách 2","ic_book_link_default.png",""));
    }};


    public static final String USER_NAME = "qltv_demo";

    public static final String ITEM_ALL = "Tất cả";

    public static final String OPERATOR_INCREASE = "+";

    public static final String OPERATOR_REDUCE = "-";

    public static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    public static List<RoleDTO> ROLE_lIST = new ArrayList<RoleDTO>(){{
        add(new RoleDTO(1, ACCOUNT_TYPE_USER, "Người dùng"));
        add(new RoleDTO(1, ACCOUNT_TYPE_ADMIN, "Admin"));
    }};
    public static final String ACCOUNT_TYPE_ADMIN = "0";

    public static final String ACCOUNT_TYPE_USER = "1";
    public static List<ClassRoomDTO> CLASS_ROOM_lIST = new ArrayList<>();
}
