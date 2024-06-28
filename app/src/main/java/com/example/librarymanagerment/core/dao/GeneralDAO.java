package com.example.librarymanagerment.core.dao;

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
import com.example.librarymanagerment.core.sco.BookReadingSCO;
import com.example.librarymanagerment.core.sco.TransactionSCO;
import com.example.librarymanagerment.core.sco.VideoSCO;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GeneralDAO {

    DBHelper dbHelper;

    public GeneralDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public Integer saveBookDTO(BookDTO bookDTO) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        int id = -1;

        values.put("_id", bookDTO.getId());
        values.put("book_code", bookDTO.getCode());
        values.put("book_name", bookDTO.getName());
        values.put("book_description", bookDTO.getDescription());
        values.put("locate", bookDTO.getLocate());
        values.put("quantity", bookDTO.getQuantity());
        values.put("collection_code", bookDTO.getCollectionCode());
        values.put("image_base_64", bookDTO.getImageBase64());

        if (bookDTO.getId() == null) {
            Log.e("log", "insert book");
            id = (int) db.insert("book_tbl", null, values);

        } else {
            Log.e("log", "update book");
            id = db.update(
                    "book_tbl",
                    values,
                    "_id = ?",
                    new String[]{String.valueOf(bookDTO.getId())}
            );
        }

        db.close();
        return id;
    }
    public void deleteBookDTO(Integer id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete(
                "book_tbl",
                "_id = ?",
                new String[]{String.valueOf(id)}
        );
        db.close();
    }
    public List<BookDTO> findAllBook() {
        List<BookDTO> bookDTOList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM book_tbl";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                BookDTO item = new BookDTO();
                ;
                item.setId(cursor.getInt(0));
                item.setCode(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setLocate(cursor.getString(4));
                item.setQuantity(cursor.getInt(5));
                item.setCollectionCode(cursor.getString(6));
                item.setImageBase64(cursor.getString(7));
                item.setClassRoom(cursor.getString(8));
                item.setImageFile(cursor.getString(9));
                bookDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return bookDTOList;
    }
    public BookDTO findBookById(Integer id) {
        List<BookDTO> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM book_tbl WHERE _id = "+id;
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                BookDTO item = new BookDTO();
                ;
                item.setId(cursor.getInt(0));
                item.setCode(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setLocate(cursor.getString(4));
                item.setQuantity(cursor.getInt(5));
                item.setCollectionCode(cursor.getString(6));
                item.setImageBase64(cursor.getString(7));
                item.setClassRoom(cursor.getString(8));
                item.setImageFile(cursor.getString(9));
                result.add(item);
            } while (cursor.moveToNext());
        }
        if(result.isEmpty()){
            return null;
        }else {
            return result.get(0);
        }
    }

    public BookDTO findBookByCode(String code) {
        List<BookDTO> bookDTOList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM book_tbl WHERE book_code = '"+ code +"'";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                BookDTO item = new BookDTO();
                ;
                item.setId(cursor.getInt(0));
                item.setCode(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setLocate(cursor.getString(4));
                item.setQuantity(cursor.getInt(5));
                item.setCollectionCode(cursor.getString(6));
                item.setImageBase64(cursor.getString(7));
                item.setClassRoom(cursor.getString(8));
                item.setImageFile(cursor.getString(9));

                bookDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return bookDTOList.get(0);
    }

    public List<BookDTO> findBooksByCollectionCode(String code) {
        List<BookDTO> bookDTOList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM book_tbl WHERE collection_code = '"+ code +"'";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                BookDTO item = new BookDTO();
                ;
                item.setId(cursor.getInt(0));
                item.setCode(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setLocate(cursor.getString(4));
                item.setQuantity(cursor.getInt(5));
                item.setCollectionCode(cursor.getString(6));
                item.setImageBase64(cursor.getString(7));
                item.setClassRoom(cursor.getString(8));
                item.setImageFile(cursor.getString(9));

                bookDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return bookDTOList;
    }
    public List<BookDTO> findBooksBySQLChildCode(String sqlListChildCode, String key) {
        List<BookDTO> bookDTOList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM book_tbl WHERE collection_code IN "+ sqlListChildCode +" AND book_name LIKE '%"+ key +"%'";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                BookDTO item = new BookDTO();
                ;
                item.setId(cursor.getInt(0));
                item.setCode(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setLocate(cursor.getString(4));
                item.setQuantity(cursor.getInt(5));
                item.setCollectionCode(cursor.getString(6));
                item.setImageBase64(cursor.getString(7));
                item.setClassRoom(cursor.getString(8));
                item.setImageFile(cursor.getString(9));
                bookDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return bookDTOList;
    }

    public List<BookDTO> findBooksBySQLChildCodeAndClass(String sqlListChildCode, String classRoom, String key) {
        List<BookDTO> bookDTOList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM book_tbl WHERE collection_code IN "+ sqlListChildCode + " AND class_room = '"+ classRoom+"' AND book_name LIKE '%"+ key +"%'";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                BookDTO item = new BookDTO();
                ;
                item.setId(cursor.getInt(0));
                item.setCode(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setLocate(cursor.getString(4));
                item.setQuantity(cursor.getInt(5));
                item.setCollectionCode(cursor.getString(6));
                item.setImageBase64(cursor.getString(7));
                item.setClassRoom(cursor.getString(8));
                item.setImageFile(cursor.getString(9));
                bookDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return bookDTOList;
    }
    public List<BookDTO> searchBookByKey(String key) {
        List<BookDTO> bookDTOList = new ArrayList<>();
        if(key.trim().equals("")){
            return bookDTOList;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM book_tbl WHERE book_name LIKE '%"+ key +"%'";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                BookDTO item = new BookDTO();
                ;
                item.setId(cursor.getInt(0));
                item.setCode(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setDescription(cursor.getString(3));
                item.setLocate(cursor.getString(4));
                item.setQuantity(cursor.getInt(5));
                item.setCollectionCode(cursor.getString(6));

                bookDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return bookDTOList;
    }
    public Long saveBookCollectionDTO(BookCollectionDTO bookCollectionDTO) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Long id = Long.valueOf(-1);
        values.put("_id", bookCollectionDTO.getId());
        values.put("book_collection_code", bookCollectionDTO.getCode());
        values.put("book_collection_name", bookCollectionDTO.getName());
        values.put("book_collection_description", bookCollectionDTO.getDescription());


        if (bookCollectionDTO.getId() == null) {
            id = db.insert("book_collection_tbl", null, values);
        } else {
            db.update(
                    "book_collection_tbl",
                    values,
                    "_id = ?",
                    new String[]{String.valueOf(bookCollectionDTO.getId())}
            );
        }
        db.close();
        return id;
    }

    public List<BookCollectionDTO> findAllBookCollection() {
        List<BookCollectionDTO> bookCollectionDTOList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM book_collection_tbl";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                BookCollectionDTO item = new BookCollectionDTO();
                ;
                item.setId(cursor.getInt(0));
                item.setCode(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setDescription(cursor.getString(3));

                bookCollectionDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return bookCollectionDTOList;
    }

    public void deleteBookCollectionDTO(Integer id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete(
                "book_collection_tbl",
                "_id = ?",
                new String[]{String.valueOf(id)}
        );
        db.close();
    }

    public int saveTransactionDTO(TransactionDTO transactionDTO) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        int id = -1;

        values.put("_id", transactionDTO.getId());
        values.put("list_book_value", transactionDTO.getJsonListBook());
        values.put("student_name", transactionDTO.getStudentName());
        values.put("class_room", transactionDTO.getClassRoom());
        values.put("create_date", transactionDTO.getCreateDate());
        values.put("from_date", transactionDTO.getFromDate());
        values.put("to_date", transactionDTO.getToDate());
        values.put("status", transactionDTO.getStatus());
        values.put("delete_flag", transactionDTO.getStatus());
        values.put("staff_name", "admin");

        if (transactionDTO.getId() == null) {
            id = (int) db.insert("transaction_tbl", null, values);
        } else {
            id = db.update(
                    "transaction_tbl",
                    values,
                    "_id = ?",
                    new String[]{String.valueOf(transactionDTO.getId())}
            );
        }
        db.close();
        return id;
    }
    public List<TransactionDTO> searchTransaction(TransactionSCO transactionSCO) {
        List<TransactionDTO> transactionDTOList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        String min_date = transactionSCO.getMinDate();
        String max_date = transactionSCO.getMaxDate();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM transaction_tbl WHERE from_date >='" +  min_date + "'AND  from_date<='" + max_date + "'";
//        String sql = "SELECT * FROM transaction_tbl";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                TransactionDTO item = new TransactionDTO();

                item.setId(cursor.getInt(0));
                item.setJsonListBook(cursor.getString(1));
                try {
                    item.setListBookDTO(mapper.readValue(item.getJsonListBook(), new TypeReference<List<BookDTO>>(){}));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                item.setStudentName(cursor.getString(2));
                item.setClassRoom(cursor.getString(3));
                item.setCreateDate(cursor.getString(4));
                item.setFromDate(cursor.getString(5));
                item.setToDate(cursor.getString(6));
                item.setStatus(cursor.getInt(7));

                transactionDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return transactionDTOList;
    }

    public List<ClassRoomDTO> getClassRoomByStudentList(boolean isIncludeOptAll) {
        List<ClassRoomDTO> classRoomDTOList = new ArrayList<>();
        if(isIncludeOptAll){
            classRoomDTOList.add(new ClassRoomDTO(0, DBConstants.ITEM_ALL));
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT DISTINCT class_room FROM student_tbl WHERE class_room NOT NULL";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                ClassRoomDTO item = new ClassRoomDTO();

                item.setId(0);
                item.setClassRoom(cursor.getString(0));
                classRoomDTOList.add(item);
            } while (cursor.moveToNext());
        }
        Log.e("classRoomDTOList.size", classRoomDTOList.size()+"");
        return classRoomDTOList;
    }
    public List<String> getCollectionParentByListBook() {
        List<String> result = new ArrayList<>();
        result.add(DBConstants.ITEM_ALL);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT DISTINCT collection_parent FROM book_tbl WHERE collection_parent NOT NULL";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        Log.e("getCollectionParentByListBook", ObjectMapperUtils.dtoToString(result));
        return result;
    }

    public List<String> findClassRoomByListBook() {
        List<String> result = new ArrayList<>();
        result.add(DBConstants.ITEM_ALL);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT DISTINCT class_room FROM book_tbl WHERE class_room NOT NULL";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        Log.e("getCollectionParentByListBook", ObjectMapperUtils.dtoToString(result));
        return result;
    }
    public List<String> getCollectionParentByListBookReading() {
        List<String> result = new ArrayList<>();
        result.add(DBConstants.ITEM_ALL);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT DISTINCT book_collection_code FROM book_reading_tbl WHERE book_collection_code NOT NULL";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        Log.e("getCollectionParentByListBookReading", ObjectMapperUtils.dtoToString(result));
        return result;
    }
    public List<String> getClassRoomByListBookReading() {
        List<String> result = new ArrayList<>();
        result.add(DBConstants.ITEM_ALL);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT DISTINCT class_room FROM book_reading_tbl WHERE class_room NOT NULL";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        Log.e("getClassRoomByListBookReading", ObjectMapperUtils.dtoToString(result));
        return result;
    }
    public List<StudentDTO> findStudentByClassRoom(String classRoom) {
        List<StudentDTO> studentDTOList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM student_tbl WHERE class_room = '" + classRoom +"'"+ " AND borrowed_flag = " + DBConstants.STATUS_NONE_BORROWED;
        Log.e("findStudentByClassRoom", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                StudentDTO item = new StudentDTO();

                item.setId(cursor.getInt(0));
                item.setStudentCode(cursor.getString(1));
                item.setStudentName(cursor.getString(2));
                item.setClassRoom(cursor.getString(3));
                item.setBorrowedFlag(cursor.getInt(4));

                studentDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return studentDTOList;
    }


    public int updateStudentDTO(StudentDTO studentDTO) {
        Log.e("updateStudentDTO", ObjectMapperUtils.dtoToString(studentDTO));
        int id = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "UPDATE student_tbl SET borrowed_flag = " + studentDTO.getBorrowedFlag() + " WHERE _id = " + studentDTO.getId();
        db.execSQL(sql);
        db.close();
        return id;
    }

    public void updateQuantityBook(List<BookDTO> bookDTOList, String operator) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "";
        for (BookDTO item:bookDTOList ) {
             sql = "UPDATE book_tbl SET quantity = quantity" + operator + item.getQuantity() + " " +
                    "WHERE _id = " + item.getId();
            db.execSQL(sql);
        }
        db.close();
    }

    public StudentDTO findStudentById(Integer id) {
        List<StudentDTO> studentDTOList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM student_tbl WHERE _id = " + id;
        Log.e("findStudentById", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                StudentDTO item = new StudentDTO();

                item.setId(cursor.getInt(0));
                item.setStudentName(cursor.getString(1));
                item.setClassRoom(cursor.getString(2));
                item.setBorrowedFlag(cursor.getInt(3));

                studentDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return studentDTOList.get(0);
    }

    public int saveOrderPickedDTO(OrderPickedDTO orderPickedDTO) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        int id = -1;

        values.put("_id", orderPickedDTO.getId());
        values.put("book_data", orderPickedDTO.getBookData());
        values.put("book_name", orderPickedDTO.getBookName());
        values.put("book_code", orderPickedDTO.getBookCode());
        values.put("quantity", orderPickedDTO.getQuantity());
        values.put("status", orderPickedDTO.getStatus());
        values.put("delete_flag", orderPickedDTO.getDeleteFlag());

        if (orderPickedDTO.getId() == null) {
            id = (int) db.insert("order_picked_tbl", null, values);
        } else {
            id = db.update(
                    "order_picked_tbl",
                    values,
                    "_id = ?",
                    new String[]{String.valueOf(orderPickedDTO.getId())}
            );
        }
        db.close();
        return id;
    }

    public Boolean isBookOrderPickedExisting(String bookCode) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT EXISTS(SELECT * FROM order_picked_tbl WHERE book_code = '" + bookCode + "')";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor.getInt(0) != 0;
        } else {
            return null;
        }
    }

    public OrderPickedDTO getBookOrderPickedExisting(String bookCode) {
        OrderPickedDTO orderPickedDTO = new OrderPickedDTO();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM order_picked_tbl WHERE book_code = '" + bookCode + "'";
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                orderPickedDTO.setId(cursor.getInt(0));
                orderPickedDTO.setBookData(cursor.getString(1));
                orderPickedDTO.setBookName(cursor.getString(2));
                orderPickedDTO.setBookCode(cursor.getString(3));
                orderPickedDTO.setQuantity(cursor.getInt(4));
                orderPickedDTO.setStatus(cursor.getInt(5));
                orderPickedDTO.setDeleteFlag(cursor.getInt(6));
                return orderPickedDTO;
            } while (cursor.moveToNext());
        }
        return null;
    }

    public void updateQuantityBookOrderPicked(Integer id, Integer quantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "";
        sql = "UPDATE order_picked_tbl SET quantity = " + quantity +
                " WHERE _id = " + id;
        Log.e("sql", sql);
        db.execSQL(sql);
        db.close();
    }

    public void deleteBookOrderPicked(Integer id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "";
        sql = "DELETE FROM order_picked_tbl" + " WHERE _id = " + id;
        Log.e("sql", sql);
        db.execSQL(sql);
        db.close();
    }

    public List<OrderPickedDTO> findOrderPickedDTList(Integer status, Integer deleteFlag) {
        List<OrderPickedDTO> orderPickedDTOList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM order_picked_tbl WHERE status = " + status + " AND delete_flag = " + deleteFlag;
        Log.e("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                OrderPickedDTO item = new OrderPickedDTO();
//                BookDTO bookDTO = ObjectMapperUtils.stringToDTO(item.getBookData(), BookDTO.class);
                item.setId(cursor.getInt(0));
                item.setBookData(cursor.getString(1));
                item.setBookDTO(ObjectMapperUtils.stringToDTO(cursor.getString(1), BookDTO.class));
                item.setBookName(cursor.getString(2));
                item.setBookCode(cursor.getString(3));
                item.setQuantity(cursor.getInt(4));
                item.setStatus(cursor.getInt(5));
                item.setDeleteFlag(cursor.getInt(6));
                orderPickedDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return orderPickedDTOList;
    }


    public List<BookReadingDTO> findBookReading(BookReadingSCO bookReadingSCO) {
        List<BookReadingDTO> bookDTOList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder sql = new StringBuilder("SELECT * FROM book_reading_tbl WHERE book_reading_resource NOT NULL");
        if(!bookReadingSCO.getClassRoom().equals(DBConstants.ITEM_ALL)
                && StringUtils.isNotBlank(bookReadingSCO.getClassRoom())){
            sql.append(" AND class_room = '"+ bookReadingSCO.getClassRoom()+"'");
        }
        if(!bookReadingSCO.getCollectionParent().equals(DBConstants.ITEM_ALL)
                && StringUtils.isNotBlank(bookReadingSCO.getCollectionParent())){
            sql.append(" AND book_collection_code = '"+ bookReadingSCO.getCollectionParent()+"'");
        }
        if(StringUtils.isNotBlank(bookReadingSCO.getSearchName())){
            sql.append(" AND book_reading_name LIKE '%"+ bookReadingSCO.getSearchName() +"%'");
        }
        Log.e("sql", sql.toString());
        Cursor cursor = db.rawQuery(sql.toString(), null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                BookReadingDTO item = new BookReadingDTO();
                item.setId(cursor.getInt(0));
                item.setCode(cursor.getString(1));
                item.setName(cursor.getString(2));
                item.setType(cursor.getString(3));
                item.setResource(cursor.getString(4));
                item.setImageFile(cursor.getString(5));
                item.setCollectionCode(cursor.getString(6));
                item.setClassRoom(cursor.getString(7));
                bookDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return bookDTOList;
    }

    public List<String> getClassRoomByListVideo() {
        List<String> result = new ArrayList<>();
        result.add(DBConstants.ITEM_ALL);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT DISTINCT class_room FROM video_tbl WHERE class_room NOT NULL";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        Log.e("findClassRoomByListVideo", ObjectMapperUtils.dtoToString(result));
        return result;
    }

    public List<String> getCategoryByListVideo() {
        List<String> result = new ArrayList<>();
        result.add(DBConstants.ITEM_ALL);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT DISTINCT category FROM video_tbl WHERE category NOT NULL";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        Log.e("findCategoryByListVideo", ObjectMapperUtils.dtoToString(result));
        return result;
    }

    public List<VideoDTO> findVideo(VideoSCO videoSCO) {
        List<VideoDTO> videoDTOList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder sql = new StringBuilder("SELECT * FROM video_tbl WHERE link NOT NULL");
        Log.e("findVideo", String.valueOf(videoSCO.getClassRoom().equals(DBConstants.ITEM_ALL)));
        Log.e("findVideo", String.valueOf(videoSCO.getCategory().equals(DBConstants.ITEM_ALL)));
        if(!videoSCO.getClassRoom().equals(DBConstants.ITEM_ALL)
                && StringUtils.isNotBlank(videoSCO.getClassRoom())){
            sql.append(" AND class_room = '"+ videoSCO.getClassRoom()+"'");
        }
        if(!videoSCO.getCategory().equals(DBConstants.ITEM_ALL)
                && StringUtils.isNotBlank(videoSCO.getCategory())){
            sql.append(" AND category = '"+ videoSCO.getCategory()+"'");
        }
        if(StringUtils.isNotBlank(videoSCO.getSearchName())){
            sql.append(" AND name LIKE '%"+ videoSCO.getSearchName() +"%'");
        }
        Log.e("sql", sql.toString());
        Cursor cursor = db.rawQuery(sql.toString(), null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }
        if (cursor.getCount() != 0) {
            do {
                VideoDTO item = new VideoDTO();
                item.setId(cursor.getInt(0));
                item.setName(cursor.getString(1));
                item.setThumbnail(cursor.getString(2));
                item.setLink(cursor.getString(3));
                item.setLinkWeb(cursor.getString(4));
                item.setCategory(cursor.getString(5));
                item.setClassRoom(cursor.getString(6));
                videoDTOList.add(item);
            } while (cursor.moveToNext());
        }
        return videoDTOList;
    }
}

