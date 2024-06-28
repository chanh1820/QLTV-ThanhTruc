package com.example.librarymanagerment.view.admin.phienmuonsach;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.constants.GoogleSheetConstants;
import com.example.librarymanagerment.core.constants.KeyConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.dto.ClassRoomDTO;
import com.example.librarymanagerment.core.dto.OrderDTO;
import com.example.librarymanagerment.core.dto.ResponseDTO;
import com.example.librarymanagerment.core.dto.StudentDTO;
import com.example.librarymanagerment.core.dto.TransactionDTO;
import com.example.librarymanagerment.core.dto.TransactionSheetDTO;
import com.example.librarymanagerment.core.utils.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddTransactionActivity extends AppCompatActivity {
    EditText edtSearchBook, edtToDate, edtName, edtClassRoom;
    ListView lvSuggestBook;
    TextView tvBookTotal;
    ImageView imvUpDownSuggest, imvCalendar;
    Button btnAddTransaction;
    ProgressBar pbLoading;
    LinearLayout lnAddTransactionParent, lnAddTransactionSuggest;
    RecyclerView rvPickedBook;
    Spinner spnClassRoom, spnStudent;

    static SuggestBookAdapter suggestBookAdapter;
    static PickedBookAdapter pickedBookAdapter;
    SPNChooseClassAdapter spnChooseClassAdapter;
    SPNChooseStudentAdapter spnChooseStudentAdapter;


    GeneralDAO generalDAO;
    public static List<BookDTO> listBookSuggest = new ArrayList<>();
    public static List<BookDTO> listBookPicked = new ArrayList<>();
    public static List<ClassRoomDTO> classRoomDTOList = new ArrayList<>();
    public static List<StudentDTO> studentDTOList = new ArrayList<>();

    public static List<BookDTO> listBookSearch= new ArrayList<>();
    Boolean isDatTruoc = false;
    OrderDTO orderDTO = new OrderDTO();
    public TransactionDTO transactionDTO = new TransactionDTO();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

    StudentDTO studentDTO = new StudentDTO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        transactionDTO.setToDate("");
        initView();
        action();
    }

    public void action() {
        btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionDTO.setCreateDate(sdf2.format(new Date().getTime()));
                transactionDTO.setFromDate(sdf.format(new Date().getTime()));
                transactionDTO.setListBookDTO(listBookPicked);
                transactionDTO.setStatus(DBConstants.TRANSACTION_STATUS_NOT_REFUNDED);
                transactionDTO.setStaffName("admin");
                try {
                    transactionDTO.setJsonListBook(new ObjectMapper().writeValueAsString(listBookPicked));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (ValidateUtils.validateTransactionDTO(transactionDTO, generalDAO, getApplicationContext())) {
                    btnAddTransaction.setClickable(false);
                    btnAddTransaction.setEnabled(false);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("response",response);
                                ResponseDTO responseDTO = ObjectMapperUtils.stringToTypeReference(response, new TypeReference<ResponseDTO>() {});
                                btnAddTransaction.setClickable(true);
                                btnAddTransaction.setEnabled(true);

                                if(responseDTO.getStatusCode().equals(GoogleSheetConstants.STATUS_SUCCESS)){
//                                    studentDTO.setBorrowedFlag(DBConstants.STATUS_BORROWED);
//                                    studentDTO.setStudentName(transactionDTO.getStudentName());
//                                    studentDTO.setClassRoom(transactionDTO.getClassRoom());
//                                    studentDTO.setId(transactionDTO.getStudentId());
//
//                                    generalDAO.updateStudentDTO(studentDTO);
//                                    generalDAO.updateQuantityBook(transactionDTO.getListBookDTO(), DBConstants.OPERATOR_REDUCE);
                                    if(isDatTruoc){
                                        handleOrder(orderDTO);
                                    }
                                    Toast.makeText(getApplicationContext(), "Đã lưu", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    NotifyUtils.defaultNotify(getApplicationContext(), responseDTO.getMessage());
                                }
                                listBookSuggest = new ArrayList<>();
                                listBookPicked = new ArrayList<>();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                btnAddTransaction.setClickable(true);
                                btnAddTransaction.setEnabled(true);
                                Toast.makeText(getApplicationContext(), "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                TransactionSheetDTO transactionSheetDTO
                                        = TransformerUtils.convertTransactionDTOtoTransactionSheetDTO(transactionDTO);
                                transactionSheetDTO.setId(0);
                                Map<String, String> params = ObjectMapperUtils.dtoToMap(transactionSheetDTO, new TypeReference<Map<String, String>>(){});
                                params.put("action", GoogleSheetConstants.ACTION_SAVE_TRANSACTION);
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
                    } else {
                    btnAddTransaction.setClickable(true);
                    btnAddTransaction.setEnabled(true);
                    }
            }
        });
        edtSearchBook.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                listBookSuggest = listBookSearch.stream()
                                    .filter(book -> book.getName().contains(edtSearchBook.getText().toString().trim()))
                                    .collect(Collectors.toList());
                suggestBookAdapter = new SuggestBookAdapter(getApplicationContext(), listBookSuggest);
                lvSuggestBook.setAdapter(suggestBookAdapter);
                setStatusSuggestView(View.VISIBLE);
                Log.e("lisBookPicked.size()", listBookPicked.size() + "");
                Log.e("listBookSuggest.size()", listBookSuggest.size() + "");

            }
        });
        lnAddTransactionParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStatusSuggestView(View.GONE);
            }
        });
        edtSearchBook.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setStatusSuggestView(View.VISIBLE);
            }
        });
        imvUpDownSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lvSuggestBook.getVisibility() == View.GONE) {
                    setStatusSuggestView(View.VISIBLE);
                } else if ((lvSuggestBook.getVisibility() == View.VISIBLE)) {
                    setStatusSuggestView(View.GONE);
                }
            }
        });

        imvCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        edtToDate.setText(sdf.format(calendar.getTime()));
                        transactionDTO.setToDate(sdf.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        spnStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("log","update");
                StudentDTO item = (StudentDTO) view.getTag();
                Log.e("log2 ",ObjectMapperUtils.dtoToString(item));

                transactionDTO.setClassRoom(item.getClassRoom());
                transactionDTO.setStudentName(item.getStudentName());
                transactionDTO.setStudentId(item.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnClassRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("log","update 2");

                ClassRoomDTO item = (ClassRoomDTO) view.getTag();
                updateSPNStudent(item.getClassRoom());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void validateTransactionBook() {

    }

    private void initView() {
        generalDAO = new GeneralDAO(getApplicationContext());
        edtSearchBook = findViewById(R.id.edt_add_transaction_search_book);
        pbLoading = findViewById(R.id.pb_add_transaction_loading);
        edtToDate = findViewById(R.id.edt_add_transaction_to_date);
        spnClassRoom = findViewById(R.id.spn_add_transaction_class_room);
        spnStudent = findViewById(R.id.spn_add_transaction_student_name);
        btnAddTransaction = findViewById(R.id.btn_add_transaction_add);
        imvUpDownSuggest = findViewById(R.id.imv_add_transaction_up_down_suggest);
        imvCalendar = findViewById(R.id.img_add_transaction_calendar);
        lvSuggestBook = findViewById(R.id.lv_add_transaction_suggest_book);
        tvBookTotal = findViewById(R.id.tv_add_transaction_book_total);
        rvPickedBook = findViewById(R.id.rv_add_transaction_picked_book);
        lnAddTransactionParent = findViewById(R.id.ln_add_transaction_parent);
        getAllListBook();
        isDatTruoc = KeyConstants.INTENT_DAT_TRUOC_ACTIVITY.equals(getIntent().getStringExtra(KeyConstants.INTENT_DAT_TRUOC_ACTIVITY));
        if(isDatTruoc){
            orderDTO = (OrderDTO) getIntent().getSerializableExtra(KeyConstants.INTENT_ORDER_DTO);
            listBookPicked = ObjectMapperUtils.stringToTypeReference(orderDTO.getBooksData(), new TypeReference<List<BookDTO>>() {});
            suggestBookAdapter = new SuggestBookAdapter(AddTransactionActivity.this, listBookSuggest);
            pickedBookAdapter = new PickedBookAdapter(AddTransactionActivity.this, listBookPicked);
            pickedBookAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    calculatorBook();
                }
            });
            classRoomDTOList = new ArrayList<>();
            classRoomDTOList.add(new ClassRoomDTO(0, orderDTO.getClassRoom()));
            spnChooseClassAdapter = new SPNChooseClassAdapter(AddTransactionActivity.this, classRoomDTOList);
            spnClassRoom.setAdapter(spnChooseClassAdapter);

//        updateSPNStudent(classRoomDTOList.get(0).getClassRoom());


            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rvPickedBook.setLayoutManager(llm);

            lvSuggestBook.setAdapter(suggestBookAdapter);
            rvPickedBook.setAdapter(pickedBookAdapter);
        }else {
            suggestBookAdapter = new SuggestBookAdapter(AddTransactionActivity.this, listBookSuggest);
            pickedBookAdapter = new PickedBookAdapter(AddTransactionActivity.this, listBookPicked);
            pickedBookAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    calculatorBook();
                }
            });
            classRoomDTOList = generalDAO.getClassRoomByStudentList(false);
            spnChooseClassAdapter = new SPNChooseClassAdapter(AddTransactionActivity.this, classRoomDTOList);
            spnClassRoom.setAdapter(spnChooseClassAdapter);

