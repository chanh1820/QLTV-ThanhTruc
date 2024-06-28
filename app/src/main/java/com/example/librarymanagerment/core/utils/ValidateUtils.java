package com.example.librarymanagerment.core.utils;

import android.content.Context;
import android.util.Log;

import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.BookCollectionDTO;
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.dto.LoginDTO;
import com.example.librarymanagerment.core.dto.TransactionDTO;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class ValidateUtils {

    public static boolean validateBookDTO(BookDTO bookDTO) {
        if (bookDTO.getCode().trim().isEmpty()) {
            return false;
        }
        if (bookDTO.getName().trim().isEmpty()) {
            return false;
        }
        if (bookDTO.getLocate().trim().isEmpty()) {
            return false;
        }
        if (bookDTO.getQuantity() == null) {
            return false;
        }
        return true;

    }

    public static boolean validateBookCollectionDTO(BookCollectionDTO bookCollectionDTO) {
        if (bookCollectionDTO.getCode().trim().isEmpty()) {
            return false;
        }
        if (bookCollectionDTO.getName().trim().isEmpty()) {
            return false;
        }
        return true;

    }

    public static boolean validateTransactionDTO(TransactionDTO transactionDTO, GeneralDAO generalDAO, Context context) {
        Log.e("validateTransactionDTO", ObjectMapperUtils.dtoToString(transactionDTO));
        if (transactionDTO.getListBookDTO().size() == 0) {
            return false;
        }
        if (transactionDTO.getStudentName().trim().isEmpty()) {
            return false;
        }
        if (transactionDTO.getClassRoom().trim().isEmpty()) {
            return false;
        }
        if (transactionDTO.getToDate().trim().isEmpty()) {
            return false;
        }

        for (BookDTO item : transactionDTO.getListBookDTO()) {
            BookDTO itemInDB  = generalDAO.findBookById(item.getId());
            if(item.getQuantity() <= itemInDB.getQuantity()){

            }else {
                NotifyUtils.defaultNotify(context, item.getName()+" vượt quá số lượng");
                return false;
            }
        }
        return true;

    }

    public static final boolean isValidLoginDTO(LoginDTO loginDTO, Context context){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(loginDTO.getUserName())){
            if (StringUtils.isNotBlank(loginDTO.getPassWord())){
                return true;
            }else {
                NotifyUtils.defaultNotify(context, "Mật khẩu không được để trống");
                return false;
            }
        }else {
            NotifyUtils.defaultNotify(context, "Tên đăng nhập không được để trống");
            return false;
        }
    }

    private Pattern patternUserName;
    private Pattern patternPassword;
    private static final String USERNAME_PATTERN = "^[a-z0-9._-]{3,15}$";
    private static final String PASSWORD_PATTERN = "((?=.*[a-z]).{3,50})";

    public ValidateUtils() {
        patternUserName = Pattern.compile(USERNAME_PATTERN);
        patternPassword = Pattern.compile(PASSWORD_PATTERN);

    }

    public boolean validateUserName(final String username) {
        return patternUserName.matcher(username).matches();
    }


    public boolean validatePassword(final String password) {
        return patternPassword.matcher(password).matches();
    }
}
