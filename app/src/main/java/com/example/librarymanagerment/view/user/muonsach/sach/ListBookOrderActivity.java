package com.example.librarymanagerment.view.user.muonsach.sach;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.dto.OrderPickedDTO;
import com.example.librarymanagerment.core.sco.BookSCO;
import com.example.librarymanagerment.core.utils.NotifyUtils;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.example.librarymanagerment.core.utils.TransformerUtils;
import com.example.librarymanagerment.view.admin.listsach.SPNChooseClassAdapter;
import com.example.librarymanagerment.view.user.muonsach.giohang.ListOrderPickedActivity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListBookOrderActivity extends AppCompatActivity {
    GeneralDAO generalDAO;
    ImageView imvAddImageData, imvCart;
    TextView tvQuantity, tvQuantityGioHang;
    EditText edtInputSearch;
    ProgressBar pbLoading;
    private Button btnBack, btnSearch;
    private RecyclerView rvListBook;

    Spinner spnChooseCollectionParent, spnChooseClass;
    private List<BookDTO> bookDTOList;
    List<String> collectionParentList = new ArrayList<>();
    List<String> classRoomList = new ArrayList<>();

    BookSCO bookSCO = new BookSCO();
//    private List<BookDTO> bookDTOListPicked = new ArrayList<>();

    List<OrderPickedDTO> orderPickedDTOList = new ArrayList<>();
    public static ListBookOrderAdapter listBookOrderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book_order);
        generalDAO = new GeneralDAO(getApplicationContext());
        initView();
        action();

    }

    private void action() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spnChooseCollectionParent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("log i", String.valueOf(i));
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
                }            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imvCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListBookOrderActivity.this, ListOrderPickedActivity.class);
                startActivity(i);
            }
        });
        edtInputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                bookSCO.setSearchName(edtInputSearch.getText().toString().trim());
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSearchBook(bookSCO);
            }
        });

    }

    private void initView() {
        btnBack = findViewById(R.id.btn_book_order_back);
        btnSearch = findViewById(R.id.btn_list_book_order_search);
        tvQuantity = findViewById(R.id.tv_book_order_quantity);
        imvCart = findViewById(R.id.imv_book_order_cart);
        tvQuantityGioHang = findViewById(R.id.tv_quantity_gio_hang);
        edtInputSearch = findViewById(R.id.edt_list_order_picked_input_search);
        pbLoading = findViewById(R.id.pb_list_book_reading_loading);
//        btnAdmin = findViewById(R.id.btn_admin_on);
        rvListBook = findViewById(R.id.rv_list_book_order);
        spnChooseCollectionParent = findViewById(R.id.spn_book_order_choose_collection);
        spnChooseClass = findViewById(R.id.spn_book_order_choose_class_room);

        collectionParentList = generalDAO.getCollectionParentByListBook();
        classRoomList = generalDAO.findClassRoomByListBook();

        spnChooseCollectionParent.setAdapter(new SPNChooseCollectionParentAdapter(getApplicationContext(), collectionParentList));
        spnChooseCollectionParent.setSelection(getIntent().getIntExtra(KeyConstants.INTENT_POSITION, 0));

        spnChooseClass.setAdapter(new SPNChooseClassAdapter(getApplicationContext(), classRoomList));

        updateListPicked();

        bookSCO.setCollectionParent(collectionParentList.get(getIntent().getIntExtra(KeyConstants.INTENT_POSITION,0)));
        handleSearchBook(bookSCO);
    }
    private void handleSearchBook(BookSCO bookSCO) {
        pbLoading.setVisibility(View.VISIBLE);
        bookSCO.setSearchName(edtInputSearch.getText().toString().trim());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("log", response);
                pbLoading.setVisibility(View.GONE);
                bookDTOList = ObjectMapperUtils.stringToTypeReference(response, new TypeReference<List<BookDTO>>() {});
                onUpdateRecyclerView(bookDTOList);
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
                Map<String, String> payload = TransformerUtils.dtoToPayload(bookSCO, GoogleSheetConstants.ACTION_FIND_BOOK);
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

    public final void onUpdateRecyclerView(List<BookDTO> bookDTOList){
        tvQuantity.setText(String.valueOf(bookDTOList.size()));
        listBookOrderAdapter = new ListBookOrderAdapter(ListBookOrderActivity.this, bookDTOList, new OnItemClickListener() {
            @Override
            public void onItemClick( Object data) {
                BookDTO item = (BookDTO) data;
                if (generalDAO.isBookOrderPickedExisting(item.getCode())) {
                    OrderPickedDTO itemExisting = generalDAO.getBookOrderPickedExisting(item.getCode());
                    if(itemExisting.getQuantity() >= item.getQuantity()){
                        NotifyUtils.defaultNotify(getApplicationContext(), "Số lượng sách này tối đa là "+ item.getQuantity());
                        return;
                    }
                    for (int index = 0; index < orderPickedDTOList.size(); index++) {
                        OrderPickedDTO orderPickedDTO = orderPickedDTOList.get(index);
                        Log.e(orderPickedDTO.getBookCode(),item.getCode());
                        if (orderPickedDTO.getBookCode().equals(item.getCode())) {
                            generalDAO.updateQuantityBookOrderPicked(orderPickedDTO.getId(), orderPickedDTO.getQuantity() + 1);
                        }
                    }
                } else {
                    OrderPickedDTO orderPickedDTO = new OrderPickedDTO();
                    orderPickedDTO.setBookDTO(item);
                    orderPickedDTO.setBookData(ObjectMapperUtils.dtoToString(item));
                    orderPickedDTO.setBookName(item.getName());
                    orderPickedDTO.setBookCode(item.getCode());
                    orderPickedDTO.setQuantity(1);
                    orderPickedDTO.setStatus(DBConstants.STATUS_ORDER_PICKER_AVAILABLE);
                    orderPickedDTO.setDeleteFlag(DBConstants.FLAG_NONE_DELETED);
                    generalDAO.saveOrderPickedDTO(orderPickedDTO);
                }
                NotifyUtils.defaultNotify(getApplicationContext(), "Bạn đã thêm "+ item.getName() + " vào giỏ hàng");
                updateListPicked();
            }

            @Override
            public void onItemClickIncreaseButton(OrderPickedDTO item) {

            }

            @Override
            public void onItemClickDecreaseButton(OrderPickedDTO item) {

            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvListBook.setLayoutManager(llm);
        rvListBook.setAdapter(listBookOrderAdapter);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DBConstants.REQUEST_BY_ACTIVITY){
            Uri selectedImage = data.getData();
            imvAddImageData.setImageURI(selectedImage);
        }else if (requestCode == DBConstants.REQUEST_BY_ADAPTER){
            listBookOrderAdapter.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void updateListPicked() {
        orderPickedDTOList = generalDAO.findOrderPickedDTList(DBConstants.STATUS_ORDER_PICKER_AVAILABLE, DBConstants.STATUS_NONE_DELETE);
        Integer total = 0;
        for (OrderPickedDTO item : orderPickedDTOList ) {
            total += item.getQuantity();
        }
        tvQuantityGioHang.setText("Giỏ hàng ("+ total +")");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListPicked();
    }
}

