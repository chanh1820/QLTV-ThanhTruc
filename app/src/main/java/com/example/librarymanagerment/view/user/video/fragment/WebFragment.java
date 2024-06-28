package com.example.librarymanagerment.view.user.video.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.view.user.video.VideoViewActivity;
import com.example.librarymanagerment.view.user.video.VideoViewTestActivity;

public class WebFragment extends Fragment {
    WebView wvWeb;

    public WebFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_web, container, false);

        wvWeb = rootView.findViewById(R.id.wv_web);
        String webUrl = VideoViewActivity.webUrl;
        Log.e("a",webUrl);
        wvWeb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return false;

            }
        });
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

//        wvWeb.setWebChromeClient(new WebChromeClient());
        wvWeb.getSettings().setJavaScriptEnabled(true);
        wvWeb.getSettings().setLoadWithOverviewMode(false);
        wvWeb.getSettings().setUseWideViewPort(true);
        wvWeb.requestFocus(View.FOCUS_UP);
        wvWeb.getSettings().setDomStorageEnabled(true);
        wvWeb.getSettings().setMediaPlaybackRequiresUserGesture(false);
        wvWeb.setLayoutParams(p);
        wvWeb.loadUrl("https://thiengioi.com/news/khuyen-mai-the-nap-30");

//        WebView webView = new WebView(getContext());
//
//        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setSavePassword(false);
//        webSettings.setSaveFormData(false);
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//
//        webView.setLayoutParams(p);
//        webView.loadUrl("http://www.google.com/");
        return  rootView;
    }
}