//        updateSPNStudent(classRoomDTOList.get(0).getClassRoom());


            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rvPickedBook.setLayoutManager(llm);

            lvSuggestBook.setAdapter(suggestBookAdapter);
            rvPickedBook.setAdapter(pickedBookAdapter);
        }


    }

    private void getAllListBook() {
        imvUpDownSuggest.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("log", response );
                imvUpDownSuggest.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(View.GONE);
                listBookSearch = ObjectMapperUtils.stringToTypeReference(response, new TypeReference<List<BookDTO>>() {});
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imvUpDownSuggest.setVisibility(View.VISIBLE);
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> payload =  new HashMap<>();
                payload.put("action", GoogleSheetConstants.ACTION_GET_ALL_BOOK);
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

    private void updateSPNStudent(String classRoom) {
        Log.e("log","updateSPNStudent");
        if(isDatTruoc) {
            studentDTOList = new ArrayList<>();
            studentDTOList.add(new StudentDTO(0, orderDTO.getStudentName(), orderDTO.getClassRoom()));
        }else {
            studentDTOList = generalDAO.findStudentByClassRoom(classRoom);
        }
            spnChooseStudentAdapter = new SPNChooseStudentAdapter(
                AddTransactionActivity.this,
                studentDTOList
        );
        spnStudent.setAdapter(spnChooseStudentAdapter);

        Log.e("log",ObjectMapperUtils.dtoToString(studentDTOList));

//        StudentDTO item = studentDTOList.get(0);
//        transactionDTO.setClassRoom(item.getClassRoom());
//        transactionDTO.setStudentName(item.getStudentName());
//        transactionDTO.setStudentId(item.getId());

    }

    public void calculatorBook() {
        Integer total = 0;
        for (BookDTO item : listBookPicked){
            total +=item.getQuantity();
        }
        tvBookTotal.setText("Sách: " + total +" quyển");
    }

    void setStatusSuggestView(int status) {
        lvSuggestBook.setVisibility(status);

        if (View.GONE == status) {
            imvUpDownSuggest.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
        } else if (View.VISIBLE == status) {
            imvUpDownSuggest.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        } else {

        }
    }


    void handleOrder(OrderDTO orderDTO){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);
                ResponseDTO responseDTO = ObjectMapperUtils.stringToDTO(response, ResponseDTO.class);
                if(responseDTO.getStatusCode().equals(GoogleSheetConstants.STATUS_SUCCESS)){

                }else {
//                    NotifyUtils.defaultNotify(getApplicationContext(), responseDTO.getMessage());
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
                orderDTO.setStatus(DBConstants.STATUS_ORDER_APPROVED);
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
}