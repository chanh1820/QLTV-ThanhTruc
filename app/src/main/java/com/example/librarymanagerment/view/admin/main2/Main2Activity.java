package com.example.librarymanagerment.view.admin.main2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.cache.AccountCache;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.DBHelper;
import com.example.librarymanagerment.core.constants.KeyConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.AccountDTO;
import com.example.librarymanagerment.core.dto.BookCollectionDTO;
import com.example.librarymanagerment.core.version.DBVersionHelper;
import com.example.librarymanagerment.core.version.VersionService;
import com.example.librarymanagerment.view.admin.danhsachphieumuon.ListTransactionActivity;
import com.example.librarymanagerment.view.admin.danhsachtaikhoan.ListAccountActivity;
import com.example.librarymanagerment.view.admin.danhsachtaikhoan.registry.RegistryAccountActivity;
import com.example.librarymanagerment.view.admin.listdanhmucsach.ListBookCollectionActivity;
import com.example.librarymanagerment.view.admin.phienmuonsach.AddTransactionActivity;
import com.example.librarymanagerment.view.admin.dattruoc.DatTruocActivity;
import com.example.librarymanagerment.view.doimatkhau.ChangePasswordActivity;
import com.example.librarymanagerment.view.login.SignInActivity;
import com.example.librarymanagerment.view.admin.listsach.ListBookActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    Button btnThemPhieuMuon,btnDanhMuc, btnSach, btnDanhSachPhieuMuon, btnDanhSachDatTruoc;
    ImageView imvSettings;

    ChooseSettingsAdapter chooseSettingsAdapter;

    GeneralDAO generalDAO;
    public static List<BookCollectionDTO> listBookCollectionDTO = new ArrayList<>();
    public static Map<String, String> mapListBookCollectionDTO = new HashMap<String, String>();

    List<String> collectionParentList = new ArrayList<>();
    private SharedPreferences accountCache;
    public static AccountDTO accountDTO = new AccountDTO();
    ChooseBookCollectionAdapter chooseBookCollectionAdapter;
    ChooseBookParentCollectionAdapter chooseBookParentCollectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        accountCache = getSharedPreferences("account",MODE_PRIVATE);
        String passwordCurrent = accountCache.getString("pass_word", "");
        Log.e("hello","log");
        if(passwordCurrent.isEmpty()){
            SharedPreferences.Editor accountEdit = accountCache.edit();
            accountEdit.putString("pass_word", "123456");
            accountEdit.commit();
        }
        accountDTO.setPassWord(accountCache.getString("pass_word",""));
        firebase();
        initView();
        actionView();
    }

    private void actionView() {
        btnDanhMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ListBookCollectionActivity.class);
                startActivity(i);
            }
        });
        btnSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(Main2Activity.this);
                dialog.setContentView(R.layout.dialog_choose_book);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setAttributes(lp);
                dialog.show();
//                chooseBookCollectionAdapter = new ChooseBookCollectionAdapter(getApplicationContext(), listBookCollectionDTO);
                chooseBookParentCollectionAdapter =  new ChooseBookParentCollectionAdapter(getApplicationContext(), collectionParentList);
                GridView gvListSession = dialog.findViewById(R.id.gv_book_collection);
                Button btnBack = dialog.findViewById(R.id.btn_dialog_book_collection_back);

                gvListSession.setAdapter(chooseBookParentCollectionAdapter);
                gvListSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(Main2Activity.this, ListBookActivity.class);
                        String item = (String) view.getTag();
                        intent.putExtra(KeyConstants.INTENT_COLLECTION_PARENT_NAME, item);
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
        btnThemPhieuMuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddTransactionActivity.class);
                startActivity(i);
            }
        });
        btnDanhSachPhieuMuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openDialog();
                Intent i = new Intent(getApplicationContext(), ListTransactionActivity.class);
                startActivity(i);

            }
        });
        imvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(Main2Activity.this);
                dialog.setContentView(R.layout.dialog_setting);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setAttributes(lp);

                dialog.show();

                chooseSettingsAdapter = new ChooseSettingsAdapter(DBConstants.listSettingAdmimDTO, getApplicationContext());
                GridView gvListSession = dialog.findViewById(R.id.gv_list_session);
                gvListSession.setAdapter(chooseSettingsAdapter);
                gvListSession.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        if (position == 0) {
                            intent = new Intent(Main2Activity.this, ListAccountActivity.class);
                        } else if (position == 1) {
                            intent = new Intent(Main2Activity.this, ChangePasswordActivity.class);
                        } else if (position == 2) {
                            intent = new Intent(Main2Activity.this, SignInActivity.class);
                            AccountCache.removeCache(getApplicationContext());
                            finishAffinity();
                        }
                        startActivity(intent);
                        dialog.dismiss();

                    }
                });
            }
        });

        btnDanhSachDatTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, DatTruocActivity.class);
                startActivity(intent);
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
        DBVersionHelper dbVersionHelper = new DBVersionHelper(this);
        try {
            dbVersionHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnThemPhieuMuon = findViewById(R.id.btn_main_them_phieu_muon);
        btnDanhMuc = findViewById(R.id.btn_main_danh_muc);
        btnSach = findViewById(R.id.btn_main_sach);
        btnDanhSachPhieuMuon = findViewById(R.id.btn_main_danh_sach_phieu);
        btnDanhSachDatTruoc = findViewById(R.id.btn_main_danh_sach_dat_truoc);
        imvSettings = findViewById(R.id.imv_settings);

        generalDAO = new GeneralDAO(getApplicationContext());
        accountDTO.setPassWord(accountCache.getString("pass_word",""));
        collectionParentList = generalDAO.getCollectionParentByListBook();
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

    public void openDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        LayoutInflater inflater = Main2Activity.this.getLayoutInflater();
        View view1=inflater.inflate(R.layout.dialog_validate_pass_word,null);
        builder.setView(view1);

        EditText edtValidate = (EditText) view1.findViewById(R.id.edt_validate_password);

        builder.setTitle("Xác thực");
        builder.setPositiveButton("Xác thực", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if(accountDTO.getPassWord().equals(edtValidate.getText().toString().trim())){
                        Intent i = new Intent(getApplicationContext(), ListTransactionActivity.class);
                        startActivity(i);
                    }else {
                        Toast.makeText(getApplicationContext(), "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }
//    Callable<?> start(){
//        Intent i = new Intent(getApplicationContext(), ListTransactionActivity.class);
//        startActivity(i);
//        return null;
//    }
}