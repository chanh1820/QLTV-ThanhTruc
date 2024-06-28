package com.example.librarymanagerment.view.user.video;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.librarymanagerment.view.user.video.fragment.WebFragment;
import com.example.librarymanagerment.view.user.video.fragment.YoutubeFragment;

public class VideoViewAdapter extends FragmentStateAdapter {
    public VideoViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
//    public VideoViewAdapter(@NonNull FragmentManager fm, int behavior) {
//        super(fm, behavior);
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new YoutubeFragment();
            case 1:
                return new WebFragment();
            default:
                return new YoutubeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
