package com.example.librarymanagerment.view.admin.danhsachtaikhoan.registry;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.AccountDTO;
import com.example.librarymanagerment.core.dto.ClassRoomDTO;
import com.example.librarymanagerment.core.dto.ResponseDTO;
import com.example.librarymanagerment.core.dto.RoleDTO;
import com.example.librarymanagerment.core.utils.NotifyUtils;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.example.librarymanagerment.core.utils.TransformerUtils;
import com.example.librarymanagerment.core.utils.ValidateUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class RegistryAccountActivity extends AppCompatActivity {
    EditText edtDisplayName, edtUserName, edtPassWord1, edtPassWord2;
    Button btnRegistry, btnBack;
    Spinner spnChooseClassRoom, spnChooseRole;
    AccountDTO accountDTO = new AccountDTO();
    GeneralDAO generalDAO;
    ChooseClassRoomAdapter chooseClassRoomAdapter;
    ChooseRoleAdapter chooseRoleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry_account);

        initView();
        action();
        spnChooseRole.setSelection(0,false);
        spnChooseClassRoom.setSelection(0,false);

    }

    private void action() {
        btnRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String displayName = edtDisplayName.getText().toString().trim();
                String userName = edtUserName.getText().toString().trim();
                String passWord1 = edtPassWord1.getText().toString().trim();
                String passWord2 = edtPassWord2.getText().toString().trim();
                if (displayName.isEmpty() ||userName.isEmpty()||passWord1.isEmpty()||passWord2.isEmpty()){
                    NotifyUtils.defaultNotify(getApplicationContext(), "Thông tin chưa chính xác");
                    return;
                }
                if (!passWord1.equals(passWord2)){
                    NotifyUtils.defaultNotify(getApplicationContext(), "Mật khẩu xác nhận không khớp");
                    return;
                }
                ValidateUtils validateUtils = new ValidateUtils();
                if (!validateUtils.validatePassword(passWord1)){
                    NotifyUtils.defaultNotify(getApplicationContext(), "Mật khẩu yêu cầu kí tự từ a-z và số từ 0-9");
                    return;
                }

                if (!validateUtils.validateUserName(userName)){
                    NotifyUtils.defaultNotify(getApplicationContext(), "Tên đăng nhập không hợp lệ");
                    return;
                }
                accountDTO.setUserName(userName);
                accountDTO.setPassWord(passWord1);
                accountDTO.setDisplayName(displayName);
                registryAccount(accountDTO);

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        spnChooseRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RoleDTO roleDTO = (RoleDTO) view.getTag();
                accountDTO.setRole(roleDTO.getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnChooseClassRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                accountDTO.setClassRoom(((ClassRoomDTO) view.getTag()).getClassRoom());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void registryAccount(AccountDTO accountDTO) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                ResponseDTO responseDTO
                        = ObjectMapperUtils.stringToTypeReference(
                        response,
                        new TypeReference<ResponseDTO>() {
                        }
                );
                if(responseDTO.getStatusCode().toString().equals(GoogleSheetConstants.STATUS_SUCCESS)){
                    NotifyUtils.defaultNotify(getApplicationContext(), "Đăng kí thành công");
                    finish();
                }else {
                    NotifyUtils.defaultNotify(getApplicationContext(), responseDTO.getMessage());
                }
                btnRegistry.setFocusable(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnRegistry.setFocusable(true);
                Toast.makeText(getApplicationContext(), "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                accountDTO.setFlagLogin("");
                return TransformerUtils.dtoToPayload(accountDTO, GoogleSheetConstants.ACTION_SAVE_ACCOUNT);
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
        edtDisplayName = findViewById(R.id.edt_registry_display_name);
        edtUserName = findViewById(R.id.edt_registry_username);
        edtPassWord1 = findViewById(R.id.edt_registry_pass_word_1);
        edtPassWord2 = findViewById(R.id.edt_registry_pass_word_2);
        btnRegistry = findViewById(R.id.btn_registry_accept);
        btnBack = findViewById(R.id.btn_back_registry_account);
        spnChooseClassRoom = findViewById(R.id.spn_choose_class_room);
        spnChooseRole = findViewById(R.id.spn_choose_role);

        chooseClassRoomAdapter = new ChooseClassRoomAdapter(getApplicationContext(), DBConstants.CLASS_ROOM_lIST);
        spnChooseClassRoom.setAdapter(chooseClassRoomAdapter);

        chooseRoleAdapter = new ChooseRoleAdapter(getApplicationContext(), DBConstants.ROLE_lIST);
        spnChooseRole.setAdapter(chooseRoleAdapter);

    }

}