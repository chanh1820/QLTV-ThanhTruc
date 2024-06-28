package com.example.librarymanagerment.view.user.video;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.KeyConstants;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.apache.commons.lang3.StringUtils;

public class VideoViewActivity extends AppCompatActivity {
    WebView wvVideo, wvWeb;

    private TabLayout tlMenu;
    private ViewPager2 vpVideo;
    VideoViewAdapter videoViewAdapter;

    public static String videoId;
    public static String webUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_video_view_2);

        initView();
    }


//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            wvWeb.setVisibility(View.GONE);
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            wvWeb.setVisibility(View.VISIBLE);
//        }
//    }
    private void initView() {
        videoId = getIntent().getStringExtra(KeyConstants.INTENT_WEB_VIEW_VIDEO_ID );
        webUrl = getIntent().getStringExtra(KeyConstants.INTENT_WEB_VIEW_VIDEO_WEB );
        tlMenu = findViewById(R.id.tl_video_view_menu);
        vpVideo = findViewById(R.id.vp2_video_view_fragment);

        videoViewAdapter = new VideoViewAdapter(this);
        vpVideo.setAdapter(videoViewAdapter);

        new TabLayoutMediator(tlMenu, vpVideo, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Video");
                    break;
                case 1:
                    tab.setText("Bài học");
                    break;
            }
        }).attach();




















//        String iframe = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
//        wvVideo = findViewById(R.id.wv_video);
//        wvWeb = findViewById(R.id.wv_web);
//
//        wvVideo.setWebChromeClient(new WebChromeClient());
//        wvVideo.getSettings().setJavaScriptEnabled(true);
//        wvVideo.getSettings().setLoadWithOverviewMode(false);
//        wvVideo.getSettings().setUseWideViewPort(true);
//        wvVideo.getSettings().setDomStorageEnabled(true);
//        wvVideo.loadData(iframe, "text/html", "utf-8");
//
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