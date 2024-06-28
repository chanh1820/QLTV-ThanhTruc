package com.example.librarymanagerment.view.user.muonsach.sach;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.utils.AssetUtils;
import com.example.librarymanagerment.core.utils.StringUtils;
import com.example.librarymanagerment.view.user.mainuser.MainUserActivity;

import java.util.List;

public class ListBookOrderAdapter extends RecyclerView.Adapter<ListBookOrderAdapter.MyViewHolder> {

    ImageView imvAddImageData;
    Context context;
    List<BookDTO> items;
    OnItemClickListener listener;

    public ListBookOrderAdapter(Context context, List<BookDTO> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public ListBookOrderAdapter(Context context, List<BookDTO> items) {
        this.context = context;
        this.items = items;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgItemBookDelete, imgItemBookEdit;
        TextView tvItemBookCode, tvItemBookName, tvItemBookCollectionName, tvQuantity;
        ImageView imvImage;

        Button btnAddBookOrder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imvImage = itemView.findViewById(R.id.tv_item_book_order_image);
            tvItemBookCode = itemView.findViewById(R.id.tv_item_book_order_code);
            tvItemBookName = itemView.findViewById(R.id.tv_item_book_order_name);
            tvItemBookCollectionName = itemView.findViewById(R.id.tv_item_book_order_book_collection_code);
            tvQuantity = itemView.findViewById(R.id.tv_item_book_order_quantity);
            btnAddBookOrder = itemView.findViewById(R.id.btn_item_book_order_add);
        }
        public void bind(final BookDTO item, final OnItemClickListener listener) {
            btnAddBookOrder.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_book_order, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        BookDTO item = items.get(position);
        holder.bind(item, listener);

        holder.tvItemBookCode.setText("Mã : " + item.getCode());
        holder.tvItemBookName.setText("Tên : " + item.getName());
        holder.tvQuantity.setText("Số lượng gốc: " + item.getQuantity());
        holder.tvItemBookCollectionName
                .setText("Danh mục: " + MainUserActivity.mapListBookCollectionDTO.get(item.getCollectionCode()));
        if(org.apache.commons.lang3.StringUtils.isEmpty(item.getImageBase64())){
            Drawable imageDrawble = AssetUtils.getImageFromAsset(DBConstants.TYPE_ASSET_BOOK_LINK, item.getImageFile(), context);
            if (imageDrawble != null){
                holder.imvImage.setImageDrawable(imageDrawble);
            }else {
                holder.imvImage.setImageResource(R.drawable.ic_book_link_default);
            }
        }else {
            holder.imvImage.setVisibility(View.VISIBLE);
            holder.imvImage.setImageBitmap(StringUtils.StringToBitMap(item.getImageBase64()));
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
