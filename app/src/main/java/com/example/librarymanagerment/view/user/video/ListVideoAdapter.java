package com.example.librarymanagerment.view.user.video;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.dto.VideoDTO;
import com.example.librarymanagerment.core.utils.AssetUtils;
import com.example.librarymanagerment.core.utils.StringUtils;
import com.example.librarymanagerment.view.user.mainuser.MainUserActivity;
import com.example.librarymanagerment.view.user.muonsach.sach.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListVideoAdapter extends RecyclerView.Adapter<ListVideoAdapter.MyViewHolder> {

    Context context;
    List<VideoDTO> items;
    OnItemClickListener listener;

    public ListVideoAdapter(Context context, List<VideoDTO> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public ListVideoAdapter(Context context, List<VideoDTO> items) {
        this.context = context;
        this.items = items;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lnParent;
        TextView tvName;
        ImageView imvThumbnail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lnParent = itemView.findViewById(R.id.ln_item_video_parent);
            tvName = itemView.findViewById(R.id.tv_item_video_name);
            imvThumbnail = itemView.findViewById(R.id.imv_item_video_thumbnail);
        }
        public void bind(final VideoDTO item, final OnItemClickListener listener) {
            lnParent.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_video, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        VideoDTO item = items.get(position);
        holder.bind(item, listener);

        holder.tvName.setText(item.getName());
        Picasso.get().load(item.getThumbnail()).into(holder.imvThumbnail);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
