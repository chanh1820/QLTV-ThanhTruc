package com.example.librarymanagerment.core.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.librarymanagerment.core.dto.BookDTO;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class StringUtils {

    public static final String genBooksByListBook(List<BookDTO> bookDTOList) {
        StringBuilder listBookName = new StringBuilder();
        boolean flag = false;
        for (BookDTO bookDTO : bookDTOList) {
            if (flag) {
                listBookName.append("\n");
            }
            listBookName.append("(" + bookDTO.getQuantity() + ")" + bookDTO.getName());
            flag = true;
        }
        return listBookName.toString();
    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] arr = baos.toByteArray();
        String result = Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }

    public static Bitmap StringToBitMap(String image) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
