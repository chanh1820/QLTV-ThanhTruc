package com.example.librarymanagerment.view.user.muonsach.giohang;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.dto.OrderPickedDTO;
import com.example.librarymanagerment.core.utils.StringUtils;
import com.example.librarymanagerment.view.user.mainuser.MainUserActivity;
import com.example.librarymanagerment.view.user.muonsach.sach.OnItemClickListener;

import java.util.List;

public class ListOrderPickedAdapter extends RecyclerView.Adapter<ListOrderPickedAdapter.MyViewHolder> {

    ImageView imvAddImageData;
    Context context;
    List<OrderPickedDTO> items;
    OnItemClickListener listener;

    public ListOrderPickedAdapter(Context context, List<OrderPickedDTO> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public ListOrderPickedAdapter(Context context, List<OrderPickedDTO> items) {
        this.context = context;
        this.items = items;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgItemBookDelete, imgItemBookEdit;
        TextView tvItemBookCode, tvItemBookName, tvItemBookCollectionName, tvQuantity;
        ImageView imvImage;

        Button btnIncrease, btnDecrease;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imvImage = itemView.findViewById(R.id.imv_item_book_order_picked_image);
            tvItemBookCode = itemView.findViewById(R.id.tv_item_book_order_picked_code);
            tvItemBookName = itemView.findViewById(R.id.tv_item_book_order_picked_name);
            tvItemBookCollectionName = itemView.findViewById(R.id.tv_item_book_order_picked_book_collection_code);
            btnIncrease = itemView.findViewById(R.id.btn_book_order_picked_increase);
            btnDecrease = itemView.findViewById(R.id.btn_book_order_picked_decrease);
            tvQuantity = itemView.findViewById(R.id.tv_item_book_order_picked_quantity);
        }
        public void bindIncrease(final OrderPickedDTO item, final OnItemClickListener listener) {
            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClickIncreaseButton(item);
                }
            });
        }
        public void bindDecrease(final OrderPickedDTO item, final OnItemClickListener listener) {
            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClickDecreaseButton(item);
                }
            });
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_book_order_picked, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrderPickedDTO item = items.get(position);
        holder.bindIncrease(item, listener);
        holder.bindDecrease(item, listener);

        holder.tvItemBookCode.setText("Mã : " + item.getBookCode());
        holder.tvItemBookName.setText("Tên : " + item.getBookName());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        Log.e("item.getBookDTO().getCollectionCode()", item.getBookDTO().getCollectionCode());
        holder.tvItemBookCollectionName
                .setText("Danh mục: " + MainUserActivity.mapListBookCollectionDTO.get(item.getBookDTO().getCollectionCode()));
        if(item.getBookDTO().getImageBase64()== null || org.apache.commons.lang3.StringUtils.isEmpty(item.getBookDTO().getImageBase64())){
            holder.imvImage.setVisibility(View.INVISIBLE);
        }else {
            holder.imvImage.setVisibility(View.VISIBLE);
            holder.imvImage.setImageBitmap(StringUtils.StringToBitMap(item.getBookDTO().getImageBase64()));
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imvAddImageData.setImageURI(selectedImage);
        }
    }
}
