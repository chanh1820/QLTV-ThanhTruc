package com.example.librarymanagerment.view.admin.listsach;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.constants.GoogleSheetConstants;
import com.example.librarymanagerment.core.constants.KeyConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.BookCollectionDTO;
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.sco.BookSCO;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.example.librarymanagerment.core.utils.StringUtils;
import com.example.librarymanagerment.core.utils.TransformerUtils;
import com.example.librarymanagerment.core.utils.ValidateUtils;
import com.example.librarymanagerment.view.admin.main2.Main2Activity;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListBookActivity extends AppCompatActivity {
    GeneralDAO generalDAO;
    ImageView imvAddImageData;
    TextView tvQuantity, tvNotify;
    EditText edtInputSearch;
    private Button btnAddBook, btnBack, btnSearch;
    private RecyclerView rvListBook;
    ProgressBar pbLoading;
    List<String> collectionParentList = new ArrayList<>();
    List<String> classRoomList = new ArrayList<>();
    Spinner spnChooseCollectionParent, spnChooseClass;
    private List<BookDTO> bookDTOList;
    BookSCO bookSCO = new BookSCO();
//    public static Integer flagAdmin = DBConstants.IS_NONE_ADMIN;
    public static Integer flagAdmin = DBConstants.IS_ADMIN;
    public static ListBookAdapter listBookAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);
        initView();
        action();

    }

    private void action() {
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ListBookActivity.flagAdmin.equals(DBConstants.IS_ADMIN)) {


                BookDTO bookDTO = new BookDTO();
                SPNChooseBookCollectionAdapter spnChooseBookCollectionAdapter;

                AlertDialog.Builder builder = new AlertDialog.Builder(ListBookActivity.this);
                LayoutInflater inflater = ListBookActivity.this.getLayoutInflater();
                View view1 = inflater.inflate(R.layout.dialog_save_book,null);
                builder.setView(view1);

                ImageView imvAddBook = (ImageView) view1.findViewById(R.id.imv_save_book_add_image);
                EditText edtCode = (EditText) view1.findViewById(R.id.edt_save_book_code);
                EditText edtName = (EditText) view1.findViewById(R.id.edt_save_book_name);
                Spinner spnCollectionName = (Spinner) view1.findViewById(R.id.spn_save_book_collection_name);
                EditText edtLocate = (EditText) view1.findViewById(R.id.edt_save_book_locate);
                EditText edtQuantity = (EditText) view1.findViewById(R.id.edt_save_book_quantity);

//                Log.e("Main2Activity.listBookCollectionDTO.size()", Main2Activity.listBookCollectionDTO.size()+"");
                List<BookCollectionDTO> bookCollectionDTOList = Main2Activity.listBookCollectionDTO;
                spnChooseBookCollectionAdapter = new SPNChooseBookCollectionAdapter(getApplicationContext(), bookCollectionDTOList);
                Map<String, Integer> mapSectionSpinner =  new HashMap<>();
                for (int i = 0; i < bookCollectionDTOList.size(); i++) {
                    mapSectionSpinner.put(bookCollectionDTOList.get(i).getCode(), i);
                }

                spnCollectionName.setSelection(0);
                spnCollectionName.setAdapter(spnChooseBookCollectionAdapter);
                spnCollectionName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        bookDTO.setCollectionCode(Main2Activity.listBookCollectionDTO.get(i).getCode());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        bookDTO.setCollectionCode(Main2Activity.listBookCollectionDTO.get(0).getCode());
                    }
                });
                imvAddBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imvAddImageData = imvAddBook;

                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, DBConstants.REQUEST_BY_ACTIVITY);
                    }
                });
                builder.setTitle("Thêm Sách");
                builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bookDTO.setCode(edtCode.getText().toString().trim());
                        bookDTO.setName(edtName.getText().toString().trim());
                        if(NumberUtils.isNumber(edtQuantity.getText().toString().trim())){
                            bookDTO.setQuantity(Integer.parseInt(edtQuantity.getText().toString()));
                        }
                        bookDTO.setLocate(edtLocate.getText().toString().trim());
                        bookDTO.setCollectionCode(bookDTO.getCollectionCode());
                        bookDTO.setImageBase64(StringUtils.BitMapToString(((BitmapDrawable) imvAddImageData.getDrawable()).getBitmap()));
                        if(ValidateUtils.validateBookDTO(bookDTO)){
                            Integer id = generalDAO.saveBookDTO(bookDTO);
                            if (id > 0) {
                                Toast.makeText(getApplicationContext(), "Thêm Sách Thành Công", Toast.LENGTH_SHORT).show();
                                handleSearchBook(bookSCO);
                            } else {
                                Toast.makeText(getApplicationContext(), "Mã Sách Trùng Lặp \n Thêm Thất Bại", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Thông tin không hợp lệ", Toast.LENGTH_SHORT).show();
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagAdmin = DBConstants.IS_NONE_ADMIN;
                finish();
            }
        });

        spnChooseCollectionParent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String collectionParent = (String) view.getTag();
                if(org.apache.commons.lang3.StringUtils.equals(collectionParent, DBConstants.ITEM_ALL)){
                    bookSCO.setCollectionParent("");
                }else {
                    bookSCO.setCollectionParent(collectionParent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnChooseClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String classRoom = (String) view.getTag();
                if(org.apache.commons.lang3.StringUtils.equals(classRoom, DBConstants.ITEM_ALL)){
                    bookSCO.setClassRoom("");
                }else {
                    bookSCO.setClassRoom(classRoom);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSearchBook(bookSCO);
            }
        });
    }

    private void handleSearchBook(BookSCO bookSCO) {
        pbLoading.setVisibility(View.VISIBLE);
        bookSCO.setSearchName(edtInputSearch.getText().toString().trim());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("log", response);
                bookDTOList = ObjectMapperUtils.stringToTypeReference(response, new TypeReference<List<BookDTO>>() {});
                listBookAdapter = new ListBookAdapter(getApplicationContext(), bookDTOList, generalDAO);
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rvListBook.setLayoutManager(llm);
                rvListBook.setAdapter(listBookAdapter);
                pbLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("sync book onErrorResponse",error.getMessage());
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> payload =  TransformerUtils.dtoToPayload(bookSCO, GoogleSheetConstants.ACTION_FIND_BOOK);
                return payload;
            }
        };

        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void initView() {
        generalDAO = new GeneralDAO(getApplicationContext());

        collectionParentList = generalDAO.getCollectionParentByListBook();
        classRoomList =  generalDAO.findClassRoomByListBook();

        btnAddBook = findViewById(R.id.btn_add_book);
        btnBack = findViewById(R.id.btn_back_add_book);
        tvQuantity = findViewById(R.id.tv_list_book_quantity);
        edtInputSearch = findViewById(R.id.edt_list_book_input_search);
//        btnAdmin = findViewById(R.id.btn_admin_on);
        rvListBook = findViewById(R.id.rv_list_book);
        spnChooseCollectionParent = findViewById(R.id.spn_list_book_choose_collection);
        spnChooseClass = findViewById(R.id.spn_list_book_choose_class_room);

        spnChooseCollectionParent.setAdapter(new SPNChooseCollectionParentAdapter(getApplicationContext(), collectionParentList));
        spnChooseCollectionParent.setSelection(getIntent().getIntExtra(KeyConstants.INTENT_POSITION, 0));
        spnChooseClass.setAdapter(new SPNChooseClassAdapter(getApplicationContext(), classRoomList));

        btnSearch = findViewById(R.id.btn_list_book_search);
        pbLoading = findViewById(R.id.pb_list_book_loading);
        tvNotify = findViewById(R.id.tv_list_book_notify);
        bookSCO.setCollectionParent(collectionParentList.get(getIntent().getIntExtra(KeyConstants.INTENT_POSITION,0)));
        handleSearchBook(bookSCO);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        flagAdmin = DBConstants.IS_NONE_ADMIN;
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DBConstants.REQUEST_BY_ACTIVITY){
            Uri selectedImage = data.getData();
            imvAddImageData.setImageURI(selectedImage);
        }else if (requestCode == DBConstants.REQUEST_BY_ADAPTER){
            listBookAdapter.onActivityResult(requestCode, resultCode, data);
        }

    }
}

