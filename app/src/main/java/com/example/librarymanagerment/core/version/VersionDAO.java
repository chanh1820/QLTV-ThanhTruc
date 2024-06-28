package com.example.librarymanagerment.core.version;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.librarymanagerment.core.DBHelper;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.dto.BookCollectionDTO;
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.dto.BookReadingDTO;
import com.example.librarymanagerment.core.dto.ClassRoomDTO;
import com.example.librarymanagerment.core.dto.OrderPickedDTO;
import com.example.librarymanagerment.core.dto.StudentDTO;
import com.example.librarymanagerment.core.dto.TransactionDTO;
import com.example.librarymanagerment.core.dto.VideoDTO;
import com.example.librarymanagerment.core.sco.TransactionSCO;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class VersionDAO {

    DBVersionHelper dbVersionHelper;
    DBHelper dbHelper;

    public VersionDAO(Context context) {
        dbVersionHelper = new DBVersionHelper(context);
        dbHelper = new DBHelper(context);
    }

    public List<VersionDTO> findAllVersion() {
        List<VersionDTO> versionDTOList = new ArrayList<>();
        SQLiteDatabase db = dbVersionHelper.getReadableDatabase();
        String sql = "SELECT * FROM version_tbl";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                VersionDTO item = new VersionDTO();
                item.setId(cursor.getInt(0));
                item.setVersionCode(cursor.getString(1));
                item.setVersionIndex(cursor.getString(2));
                versionDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return versionDTOList;
    }
    void updateVersion(VersionDTO versionDTO){
        SQLiteDatabase db = dbVersionHelper.getWritableDatabase();
        String sql = "";
        sql = "UPDATE version_tbl "
                +"SET version_index = '"+versionDTO.getVersionIndex()
                +"' WHERE version_code = '"+versionDTO.getVersionCode()+"'";
        Log.e("sql", sql);
        db.execSQL(sql);
        db.close();
    }

    void deleteAllBook(){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = "";
            sql = "DELETE FROM book_tbl";
            Log.e("sql", sql);
            db.execSQL(sql);
            db.close();
    }

    void deleteAllBookCollection(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "";
        sql = "DELETE FROM book_collection_tbl";
        Log.e("sql", sql);
        db.execSQL(sql);
        db.close();
    }

    void deleteAllVideo(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "";
        sql = "DELETE FROM video_tbl";
        Log.e("sql", sql);
        db.execSQL(sql);
        db.close();
    }

    void deleteAllStudent(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "";
        sql = "DELETE FROM student_tbl";
        Log.e("sql", sql);
        db.execSQL(sql);
        db.close();
    }
    void deleteAllBookReading(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "";
        sql = "DELETE FROM book_reading_tbl";
        Log.e("sql", sql);
        db.execSQL(sql);
        db.close();
    }
    ////////////////////////////////////////////////////////////////////////////////////////

    public void saveALlBookDTO(List<BookDTO> bookDTOList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (BookDTO bookDTO : bookDTOList){
            ContentValues values = new ContentValues();
            values.put("_id", bookDTO.getId());
            values.put("book_code", bookDTO.getCode());
            values.put("book_name", bookDTO.getName());
            values.put("book_description", bookDTO.getDescription());
            values.put("locate", bookDTO.getLocate());
            values.put("quantity", bookDTO.getQuantity());
            values.put("collection_code", bookDTO.getCollectionCode());
            values.put("image_base_64", bookDTO.getImageBase64());
            values.put("file_name", bookDTO.getImageFile());
            values.put("class_room", bookDTO.getClassRoom());
            values.put("collection_parent", bookDTO.getCollectionParent());
            db.insert("book_tbl", null, values);
        }
        db.close();
    }

    public void saveALlBookCollection(List<BookCollectionDTO> bookDTOList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (BookCollectionDTO item : bookDTOList){
            ContentValues values = new ContentValues();
            values.put("_id", item.getId());
            values.put("book_collection_code", item.getCode());
            values.put("book_collection_name", item.getName());
            values.put("book_collection_description", item.getDescription());
            db.insert("book_collection_tbl", null, values);
        }
        db.close();
    }

    public void saveALlVideo(List<VideoDTO> videoDTOList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (VideoDTO item : videoDTOList){
            ContentValues values = new ContentValues();
            values.put("name", item.getName());
            values.put("thumbnail", item.getThumbnail());
            values.put("link", item.getLink());
            values.put("link_web", item.getLinkWeb());
            values.put("category", item.getCategory());
            values.put("class_room", item.getClassRoom());
            db.insert("video_tbl", null, values);
        }
        db.close();
    }

    public void saveAllStudent(List<StudentDTO> studentDTOList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (StudentDTO item : studentDTOList){
            ContentValues values = new ContentValues();
            values.put("student_code", item.getStudentCode());
            values.put("student_name", item.getStudentName());
            values.put("class_room", item.getClassRoom());
            values.put("borrowed_flag", DBConstants.STATUS_NONE_BORROWED);
            db.insert("student_tbl", null, values);
        }
        db.close();
    }

    public void saveAllBookReading(List<BookReadingDTO> bookReadingDTOList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (BookReadingDTO item : bookReadingDTOList){
            ContentValues values = new ContentValues();
            values.put("book_reading_code", item.getCode());
            values.put("book_reading_name", item.getName());
            values.put("book_reading_type", item.getType());
            values.put("book_reading_resource", item.getResource());
            values.put("book_reading_image", item.getImageFile());
            values.put("book_collection_code", item.getCollectionCode());
            values.put("class_room", item.getClassRoom());
            db.insert("book_reading_tbl", null, values);
        }
        db.close();
    }
}

