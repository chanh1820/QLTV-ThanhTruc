package com.example.librarymanagerment.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.DBHelper;
import com.example.librarymanagerment.core.cache.AccountCache;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.constants.GoogleSheetConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.AccountDTO;
import com.example.librarymanagerment.core.service.MasterDataService;
import com.example.librarymanagerment.core.utils.NotifyUtils;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.example.librarymanagerment.core.version.DBVersionHelper;
import com.example.librarymanagerment.core.version.VersionConstant;
import com.example.librarymanagerment.core.version.VersionService;
import com.example.librarymanagerment.view.login.SignInActivity;
import com.example.librarymanagerment.view.admin.main2.Main2Activity;
import com.example.librarymanagerment.view.user.mainuser.MainUserActivity;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {
    AccountDTO accountDTO = new AccountDTO();
    GeneralDAO generalDAO;
    MasterDataService masterDataService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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
        VersionService.processCheckVersion(getApplicationContext());

        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("TimerExample", "processSyncCount" + VersionConstant.processSyncCount);
                if(VersionConstant.processSyncCount == -1){
                    nextActivity();
                }else {
                    handler.postDelayed(this, 100);
                }
            }
        },100);


    }

    @Override
    protected void onResume() {
        super.onResume();
        VersionConstant.processSyncCount = 0;
    }

    private void nextActivity() {
        loadMasterData();
        accountDTO = AccountCache.getCache(getApplicationContext());
        Intent intent = new Intent();
        if(accountDTO.getFlagLogin().toString().equals(GoogleSheetConstants.FLAG_NONE_LOGIN)){
            intent = new Intent(SplashActivity.this, SignInActivity.class);
            startActivity(intent);
        }else {
            switch (accountDTO.getRole()){
                case GoogleSheetConstants.ACCOUNT_TYPE_USER:
                    intent = new Intent(SplashActivity.this, MainUserActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case  GoogleSheetConstants.ACCOUNT_TYPE_ADMIN:
                    intent = new Intent(SplashActivity.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    NotifyUtils.defaultNotify(getApplicationContext(), "Tài khoản không hợp lệ");
                    break;
            }
        }
    }
    
    void loadMasterData(){
        generalDAO = new GeneralDAO(getApplicationContext());
        DBConstants.CLASS_ROOM_lIST = generalDAO.getClassRoomByStudentList(false);
        Log.e("loadMasterData", ObjectMapperUtils.dtoToString(DBConstants.CLASS_ROOM_lIST));
    }
    
}