package com.example.librarymanagerment.view.user.muonsach.giohang;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.librarymanagerment.core.cache.AccountCache;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.constants.GoogleSheetConstants;
import com.example.librarymanagerment.core.constants.KeyConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.AccountDTO;
import com.example.librarymanagerment.core.dto.BookCollectionParentDTO;
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.dto.OrderDTO;
import com.example.librarymanagerment.core.dto.OrderPickedDTO;
import com.example.librarymanagerment.core.dto.ResponseDTO;
import com.example.librarymanagerment.core.dto.TransactionSheetDTO;
import com.example.librarymanagerment.core.utils.NotifyUtils;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.example.librarymanagerment.core.utils.TransformerUtils;
import com.example.librarymanagerment.view.user.mainuser.MainUserActivity;
import com.example.librarymanagerment.view.user.muonsach.sach.ListBookOrderAdapter;
import com.example.librarymanagerment.view.user.muonsach.sach.OnItemClickListener;
import com.example.librarymanagerment.view.user.muonsach.sach.SPNChooseClassAdapter;
import com.example.librarymanagerment.view.user.muonsach.sach.SPNChooseCollectionParentAdapter;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ListOrderPickedActivity extends AppCompatActivity {
    GeneralDAO generalDAO;
    private Button btnBack, btnCheckOut;
    private RecyclerView rvListBook;

    AccountDTO accountDTO = new AccountDTO();
    String classRoom;

//    private List<BookDTO> bookDTOListPicked = new ArrayList<>();

    List<OrderPickedDTO> orderPickedDTOList = new ArrayList<>();
    public static ListOrderPickedAdapter listOrderPickedAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order_picked);
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
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListOrderPickedActivity.this);
                LayoutInflater inflater = LayoutInflater.from(ListOrderPickedActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_confirm_checkout, null);
                builder.setView(dialogView);
                builder.setTitle("Đặt Sách");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("response",response);
                                ResponseDTO responseDTO = ObjectMapperUtils.stringToTypeReference(response, new TypeReference<ResponseDTO>() {});
                                btnCheckOut.setClickable(true);
                                btnCheckOut.setEnabled(true);
                                if(responseDTO.getStatusCode().equals(GoogleSheetConstants.STATUS_SUCCESS)){
                                    for (OrderPickedDTO item : orderPickedDTOList) {
                                        generalDAO.deleteBookOrderPicked(item.getId());
                                    }
                                    orderPickedDTOList.clear();
                                    Toast.makeText(getApplicationContext(), "Đã gửi", Toast.LENGTH_SHORT).show();
                                    finishAffinity();
                                    Intent intent = new Intent(getApplicationContext(), MainUserActivity.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getApplicationContext(), "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                btnCheckOut.setClickable(true);
                                btnCheckOut.setEnabled(true);
                                Toast.makeText(getApplicationContext(), "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                 List<BookDTO> bookDTOList = new ArrayList<>();
                                for (OrderPickedDTO item : orderPickedDTOList) {
                                    BookDTO bookDTO = item.getBookDTO();
                                    bookDTO.setQuantity(item.getQuantity());
                                    bookDTOList.add(bookDTO);
                                }
                                OrderDTO orderDTO = new OrderDTO();
                                orderDTO.setId(0);
                                orderDTO.setUserName(accountDTO.getUserName());
                                orderDTO.setBooksData(ObjectMapperUtils.dtoToString(bookDTOList));
                                orderDTO.setStudentName(accountDTO.getDisplayName());
                                orderDTO.setClassRoom(accountDTO.getClassRoom());
                                orderDTO.setCreateDate(DBConstants.sdf2.format(new Date().getTime()));
                                orderDTO.setStatus(DBConstants.STATUS_ORDER_WAITING_APPROVE);

                                return TransformerUtils.dtoToPayload(orderDTO, GoogleSheetConstants.ACTION_SAVE_ORDER);
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
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        });
    }

    private void initView() {
        accountDTO = AccountCache.getCache(getApplicationContext());
        btnBack = findViewById(R.id.btn_list_order_picked_back);
        btnCheckOut = findViewById(R.id.btn_list_order_picked_checkout);
        rvListBook = findViewById(R.id.rv_list_book_order_picked);
        onUpdateRecyclerView();
    }

    public final void onUpdateRecyclerView(){
        orderPickedDTOList = generalDAO.findOrderPickedDTList(DBConstants.STATUS_ORDER_PICKER_AVAILABLE, DBConstants.STATUS_NONE_DELETE);
        rvListBook.setLayoutManager(new LinearLayoutManager(this));
        listOrderPickedAdapter = new ListOrderPickedAdapter(ListOrderPickedActivity.this, orderPickedDTOList, new OnItemClickListener() {
            @Override
            public void onItemClick(Object item) {

            }

            @Override
            public void onItemClickIncreaseButton(OrderPickedDTO item) {
                BookDTO bookDTO = generalDAO.findBookByCode(item.getBookCode());
                if(item.getQuantity() >= bookDTO.getQuantity()){
                    NotifyUtils.defaultNotify(getApplicationContext(), "Số lượng sách này tối đa là "+ bookDTO.getQuantity());
                    return;
                }
                generalDAO.updateQuantityBookOrderPicked(item.getId(), item.getQuantity() + 1);
                onUpdateRecyclerView();
            }

            @Override
            public void onItemClickDecreaseButton(OrderPickedDTO item) {
                if (item.getQuantity() != 0){
                    generalDAO.updateQuantityBookOrderPicked(item.getId(), item.getQuantity() - 1);
                    onUpdateRecyclerView();
                }else {
                    NotifyUtils.defaultNotify(getApplicationContext(), "Giá trị không thể bằng 0");
                }

            }
        });
        rvListBook.setAdapter(listOrderPickedAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

