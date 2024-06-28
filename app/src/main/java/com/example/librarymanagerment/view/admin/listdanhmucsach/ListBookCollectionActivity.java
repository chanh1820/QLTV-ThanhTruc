package com.example.librarymanagerment.view.admin.listdanhmucsach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.BookCollectionDTO;
import com.example.librarymanagerment.core.utils.ValidateUtils;
import com.example.librarymanagerment.view.admin.main2.Main2Activity;

import java.util.List;

public class ListBookCollectionActivity extends AppCompatActivity {
    GeneralDAO generalDAO;

    private Button btnAddBookCollection, btnBack, btnAdmin;
    private RecyclerView rvListCollectionBook;

    private List<BookCollectionDTO> bookCollectionDTOList;

//    public static Integer flagAdmin = DBConstants.IS_NONE_ADMIN;
    public static Integer flagAdmin = DBConstants.IS_ADMIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book_collection);
        generalDAO = new GeneralDAO(getApplicationContext());
        initView();
        action();
        onUpdateRecyclerView();

    }

    private void action() {
        btnAddBookCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flagAdmin.equals(DBConstants.IS_ADMIN)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ListBookCollectionActivity.this);
                    LayoutInflater inflater = ListBookCollectionActivity.this.getLayoutInflater();
                    View view1 = inflater.inflate(R.layout.dialog_save_book_collection, null);
                    builder.setView(view1);

                    EditText edtCode = (EditText) view1.findViewById(R.id.edt_add_book_collection_code);
                    EditText edtName = (EditText) view1.findViewById(R.id.edt_add_book_collection_name);
                    EditText edtDescription = (EditText) view1.findViewById(R.id.edt_add_book_collection_description);

                    builder.setTitle("Thêm Loại Sách");
                    builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BookCollectionDTO bookCollectionDTO = new BookCollectionDTO();
                            bookCollectionDTO.setCode(edtCode.getText().toString());
                            bookCollectionDTO.setName(edtName.getText().toString());
                            bookCollectionDTO.setDescription(edtDescription.getText().toString());
                            if (ValidateUtils.validateBookCollectionDTO(bookCollectionDTO)) {
                                Long id = generalDAO.saveBookCollectionDTO(bookCollectionDTO);
                                if (id > 0) {
                                    Toast.makeText(getApplicationContext(), "Thêm Loại Sách Thành Công", Toast.LENGTH_SHORT).show();
                                    onUpdateRecyclerView();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Tên Loại Sách Trùng Lặp \n Thêm Thất Bại", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Thông tin chưa hợp lệ", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.create().show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn không có quyền", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        btnAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openDialog();
//            }
//        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagAdmin = DBConstants.IS_NONE_ADMIN;
                finish();
            }
        });
    }

    private void initView() {
        btnAddBookCollection = findViewById(R.id.btn_add_book_collection);
        btnBack = findViewById(R.id.btn_back_add_book_collection);
//        btnAdmin = findViewById(R.id.btn_admin_on);
        rvListCollectionBook = findViewById(R.id.rv_list_book_collection);
    }

    public final void onUpdateRecyclerView() {
        bookCollectionDTOList = generalDAO.findAllBookCollection();
        rvListCollectionBook.setLayoutManager(new LinearLayoutManager(this));
        rvListCollectionBook.setAdapter(new ListBookCollectionAdapter(ListBookCollectionActivity.this, bookCollectionDTOList, generalDAO));
    }

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListBookCollectionActivity.this);
        LayoutInflater inflater = ListBookCollectionActivity.this.getLayoutInflater();
        View view1 = inflater.inflate(R.layout.dialog_validate_pass_word, null);
        builder.setView(view1);

        EditText edtValidate = (EditText) view1.findViewById(R.id.edt_validate_password);

        builder.setTitle("Xác thực");
        builder.setPositiveButton("Xác thực", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (Main2Activity.accountDTO.getPassWord().equals(edtValidate.getText().toString().trim())) {
                        flagAdmin = DBConstants.IS_ADMIN;
                        Toast.makeText(getApplicationContext(), "Đã kích hoạt quyền admin", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        flagAdmin = DBConstants.IS_NONE_ADMIN;
        finish();
    }
}