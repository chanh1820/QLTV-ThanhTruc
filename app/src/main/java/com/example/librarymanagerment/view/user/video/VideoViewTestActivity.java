package com.example.librarymanagerment.view.user.video;

import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;

import com.example.librarymanagerment.R;

public class VideoViewTestActivity extends FragmentActivity {
    WebView wvVideo, wvWeb;

    public static String videoId;
    public static String webUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_video_view);

        initView();
    }


    private void initView() {
        String iframe = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + "vWh41JLvOTM" + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        wvVideo = findViewById(R.id.wv_video);

        wvVideo.setWebChromeClient(new WebChromeClient());
        wvVideo.getSettings().setJavaScriptEnabled(true);
        wvVideo.getSettings().setLoadWithOverviewMode(false);
        wvVideo.getSettings().setUseWideViewPort(true);
        wvVideo.getSettings().setDomStorageEnabled(true);
        wvVideo.loadData(iframe, "text/html", "utf-8");

//        if(StringUtils.isNotBlank(webUrl) && !webUrl.trim().equals("0")){
//            wvWeb.setWebChromeClient(new WebChromeClient());
//            wvWeb.getSettings().setJavaScriptEnabled(true);
//            wvWeb.getSettings().setLoadWithOverviewMode(false);
//            wvWeb.getSettings().setUseWideViewPort(true);
//            wvWeb.getSettings().setDomStorageEnabled(true);
//            Log.e("log11",videoId);
//            wvWeb.loadUrl(webUrl);
//        }else {
//            wvWeb.setVisibility(View.INVISIBLE);
//        }
    }
}