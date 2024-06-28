package com.example.librarymanagerment.core.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.dto.TransactionDTO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ExcelUtils {

    public static void requestPermission(Context context, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
    }

    public static String exportExcel(List<TransactionDTO> transactionDTOList, String fileName, String title) {
        File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName + ".xls");

        String absolutePath = filePath.getAbsolutePath();

        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("sheet 1");


        HSSFRow hssfTitleTile = hssfSheet.createRow(1);
        HSSFCell hssfCellTitle = hssfTitleTile.createCell(8);
        hssfCellTitle.setCellValue( title);

//        HSSFRow hssfTitleFrom = hssfSheet.createRow(1);
//        HSSFCell hssfCellFrom = hssfTitleFrom.createCell(9);
//        hssfCellFrom.setCellValue(DBConstants.mapTuan.get(Integer.parseInt(week)));

        for (int i = -1; i < transactionDTOList.size(); i++) {
            HSSFRow hssfRow = hssfSheet.createRow(i + 5);

            if (i == -1) {
                HSSFCell hssfCell0 = hssfRow.createCell(0);
                HSSFCell hssfCell1 = hssfRow.createCell(1);
                HSSFCell hssfCell2 = hssfRow.createCell(2);
                HSSFCell hssfCell3 = hssfRow.createCell(3);
                HSSFCell hssfCell4 = hssfRow.createCell(4);
                HSSFCell hssfCell5 = hssfRow.createCell(5);
                HSSFCell hssfCell6 = hssfRow.createCell(6);
                HSSFCell hssfCell7 = hssfRow.createCell(7);
                HSSFCell hssfCell8 = hssfRow.createCell(8);

                hssfCell0.setCellValue("STT");
                hssfCell1.setCellValue("Tên người mượn");
                hssfCell2.setCellValue("Lớp/Vai trò");
                hssfCell3.setCellValue("Danh sách Sách");
                hssfCell4.setCellValue("Ngày mượn");
                hssfCell5.setCellValue("Ký mượn");
                hssfCell6.setCellValue("Ngày trả");
                hssfCell7.setCellValue("Ký trả");
                hssfCell8.setCellValue("Trạng thái");
            } else {
                TransactionDTO transactionDTO = transactionDTOList.get(i);
                HSSFCell hssfCell0 = hssfRow.createCell(0);
                HSSFCell hssfCell1 = hssfRow.createCell(1);
                HSSFCell hssfCell2 = hssfRow.createCell(2);
                HSSFCell hssfCell3 = hssfRow.createCell(3);
                HSSFCell hssfCell4 = hssfRow.createCell(4);
                HSSFCell hssfCell5 = hssfRow.createCell(5);
                HSSFCell hssfCell6 = hssfRow.createCell(6);
                HSSFCell hssfCell7 = hssfRow.createCell(7);
                HSSFCell hssfCell8 = hssfRow.createCell(8);

                hssfCell0.setCellValue(i + 1);
                hssfCell1.setCellValue(transactionDTO.getStudentName());
                hssfCell2.setCellValue(transactionDTO.getClassRoom());
                hssfCell3.setCellValue(StringUtils.genBooksByListBook(transactionDTO.getListBookDTO()));
                hssfCell4.setCellValue(transactionDTO.getFromDate());
                hssfCell5.setCellValue("");
                hssfCell6.setCellValue(transactionDTO.getToDate());
                hssfCell7.setCellValue("");
                hssfCell8.setCellValue(
                        transactionDTO.getStatus().equals(DBConstants.TRANSACTION_STATUS_NOT_REFUNDED) ? "Chưa trả" : "Đã trả"
                );
            }


        }

        try {
            if (!filePath.exists()) {
                filePath.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return absolutePath;
    }
}
