package com.example.librarymanagerment.view.admin.phienmuonsach;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.dto.BookDTO;

import java.util.List;

public class PickedBookAdapter extends RecyclerView.Adapter<PickedBookAdapter.MyViewHolder> {

    Context context;
    List<BookDTO> items;

    public PickedBookAdapter(Context context, List<BookDTO> items) {
        this.context = context;
        this.items = items;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgRemove;
        TextView tvName;
        TextView tvQuantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRemove = itemView.findViewById(R.id.img_item_picked_remove_book);
            tvName = itemView.findViewById(R.id.tv_item_picked_book_name);
            tvQuantity = itemView.findViewById(R.id.tv_item_picked_book_quantity);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_book_picked, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookDTO item = items.get(position);
        int i = position;
        holder.tvName.setText(item.getName());
        holder.tvQuantity.setText("(" + item.getQuantity() + ")");


        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("position", String.valueOf(i));
                AddTransactionActivity.listBookPicked.remove(i);
                AddTransactionActivity.pickedBookAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void onUpdateItemList() {
//        items = generalDAO.findAllBookCollection();
        notifyDataSetChanged();
    }
}
