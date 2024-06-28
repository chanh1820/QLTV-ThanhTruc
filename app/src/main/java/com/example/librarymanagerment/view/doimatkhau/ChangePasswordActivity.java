package com.example.librarymanagerment.view.doimatkhau;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.librarymanagerment.core.cache.AccountCache;
import com.example.librarymanagerment.core.constants.GoogleSheetConstants;
import com.example.librarymanagerment.core.dto.AccountDTO;
import com.example.librarymanagerment.core.dto.ChangePasswordDTO;
import com.example.librarymanagerment.core.dto.ResponseDTO;
import com.example.librarymanagerment.core.utils.NotifyUtils;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.example.librarymanagerment.core.utils.TransformerUtils;
import com.example.librarymanagerment.view.login.SignInActivity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText edtPasswordOld, edtPasswordNew, edtPasswordReNew;
    Button btnAccept, btnBack;
    AccountDTO accountDTO = new AccountDTO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        accountDTO = AccountCache.getCache(getApplicationContext());
        initView();
        action();
    }

    private void action() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordOld = edtPasswordOld.getText().toString().trim();
                String passwordNew = edtPasswordNew.getText().toString().trim();
                String passwordReNew = edtPasswordReNew.getText().toString().trim();
                if(accountDTO.getPassWord().equals(passwordOld)){
                    if(passwordNew.equals(passwordReNew)){
                        btnAccept.setEnabled(true);
                        btnAccept.setClickable(true);

                        changePassword(new ChangePasswordDTO(accountDTO.getUserName(), passwordNew));
                    }else {
                        Toast.makeText(getApplicationContext(), "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Mật khẩu cũ không khớp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        btnAccept = findViewById(R.id.btn_change_password_accept);
        btnBack = findViewById(R.id.btn_back_change_password);
        edtPasswordOld = findViewById(R.id.edt_change_password_pass_word_old);
        edtPasswordNew = findViewById(R.id.edt_change_password_pass_word_new);
        edtPasswordReNew = findViewById(R.id.edt_change_password_pass_word_re_new);
    }

    private void changePassword(ChangePasswordDTO changePasswordDTO) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                ResponseDTO<String> responseDTO
                        = ObjectMapperUtils.stringToTypeReference(
                        response,
                        new TypeReference<ResponseDTO<String>>() {
                        }
                );
                if (responseDTO.getStatusCode().equals(GoogleSheetConstants.STATUS_SUCCESS)) {
                    NotifyUtils.defaultNotify(getApplicationContext(), responseDTO.getMessage());

                    finish();
                    Intent i =  new Intent(ChangePasswordActivity.this, SignInActivity.class);
                    startActivity(i);

                } else {
                    NotifyUtils.defaultNotify(getApplicationContext(), responseDTO.getMessage());
                }
                btnAccept.setFocusable(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnAccept.setFocusable(true);
                Toast.makeText(getApplicationContext(), "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return TransformerUtils.dtoToPayload(changePasswordDTO, GoogleSheetConstants.ACTION_CHANGE_PASSWORD);
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