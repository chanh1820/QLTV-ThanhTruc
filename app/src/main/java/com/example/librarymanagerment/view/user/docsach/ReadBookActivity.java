package com.example.librarymanagerment.view.user.docsach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.KeyConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.BookReadingDTO;
import com.example.librarymanagerment.core.sco.BookReadingSCO;

import java.util.ArrayList;
import java.util.List;

public class ReadBookActivity extends AppCompatActivity {
    ReadBookAdapter readBookAdapter;

    RecyclerView rvReadBook;

    EditText edtInputSearch;
    List<BookReadingDTO> bookReadingDTOList = new ArrayList<>();
    List<String> collectionList = new ArrayList<>();
    List<String> classRoomList = new ArrayList<>();
    Spinner spnChooseCollectionParent, spnChooseClass;
    GeneralDAO generalDAO;

    BookReadingSCO bookReadingSCO = new BookReadingSCO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);

        initView();
        action();

    }

    private void action() {
        spnChooseCollectionParent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("log i", String.valueOf(i));
                bookReadingSCO.setCollectionParent((String) view.getTag());
                onUpdateRecyclerView(bookReadingSCO);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnChooseClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bookReadingSCO.setClassRoom((String) view.getTag());
                onUpdateRecyclerView(bookReadingSCO);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                bookReadingSCO.setSearchName(edtInputSearch.getText().toString().trim());
                onUpdateRecyclerView(bookReadingSCO);
            }
        });
    }

    private void initView() {
        generalDAO = new GeneralDAO(getApplicationContext());
        collectionList = generalDAO.getCollectionParentByListBookReading();
        classRoomList = generalDAO.getClassRoomByListBookReading();

        rvReadBook = findViewById(R.id.rv_read_book);
        edtInputSearch = findViewById(R.id.edt_book_reading_input_search);
        spnChooseCollectionParent = findViewById(R.id.spn_list_book_reading_choose_collection);
        spnChooseClass = findViewById(R.id.spn_list_book_reading_choose_class_room);

        spnChooseCollectionParent.setAdapter(new SPNChooseCollectionParentAdapter(getApplicationContext(), collectionList));
        spnChooseCollectionParent.setSelection(getIntent().getIntExtra(KeyConstants.INTENT_POSITION, 0));
        spnChooseClass.setAdapter(new SPNChooseCollectionParentAdapter(getApplicationContext(), classRoomList));


        onUpdateRecyclerView(bookReadingSCO);


    }
    public final void onUpdateRecyclerView(BookReadingSCO bookReadingSCO){
        bookReadingDTOList = generalDAO.findBookReading(bookReadingSCO);
        rvReadBook.setLayoutManager(new LinearLayoutManager(this));
        readBookAdapter = new ReadBookAdapter(ReadBookActivity.this, bookReadingDTOList, new OnItemReadBookClickListener() {
            @Override
            public void onItemClick(BookReadingDTO item) {
                Intent intent = new Intent(ReadBookActivity.this, ReadBookViewActivity.class);
                intent.putExtra(KeyConstants.INTENT_WEB_VIEW_LINK, item.getResource());
                startActivity(intent);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(ReadBookActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvReadBook.setLayoutManager(llm);
        rvReadBook.setAdapter(readBookAdapter);
    }

}