package com.example.librarymanagerment.view.admin.danhsachtaikhoan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.example.librarymanagerment.core.dto.AccountDTO;
import com.example.librarymanagerment.core.dto.BookReadingDTO;
import com.example.librarymanagerment.core.utils.AssetUtils;

import java.util.List;

public class ListAccountAdapter extends RecyclerView.Adapter<ListAccountAdapter.MyViewHolder> {

    Context context;
    List<AccountDTO> items;
    OnItemClickListener listener;

    public ListAccountAdapter(Context context, List<AccountDTO> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public ListAccountAdapter(Context context, List<AccountDTO> items) {
        this.context = context;
        this.items = items;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSTT, tvDisplayName, tvUserName;
        Button btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSTT = itemView.findViewById(R.id.tv_item_account_stt);
            tvDisplayName = itemView.findViewById(R.id.tv_item_account_display_name);
            tvUserName = itemView.findViewById(R.id.tv_item_account_user_name);
            btnDelete = itemView.findViewById(R.id.btn_item_account_delete);
        }

        public void bindOnClickItem(final AccountDTO item, final OnItemClickListener listener) {
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemDeleteClick(item);
                }
            });
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_account, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AccountDTO item = items.get(position);
        holder.bindOnClickItem(item, listener);

        holder.tvSTT.setText(String.valueOf(position + 1));
        holder.tvDisplayName.setText(item.getDisplayName());
        holder.tvUserName.setText(item.getUserName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
