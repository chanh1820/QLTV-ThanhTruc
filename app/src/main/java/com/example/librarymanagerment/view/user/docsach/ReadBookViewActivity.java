package com.example.librarymanagerment.view.user.docsach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.KeyConstants;

public class ReadBookViewActivity extends AppCompatActivity {
    WebView wvReadBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book_view);
        initView();
    }

    private void initView() {
        wvReadBook = findViewById(R.id.wv_read_book);
        Log.e("log11",getIntent().getStringExtra(KeyConstants.INTENT_WEB_VIEW_LINK ));
//        wvReadBook.loadUrl(getIntent().getStringExtra(KeyConstants.INTENT_WEB_VIEW_LINK));
        wvReadBook.setWebChromeClient(new WebChromeClient());
        wvReadBook.getSettings().setJavaScriptEnabled(true);
        wvReadBook.getSettings().setLoadWithOverviewMode(true);
        wvReadBook.getSettings().setUseWideViewPort(true);
        wvReadBook.getSettings().setDomStorageEnabled(true);

        wvReadBook.loadUrl(getIntent().getStringExtra(KeyConstants.INTENT_WEB_VIEW_LINK ));
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse(getIntent().getStringExtra(KeyConstants.INTENT_WEB_VIEW_LINK)));
//        startActivity(i);
    }
}