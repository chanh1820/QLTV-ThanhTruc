package com.example.librarymanagerment.view.admin.danhsachtaikhoan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.example.librarymanagerment.core.constants.GoogleSheetConstants;
import com.example.librarymanagerment.core.dto.AccountDTO;
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.dto.ResponseDTO;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.example.librarymanagerment.core.utils.TransformerUtils;
import com.example.librarymanagerment.view.admin.danhsachtaikhoan.registry.RegistryAccountActivity;
import com.example.librarymanagerment.view.admin.listsach.ListBookAdapter;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAccountActivity extends AppCompatActivity {
    Button btnBack, btnAddAccount;
    RecyclerView rvListAccount;
    ProgressBar pbLoading;
    ListAccountAdapter listAccountAdapter;

    List<AccountDTO> accountDTOList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_account);
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
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegistryAccountActivity.class);
                startActivity(i);
            }
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.btn_list_account_back);
        btnAddAccount = findViewById(R.id.btn_list_account_add_account);
        rvListAccount = findViewById(R.id.rv_list_account);
        pbLoading = findViewById(R.id.pb_list_account_loading);
        handleLoadAccount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleLoadAccount();
    }

    private void handleLoadAccount(){
        pbLoading.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("log", response);
                accountDTOList = ObjectMapperUtils.stringToTypeReference(response, new TypeReference<List<AccountDTO>>() {});
                Collections.reverse(accountDTOList);
                listAccountAdapter = new ListAccountAdapter(getApplicationContext(), accountDTOList, new OnItemClickListener() {
                    @Override
                    public void onItemDeleteClick(AccountDTO item) {
                        deleteAccount(item);
                    }
                });
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rvListAccount.setLayoutManager(llm);
                rvListAccount.setAdapter(listAccountAdapter);
                pbLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> payload =  new HashMap<>();
                payload.put("action", GoogleSheetConstants.ACTION_FIND_ACCOUNT);
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

    private void deleteAccount(AccountDTO accountDTO) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("log", response);
                ResponseDTO responseDTO = ObjectMapperUtils.stringToDTO(response, ResponseDTO.class);
                if(responseDTO.getStatusCode().equals(GoogleSheetConstants.STATUS_SUCCESS)){
                    handleLoadAccount();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                accountDTO.setFlagLogin("");
                Map<String, String> payload =  TransformerUtils.dtoToPayload(accountDTO, GoogleSheetConstants.ACTION_DELETE_ACCOUNT);
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
}