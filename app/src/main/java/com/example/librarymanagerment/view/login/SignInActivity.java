package com.example.librarymanagerment.view.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.librarymanagerment.core.cache.AccountCache;
import com.example.librarymanagerment.core.constants.GoogleSheetConstants;
import com.example.librarymanagerment.core.dto.AccountDTO;
import com.example.librarymanagerment.core.dto.LoginDTO;
import com.example.librarymanagerment.core.dto.ResponseDTO;
import com.example.librarymanagerment.core.utils.NotifyUtils;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.example.librarymanagerment.core.utils.TransformerUtils;
import com.example.librarymanagerment.core.utils.ValidateUtils;
import com.example.librarymanagerment.view.admin.main2.Main2Activity;
import com.example.librarymanagerment.view.user.mainuser.MainUserActivity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    EditText edtUserName, edtPassWord ;
    Button btnSignIn;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
        action();
    }

    private void action() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSignIn.setFocusable(false);
                progressBar.setVisibility(View.VISIBLE);
                LoginDTO loginDTO = new LoginDTO(
                        edtUserName.getText().toString().trim(),
                        edtPassWord.getText().toString().trim()
                );
                if (ValidateUtils.isValidLoginDTO(loginDTO, getApplicationContext())) {
                    login(loginDTO);
                }
            }
        });
    }

    private void login(LoginDTO loginDTO) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                progressBar.setVisibility(View.INVISIBLE);
                ResponseDTO<AccountDTO> responseDTO
                        = ObjectMapperUtils.stringToTypeReference(
                        response,
                        new TypeReference<ResponseDTO<AccountDTO>>() {
                        }
                );
                if (responseDTO.getStatusCode().equals(GoogleSheetConstants.STATUS_SUCCESS)) {
                    AccountDTO accountDTO = responseDTO.getData();
                    AccountCache.setCache(SignInActivity.this, accountDTO);
                    Intent intent = new Intent();
                    switch (accountDTO.getRole()) {
                        case GoogleSheetConstants.ACCOUNT_TYPE_USER:
                            intent = new Intent(SignInActivity.this, MainUserActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case GoogleSheetConstants.ACCOUNT_TYPE_ADMIN:
                            intent = new Intent(SignInActivity.this, Main2Activity.class);
                            finishAffinity();
                            startActivity(intent);
                            finish();
                            break;
                        default:
                            NotifyUtils.defaultNotify(getApplicationContext(), "Tài khoản không hợp lệ");
                            break;
                    }
                } else {
                    NotifyUtils.notifyByMessage(getApplicationContext(), responseDTO.getStatusCode());
                }
                btnSignIn.setFocusable(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse",error.getMessage());
                btnSignIn.setFocusable(true);
                Toast.makeText(getApplicationContext(), "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return TransformerUtils.dtoToPayload(loginDTO, GoogleSheetConstants.ACTION_LOGIN);
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
        edtPassWord = findViewById(R.id.edt_sign_in_password);
        edtUserName = findViewById(R.id.edt_sign_in_user_name);
        btnSignIn = findViewById(R.id.btn_sign_in_do_login);
        progressBar = findViewById(R.id.pg_main);
    }
}