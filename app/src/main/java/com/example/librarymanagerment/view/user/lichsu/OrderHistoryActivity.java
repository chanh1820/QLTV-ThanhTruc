package com.example.librarymanagerment.view.user.lichsu;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.example.librarymanagerment.core.dto.AccountDTO;
import com.example.librarymanagerment.core.dto.OrderDTO;
import com.example.librarymanagerment.core.dto.ResponseDTO;
import com.example.librarymanagerment.core.sco.OrderSCO;
import com.example.librarymanagerment.core.utils.NotifyUtils;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.example.librarymanagerment.core.utils.TransformerUtils;
import com.example.librarymanagerment.view.admin.dattruoc.DatTruocClickListener;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderHistoryActivity extends AppCompatActivity {

    Button btnBack;
    RecyclerView rvOrder;
    ProgressBar progressBar;
    OrderHistoryAdapter orderHistoryAdapter;

    AccountDTO accountDTO= new AccountDTO();
    List<OrderDTO> orderDTOList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_truoc);

        initView();
        action();
    }

    private void action() {
    }

    private void initView() {
        accountDTO = AccountCache.getCache(getApplicationContext());
        btnBack = findViewById(R.id.btn_dat_truoc_back);
        rvOrder = findViewById(R.id.rv_dat_truoc);
        progressBar = findViewById(R.id.pg_dat_truoc);

        loadListOrder();


    }

    private void loadListOrder() {
        rvOrder.setAdapter(null);
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                Log.e("response",response);
                orderDTOList = ObjectMapperUtils.stringToTypeReference(response,new TypeReference<List<OrderDTO>>(){});

                orderHistoryAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orderDTOList, new DatTruocClickListener() {
                    @Override
                    public void onItemClick(OrderDTO item) {

                    }

                    @Override
                    public void onButtonPickClick(OrderDTO item) {
//                        Intent intent = new Intent(OrderHistoryActivity.this, AddTransactionActivity.class);
//                        intent.putExtra(KeyConstants.INTENT_DAT_TRUOC_ACTIVITY, KeyConstants.INTENT_DAT_TRUOC_ACTIVITY);
//                        intent.putExtra(KeyConstants.INTENT_ORDER_DTO, item);
//                        startActivity(intent);

                    }

                    @Override
                    public void onButtonRejectClick(OrderDTO item) {
//                        handleReject(item);
                    }
                });
                LinearLayoutManager llm = new LinearLayoutManager(OrderHistoryActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rvOrder.setLayoutManager(llm);
                rvOrder.setAdapter(orderHistoryAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                OrderSCO orderSCO = new OrderSCO();
                orderSCO.setStatus(0);
                orderSCO.setUserName(accountDTO.getUserName());
                return TransformerUtils.dtoToPayload(orderSCO, GoogleSheetConstants.ACTION_FIND_ORDER_BY_STATUS);
            }
        };

        RetryPolicy retryPolicy = new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    void handleReject(OrderDTO orderDTO){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);
                ResponseDTO responseDTO = ObjectMapperUtils.stringToDTO(response, ResponseDTO.class);
                if(responseDTO.getStatusCode().equals(GoogleSheetConstants.STATUS_SUCCESS)){
                    loadListOrder();
                }else {
                    NotifyUtils.defaultNotify(getApplicationContext(), "Cập nhật thất bại");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                orderDTO.setStatus(DBConstants.STATUS_ORDER_REJECT);
                return TransformerUtils.dtoToPayload(orderDTO, GoogleSheetConstants.ACTION_UPDATE_STATUS_ORDER_BY_ID);
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

    @Override
    protected void onResume() {
        super.onResume();

        loadListOrder();
    }

    void clearDatTruocAdapter(){
    }
}