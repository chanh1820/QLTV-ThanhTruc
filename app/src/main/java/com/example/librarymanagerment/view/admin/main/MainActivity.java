package com.example.librarymanagerment.view.admin.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.Toast;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.DBHelper;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.BookCollectionDTO;
import com.example.librarymanagerment.core.dto.TransactionDTO;
import com.example.librarymanagerment.core.sco.TransactionSCO;
import com.example.librarymanagerment.view.admin.listdanhmucsach.ListBookCollectionActivity;
import com.example.librarymanagerment.view.admin.phienmuonsach.AddTransactionActivity;
import com.example.librarymanagerment.view.admin.listsach.ListBookActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnThemPhieuMuon, btnSearchTransaction;
    EditText edtMinDate, edtMaxDate;
    TextClock textClock;

    ImageView imvMinDate, imvMaxDate, imvSettings;
    RecyclerView rvListTransaction;
    GeneralDAO generalDAO;
    TransactionSCO transactionSCO = new TransactionSCO();
    public static List<BookCollectionDTO> listBookCollectionDTO = new ArrayList<>();
    public static Map<String, String> mapListBookCollectionDTO = new HashMap<String, String>();
    List<TransactionDTO> transactionDTOList = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendarMin = Calendar.getInstance();
    Calendar calendarMax = Calendar.getInstance();

    //    String timerInit = sdf.format(Calendar.getInstance().getTime().);
    ListTransactionAdapter listTransactionAdapter;
    ChooseSettingsAdapter chooseSettingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebase();

        calendarMin.add(Calendar.DATE,-10);
        transactionSCO.setMinDate(sdf.format(calendarMin.getTime()));
        transactionSCO.setMaxDate(sdf.format(calendarMax.getTime()));


        initView();
        actionView();

        Log.e("transactionDTOList", transactionDTOList.size() + "");

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvListTransaction.setLayoutManager(llm);
        rvListTransaction.setAdapter(listTransactionAdapter);

    }

    private void actionView() {
        btnThemPhieuMuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddTransactionActivity.class);
                startActivity(i);
            }
        });
        imvMinDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                transactionDTOList = generalDAO.searchTransaction(transactionSCO);
                Log.e("transactionDTOList.size()", transactionDTOList.size() + "");
                listTransactionAdapter = new ListTransactionAdapter(MainActivity.this, transactionDTOList, generalDAO);

                rvListTransaction.setAdapter(listTransactionAdapter);
            }
        });

        imvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_setting);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setAttributes(lp);

                dialog.show();

                chooseSettingsAdapter = new ChooseSettingsAdapter(DBConstants.listSettingDTO, getApplicationContext());
                GridView gvListSession = dialog.findViewById(R.id.gv_list_session);
                gvListSession.setAdapter(chooseSettingsAdapter);
                gvListSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        if (position == 0) {
                            intent = new Intent(MainActivity.this, ListBookActivity.class);
                        } else if (position == 1) {
                            intent = new Intent(MainActivity.this, ListBookCollectionActivity.class);
                        }
                        startActivity(intent);
                        dialog.dismiss();

                    }
                });
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

        btnThemPhieuMuon = findViewById(R.id.btn_them_phieu_muon);
        edtMinDate = findViewById(R.id.edt_search_transaction_min_date);
        edtMaxDate = findViewById(R.id.edt_search_transaction_max_date);
        imvMinDate = findViewById(R.id.imv_search_transaction_calendar_min_date);
        imvMaxDate = findViewById(R.id.imv_search_transaction_calendar_max_date);
        imvSettings = findViewById(R.id.imv_settings);
        btnSearchTransaction = findViewById(R.id.btn_search_transaction);
        rvListTransaction = findViewById(R.id.rv_list_transaction);
        textClock = findViewById(R.id.textclock);
        String formatdate = "E, d-M-yyyy k:m:sa";
        textClock.setFormat12Hour(formatdate);
        textClock.setFormat24Hour(formatdate);


        edtMinDate.setText(sdf.format(calendarMin.getTime()));
        edtMaxDate.setText(sdf.format(Calendar.getInstance().getTime()));
        generalDAO = new GeneralDAO(getApplicationContext());
        transactionDTOList = generalDAO.searchTransaction(transactionSCO);
        listTransactionAdapter = new ListTransactionAdapter(MainActivity.this, transactionDTOList, generalDAO);
        onDataChange();

    }

    public void onDataChange() {
        mapListBookCollectionDTO.clear();
        listBookCollectionDTO = generalDAO.findAllBookCollection();
        transactionDTOList = generalDAO.searchTransaction(transactionSCO);
        listTransactionAdapter.notifyDataSetChanged();
        for (BookCollectionDTO item : listBookCollectionDTO) {
            mapListBookCollectionDTO.put(item.getCode(), item.getName());
        }
        mapListBookCollectionDTO.put("", "");
        mapListBookCollectionDTO.put(null, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("log", "onResume()");
        onDataChange();
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

    private void firebase() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(DBConstants.USER_NAME+"@gmail.com", "123456")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                        }else {
                            finish();
                        }
                    }
                });
    }
}