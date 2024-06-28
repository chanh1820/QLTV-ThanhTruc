package com.example.librarymanagerment.view.admin.danhsachphieumuon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.librarymanagerment.core.DBHelper;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.constants.GoogleSheetConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.ClassRoomDTO;
import com.example.librarymanagerment.core.dto.TransactionDTO;
import com.example.librarymanagerment.core.dto.TransactionStatusDTO;
import com.example.librarymanagerment.core.sco.TransactionSCO;
import com.example.librarymanagerment.core.sco.TransactionSheetSCO;
import com.example.librarymanagerment.core.utils.ExcelUtils;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.example.librarymanagerment.core.utils.TransformerUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ListTransactionActivity extends AppCompatActivity {

    Button btnSearchTransaction, btnBack, btnExportExcel;
    EditText edtMinDate, edtMaxDate, edtName;
    Spinner spnChooseTransactionStatus, spnChooseClassRoom;

    ImageView imvMinDate, imvMaxDate;
    RecyclerView rvListTransaction;
    GeneralDAO generalDAO;
    TransactionSCO transactionSCO = new TransactionSCO();
    List<TransactionDTO> transactionDTOList = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Calendar calendarMin = Calendar.getInstance();
    Calendar calendarMax = Calendar.getInstance();

    ListTransactionAdapter listTransactionAdapter;
    SPNChooseTransactionStatusAdapter spnChooseTransactionStatusAdapter;
    SPNChooseClassroomAdapter spnChooseClassroomAdapter;
    TransactionSheetSCO transactionSheetSCO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transaction);

        calendarMin.add(Calendar.DATE,-10);
        transactionSCO.setMinDate(sdf.format(calendarMin.getTime()));
        transactionSCO.setMaxDate(sdf.format(calendarMax.getTime()));
        transactionSCO.setStatus(0);
        transactionSCO.setClassRoom("");
        transactionSCO.setName("");

        initView();
        actionView();

        Log.e("transactionDTOList", transactionDTOList.size() + "");


    }

    private void actionView() {
        imvMinDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                DatePickerDialog datePickerDialog = new DatePickerDialog(ListTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        if(validateTimelineDate(sdf.format(calendar.getTime()), transactionSCO.getMaxDate())){
                            edtMinDate.setText(sdf.format(calendar.getTime()));
                        }else {
                            Toast.makeText(getApplicationContext(), "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        imvMaxDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                DatePickerDialog datePickerDialog = new DatePickerDialog(ListTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        if(validateTimelineDate(transactionSCO.getMinDate(), sdf.format(calendar.getTime()))){
                            edtMaxDate.setText(sdf.format(calendar.getTime()));
                        }else {
                            Toast.makeText(getApplicationContext(), "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        btnSearchTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionSCO.setMinDate(edtMinDate.getText().toString().trim());
                transactionSCO.setMaxDate(edtMaxDate.getText().toString().trim());
                Log.e("transactionDTOList.size()", transactionDTOList.size() + "");
                handleSearchTransaction();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        spnChooseTransactionStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TransactionStatusDTO item = (TransactionStatusDTO) view.getTag();
                transactionSCO.setStatus(item.getStatusCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnChooseClassRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ClassRoomDTO item = (ClassRoomDTO) view.getTag();
                if(item.getClassRoom().equals(DBConstants.ITEM_ALL)){
                    transactionSCO.setClassRoom("");
                }else {
                    transactionSCO.setClassRoom(item.getClassRoom());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnExportExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivity(intent);
                    }
                }
                ActivityCompat.requestPermissions(ListTransactionActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
                String absolutePath = ExcelUtils.exportExcel(transactionDTOList,
                        android.text.format.DateFormat.format("Danh sách phiếu mượn" +" (ss-mm-hh-ddMMyyyy)", new java.util.Date()).toString(),
                        "Danh sách phiếu mượn từ "+ transactionSCO.getMinDate()+" đến "+transactionSCO.getMaxDate());
                Toast.makeText(getApplicationContext(), "Đã lưu" + absolutePath, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
        DBHelper db = new DBHelper(this);
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        generalDAO = new GeneralDAO(getApplicationContext());
        edtMinDate = findViewById(R.id.edt_search_transaction_min_date);
        edtMaxDate = findViewById(R.id.edt_search_transaction_max_date);
        imvMinDate = findViewById(R.id.imv_search_transaction_calendar_min_date);
        imvMaxDate = findViewById(R.id.imv_search_transaction_calendar_max_date);
        edtName = findViewById(R.id.edt_search_transaction_name);
        btnSearchTransaction = findViewById(R.id.btn_search_transaction);
        spnChooseTransactionStatus = findViewById(R.id.spn_search_transaction_status);
        spnChooseClassRoom = findViewById(R.id.spn_search_transaction_class_room);
        rvListTransaction = findViewById(R.id.rv_list_transaction);
        btnBack = findViewById(R.id.btn_back_list_transaction);
        btnExportExcel = findViewById(R.id.btn_search_transaction_export_excel);

        spnChooseTransactionStatusAdapter = new SPNChooseTransactionStatusAdapter(getApplicationContext(), DBConstants.TRANSACTION_STATUS_DTO_LIST);
        spnChooseTransactionStatus.setAdapter(spnChooseTransactionStatusAdapter);
        List<ClassRoomDTO> classRoomDTOList = new ArrayList<>();
        classRoomDTOList.addAll(generalDAO.getClassRoomByStudentList(true));
        spnChooseClassroomAdapter = new SPNChooseClassroomAdapter(getApplicationContext(), classRoomDTOList);
        spnChooseClassRoom.setAdapter(spnChooseClassroomAdapter);

        edtMinDate.setText(sdf.format(calendarMin.getTime()));
        edtMaxDate.setText(sdf.format(Calendar.getInstance().getTime()));
        transactionSCO.setStatus(0);




        handleSearchTransaction();


    }

    private void handleSearchTransaction() {
        btnSearchTransaction.setClickable(false);
        btnSearchTransaction.setEnabled(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);
                transactionDTOList = ObjectMapperUtils.stringToTypeReference(response,new TypeReference<List<TransactionDTO>>(){});
                btnSearchTransaction.setClickable(true);
                btnSearchTransaction.setEnabled(true);

//                transactionDTOList = generalDAO.searchTransaction(transactionSCO);
                listTransactionAdapter = new ListTransactionAdapter(ListTransactionActivity.this, transactionDTOList, generalDAO);
                LinearLayoutManager llm = new LinearLayoutManager(ListTransactionActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rvListTransaction.setLayoutManager(llm);
                rvListTransaction.setAdapter(listTransactionAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnSearchTransaction.setClickable(true);
                btnSearchTransaction.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                transactionSCO.setName(edtName.getText().toString().trim());
                transactionSheetSCO
                        = TransformerUtils.convertTransactionSCOtoTransactionSheetSCO(transactionSCO);
                Map<String, String> params = ObjectMapperUtils.dtoToMap(transactionSheetSCO, new TypeReference<Map<String, String>>(){});
                params.put("action", GoogleSheetConstants.ACTION_FIND_TRANSACTION_BY_FROM_AND_TO);
                return params;
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

    private boolean validateTimelineDate(String min, String max){
        boolean isValid = true;
        Date minDate = null, maxDate = null;
        try {
            minDate = new SimpleDateFormat("yyyy-MM-dd").parse(min);
            maxDate = new SimpleDateFormat("yyyy-MM-dd").parse(max);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("minDate.compareTo(maxDate)",minDate.compareTo(maxDate)+"");
        if(minDate.compareTo(maxDate)>0){
            isValid = false;
        }
        return isValid;
    }

}