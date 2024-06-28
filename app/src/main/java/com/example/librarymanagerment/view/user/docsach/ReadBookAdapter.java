package com.example.librarymanagerment.view.user.docsach;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.dto.BookLinkDTO;
import com.example.librarymanagerment.core.dto.BookReadingDTO;
import com.example.librarymanagerment.core.utils.AssetUtils;

import java.util.List;

public class ReadBookAdapter extends RecyclerView.Adapter<ReadBookAdapter.MyViewHolder> {

    Context context;
    List<BookReadingDTO> items;
    OnItemReadBookClickListener listener;

    public ReadBookAdapter(Context context, List<BookReadingDTO> items, OnItemReadBookClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public ReadBookAdapter(Context context, List<BookReadingDTO> items) {
        this.context = context;
        this.items = items;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lnParent;
        TextView tvId, tvName;
        ImageView imvIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lnParent = itemView.findViewById(R.id.ln_item_book_parent);
            tvId = itemView.findViewById(R.id.tv_item_book_link_id);
            tvName = itemView.findViewById(R.id.tv_item_book_link_name);
            imvIcon = itemView.findViewById(R.id.tv_item_book_icon);
        }
        public void bindOnClickItem(final BookReadingDTO item, final OnItemReadBookClickListener listener) {
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
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_book_link, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookReadingDTO item = items.get(position);
        holder.bindOnClickItem(item, listener);

        holder.tvId .setText("ID: " + item.getId());
        holder.tvName .setText(item.getName());
        Drawable drawable = AssetUtils.getImageFromAsset(DBConstants.TYPE_ASSET_BOOK_LINK, item.getImageFile(), context);
        if (drawable != null) {
            holder.imvIcon.setImageDrawable(drawable);
        }else {
            holder.imvIcon.setImageResource(R.drawable.ic_book_link_default);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
