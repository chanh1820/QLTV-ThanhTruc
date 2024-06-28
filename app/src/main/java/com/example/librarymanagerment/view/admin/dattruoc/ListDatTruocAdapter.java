package com.example.librarymanagerment.view.admin.dattruoc;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.dto.OrderDTO;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class ListDatTruocAdapter extends RecyclerView.Adapter<ListDatTruocAdapter.MyViewHolder> {

    ImageView imvAddImageData;
    Context context;
    List<OrderDTO> items;
    DatTruocClickListener listener;

    public ListDatTruocAdapter(Context context, List<OrderDTO> items, DatTruocClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public ListDatTruocAdapter(Context context, List<OrderDTO> items) {
        this.context = context;
        this.items = items;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvStudentInfo, tvBookInfo, tvCreateDate;

        Button btnPick, btnReject;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_item_order_id);
            tvStudentInfo = itemView.findViewById(R.id.tv_item_order_student_info);
            tvBookInfo = itemView.findViewById(R.id.tv_item_order_book_info);
            tvCreateDate = itemView.findViewById(R.id.tv_item_order_create_date);
            btnPick = itemView.findViewById(R.id.btn_order_pick);
            btnReject = itemView.findViewById(R.id.btn_order_reject);
        }
        public void bindPick(final OrderDTO item, final DatTruocClickListener listener) {
            btnPick.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onButtonPickClick(item);
                }
            });
        }
        public void bindReject(final OrderDTO item, final DatTruocClickListener listener) {
            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onButtonRejectClick(item);
                }
            });
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrderDTO item = items.get(position);
        holder.bindPick(item, listener);
        holder.bindReject(item, listener);

        holder.tvId.setText("ID : " + item.getId());
        holder.tvStudentInfo.setText(item.getStudentName() + " - " +item.getClassRoom());
        List<BookDTO> bookDTOList = ObjectMapperUtils.stringToTypeReference(item.getBooksData(), new TypeReference<List<BookDTO>>(){});
        StringBuilder bookInfo = new StringBuilder();
        boolean isDownLine = false;
        for (BookDTO bookDTO : bookDTOList) {
            if(isDownLine){
                bookInfo.append("\n");
            }
            bookInfo.append("("+bookDTO.getQuantity()+") - " + bookDTO.getName());
            isDownLine = true;
        }
        holder.tvBookInfo.setText(bookInfo.toString());
        holder.tvCreateDate.setText(item.getCreateDate());
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
