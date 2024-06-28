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

public class YoutubeFragment extends Fragment {
    WebView wvVideo;

    public YoutubeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_youtube, container, false);
        wvVideo = rootView.findViewById(R.id.wv_video);
        String videoId = VideoViewActivity.videoId;
        Log.e("a",videoId);

        String iframe = "<iframe width=\"80%\" height=\"80%\" src=\"https://www.youtube.com/embed/" + videoId + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        wvVideo.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return false;

            }
        });
//        wvVideo.setWebChromeClient(new WebChromeClient());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        wvVideo.getSettings().setJavaScriptEnabled(true);
        wvVideo.getSettings().setLoadWithOverviewMode(false);
        wvVideo.getSettings().setUseWideViewPort(true);
        wvVideo.getSettings().setDomStorageEnabled(true);
        wvVideo.getSettings().setMediaPlaybackRequiresUserGesture(false);
        wvVideo.setLayoutParams(p);
        wvVideo.loadData(iframe, "text/html", "utf-8");
        return  rootView;
    }


}
