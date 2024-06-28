package com.example.librarymanagerment.view.user.lichsu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.dto.OrderDTO;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.example.librarymanagerment.view.admin.dattruoc.DatTruocClickListener;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    ImageView imvAddImageData;
    Context context;
    List<OrderDTO> items;
    DatTruocClickListener listener;

    public OrderHistoryAdapter(Context context, List<OrderDTO> items, DatTruocClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public OrderHistoryAdapter(Context context, List<OrderDTO> items) {
        this.context = context;
        this.items = items;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvStudentInfo, tvBookInfo, tvCreateDate, tvStatus;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_item_order_history_id);
            tvStudentInfo = itemView.findViewById(R.id.tv_item_order_history_student_info);
            tvBookInfo = itemView.findViewById(R.id.tv_item_order_history_book_info);
            tvCreateDate = itemView.findViewById(R.id.tv_item_order_history_create_date);
            tvStatus = itemView.findViewById(R.id.tv_item_order_history_status);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrderDTO item = items.get(position);
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
        if (item.getStatus().equals(DBConstants.STATUS_ORDER_WAITING_APPROVE)){
            holder.tvStatus.setText("Trạng thái: Đang chờ");
        }else if (item.getStatus().equals(DBConstants.STATUS_ORDER_APPROVED)){
            holder.tvStatus.setText("Trạng thái: Đã chấp nhận");
        }else if (item.getStatus().equals(DBConstants.STATUS_ORDER_REJECT)){
            holder.tvStatus.setText("Trạng thái: Đã huỷ");
        }else {
            holder.tvStatus.setText("Trạng thái: không xác định");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
