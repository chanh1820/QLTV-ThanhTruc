package com.example.librarymanagerment.view.user.mainuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.GridView;
import android.widget.ImageView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.DBHelper;
import com.example.librarymanagerment.core.cache.AccountCache;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.constants.KeyConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.AccountDTO;
import com.example.librarymanagerment.core.dto.BookCollectionDTO;
import com.example.librarymanagerment.core.version.DBVersionHelper;
import com.example.librarymanagerment.core.version.VersionService;
import com.example.librarymanagerment.view.doimatkhau.ChangePasswordActivity;
import com.example.librarymanagerment.view.login.SignInActivity;
import com.example.librarymanagerment.view.admin.main2.ChooseBookParentCollectionAdapter;
import com.example.librarymanagerment.view.admin.main2.ChooseSettingsAdapter;
import com.example.librarymanagerment.view.user.docsach.ReadBookActivity;
import com.example.librarymanagerment.view.user.lichsu.OrderHistoryActivity;
import com.example.librarymanagerment.view.user.muonsach.sach.ListBookOrderActivity;
import com.example.librarymanagerment.view.user.video.ListVideoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainUserActivity extends AppCompatActivity {
    Button btnSach,btnLichSuMuon, btnDocSach,btnVideo;
    ImageView imvSettings;
    ChooseSettingsAdapter chooseSettingsAdapter;

    GeneralDAO generalDAO;
    public static List<BookCollectionDTO> listBookCollectionDTO = new ArrayList<>();
    public static Map<String, String> mapListBookCollectionDTO = new HashMap<String, String>();
    List<String> collectionParentBookList = new ArrayList<>();
    List<String> collectionParentBookReadingList = new ArrayList<>();
    List<String> categoryVideoList = new ArrayList<>();
    public static AccountDTO accountDTO = new AccountDTO();
    ChooseBookParentCollectionAdapter chooseBookParentCollectionAdapter;
    ChooseCategoryVideoAdapter chooseCategoryVideoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        firebase();
        initView();
        actionView();

    }

    private void actionView() {
        btnSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainUserActivity.this);
                dialog.setContentView(R.layout.dialog_choose_book);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setAttributes(lp);
                dialog.show();

//                chooseBookCollectionAdapter = new ChooseBookCollectionAdapter(getApplicationContext(), listBookCollectionDTO);
                chooseBookParentCollectionAdapter =  new ChooseBookParentCollectionAdapter(getApplicationContext(), collectionParentBookList);
                GridView gvListSession = dialog.findViewById(R.id.gv_book_collection);
                Button btnBack = dialog.findViewById(R.id.btn_dialog_book_collection_back);

                gvListSession.setAdapter(chooseBookParentCollectionAdapter);
                gvListSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainUserActivity.this, ListBookOrderActivity.class);
                        intent.putExtra(KeyConstants.INTENT_POSITION, position);
                        startActivity(intent);
                    }
                });
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainUserActivity.this);
                dialog.setContentView(R.layout.dialog_choose_video);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setAttributes(lp);
                dialog.show();

//                chooseBookCollectionAdapter = new ChooseBookCollectionAdapter(getApplicationContext(), listBookCollectionDTO);
                chooseCategoryVideoAdapter =  new ChooseCategoryVideoAdapter(getApplicationContext(), categoryVideoList);
                GridView gvListSession = dialog.findViewById(R.id.gv_book_collection);
                Button btnBack = dialog.findViewById(R.id.btn_dialog_book_collection_back);

                gvListSession.setAdapter(chooseCategoryVideoAdapter);
                gvListSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainUserActivity.this, ListVideoActivity.class);
                        intent.putExtra(KeyConstants.INTENT_POSITION, position);
                        startActivity(intent);
                    }
                });
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        imvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainUserActivity.this);
                dialog.setContentView(R.layout.dialog_setting);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setAttributes(lp);

                dialog.show();

                chooseSettingsAdapter = new ChooseSettingsAdapter(DBConstants.listSettingDTO2, getApplicationContext());
                GridView gvListSession = dialog.findViewById(R.id.gv_list_session);
                gvListSession.setAdapter(chooseSettingsAdapter);
                gvListSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        if (position == 0) {
                            intent = new Intent(MainUserActivity.this, ChangePasswordActivity.class);
                        } else if (position == 1) {
                            intent = new Intent(MainUserActivity.this, SignInActivity.class);
                            AccountCache.removeCache(getApplicationContext());
                            finishAffinity();
                        }
                        startActivity(intent);
                        dialog.dismiss();

                    }
                });
            }
        });

        btnLichSuMuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainUserActivity.this, OrderHistoryActivity.class);
                startActivity(i);
            }
        });
        btnDocSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainUserActivity.this);
                dialog.setContentView(R.layout.dialog_choose_book);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setAttributes(lp);
                dialog.show();

//                chooseBookCollectionAdapter = new ChooseBookCollectionAdapter(getApplicationContext(), listBookCollectionDTO);
                chooseBookParentCollectionAdapter =  new ChooseBookParentCollectionAdapter(getApplicationContext(), collectionParentBookReadingList);
                GridView gvListSession = dialog.findViewById(R.id.gv_book_collection);
                Button btnBack = dialog.findViewById(R.id.btn_dialog_book_collection_back);

                gvListSession.setAdapter(chooseBookParentCollectionAdapter);
                gvListSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainUserActivity.this, ReadBookActivity.class);
                        intent.putExtra(KeyConstants.INTENT_POSITION, position);
                        startActivity(intent);
                    }
                });
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void initView() {

        generalDAO = new GeneralDAO(getApplicationContext());
        accountDTO = AccountCache.getCache(getApplicationContext());
        btnSach = findViewById(R.id.btn_main_user_sach);
        btnVideo = findViewById(R.id.btn_main_user_video);
        btnLichSuMuon = findViewById(R.id.btn_main_user_lich_su_muon);
        btnDocSach = findViewById(R.id.btn_main_user_doc_sach);
        imvSettings = findViewById(R.id.imv_settings);
        collectionParentBookList = generalDAO.getCollectionParentByListBook();
        collectionParentBookReadingList = generalDAO.getCollectionParentByListBookReading();
        categoryVideoList = generalDAO.getCategoryByListVideo();
        onDataChange();


    }

    private void onDataChange() {
        mapListBookCollectionDTO.clear();
        listBookCollectionDTO = generalDAO.findAllBookCollection();
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