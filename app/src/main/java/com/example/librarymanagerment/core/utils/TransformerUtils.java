package com.example.librarymanagerment.core.utils;

import android.util.Log;

import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.dto.TransactionDTO;
import com.example.librarymanagerment.core.dto.TransactionSheetDTO;
import com.example.librarymanagerment.core.sco.TransactionSCO;
import com.example.librarymanagerment.core.sco.TransactionSheetSCO;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class TransformerUtils {

    public static TransactionSheetDTO convertTransactionDTOtoTransactionSheetDTO(TransactionDTO transactionDTO){
        TransactionSheetDTO transactionSheetDTO = new TransactionSheetDTO();

        transactionSheetDTO.setId(transactionDTO.getId());
        transactionSheetDTO.setListBookValue(transactionDTO.getJsonListBook());
        transactionSheetDTO.setStudentName(transactionDTO.getStudentName());
        transactionSheetDTO.setClassRoom(transactionDTO.getClassRoom());

        transactionSheetDTO.setCreatedDate(transactionDTO.getCreateDate());
        transactionSheetDTO.setFromDate(transactionDTO.getFromDate());
        transactionSheetDTO.setToDate(transactionDTO.getToDate());

        transactionSheetDTO.setFromDateValue(convertStringDateToInteger(transactionDTO.getFromDate()));
        transactionSheetDTO.setToDateValue(convertStringDateToInteger(transactionDTO.getToDate()));

        transactionSheetDTO.setStatus(transactionDTO.getStatus());
        transactionSheetDTO.setDeleteFlag(DBConstants.STATUS_NONE_DELETE);
        transactionSheetDTO.setStaffName(transactionDTO.getStaffName());
        transactionSheetDTO.setStudentId(transactionDTO.getStudentId());

        return transactionSheetDTO;
    }

    public static TransactionSheetSCO convertTransactionSCOtoTransactionSheetSCO(TransactionSCO transactionSCO){
        TransactionSheetSCO transactionSheetSCO = new TransactionSheetSCO();
        transactionSheetSCO.setMinDate(convertStringDateToInteger(transactionSCO.getMinDate()).toString());
        transactionSheetSCO.setMaxDate(convertStringDateToInteger(transactionSCO.getMaxDate()).toString());
        transactionSheetSCO.setStatus(transactionSCO.getStatus());
        transactionSheetSCO.setClassRoom(transactionSCO.getClassRoom());
        transactionSheetSCO.setName(transactionSCO.getName());
        return transactionSheetSCO;
    }

    private static Integer convertStringDateToInteger(String dateFormatted){
        StringBuilder stringDate = new StringBuilder();
        String[] array = dateFormatted.split("-");
        for (int i = 0; i < array.length; i++) {
            stringDate.append(array[i]);
        }
        return Integer.parseInt(stringDate.toString());
    }

    public static final Map<String, String> dtoToPayload (Object object, String action){
        Map<String, String> result = ObjectMapperUtils.dtoToMap(object, new TypeReference<Map<String, String>>(){});
        result.put("action", action);
        Log.e("log", ObjectMapperUtils.dtoToString(result));
        return result;
    }
}
