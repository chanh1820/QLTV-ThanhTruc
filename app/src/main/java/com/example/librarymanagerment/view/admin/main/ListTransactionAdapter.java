package com.example.librarymanagerment.view.admin.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.dto.TransactionDTO;

import java.util.List;

public class ListTransactionAdapter extends RecyclerView.Adapter<ListTransactionAdapter.MyViewHolder> {

    Context context;
    List<TransactionDTO> items;
    GeneralDAO generalDAO;

    public ListTransactionAdapter(Context context, List<TransactionDTO> items, GeneralDAO generalDAO) {
        this.context = context;
        this.items = items;
        this.generalDAO = generalDAO;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imvRefund;
        TextView tvListBook, tvStudentInfo, tvTimeline;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imvRefund = itemView.findViewById(R.id.img_item_transaction_refund);

            tvListBook = itemView.findViewById(R.id.tv_item_transaction_list_book_name);
            tvStudentInfo = itemView.findViewById(R.id.tv_item_transaction_student_info);
            tvTimeline = itemView.findViewById(R.id.tv_item_transaction_time_line);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TransactionDTO item = items.get(position);
        StringBuilder listBookName = new StringBuilder();
        boolean flag = false;
        for (BookDTO bookDTO : item.getListBookDTO()) {
            if (flag) {
                listBookName.append("\n");
            }
            listBookName.append("(" + bookDTO.getQuantity() + ")" + bookDTO.getName());
            flag = true;
        }
        holder.tvListBook.setText(listBookName.toString());
        holder.tvStudentInfo.setText(item.getStudentName() + " | " + item.getClassRoom());
        holder.tvTimeline.setText(item.getFromDate() + " - " + item.getToDate());

        if (item.getStatus().equals(DBConstants.TRANSACTION_STATUS_REFUNDED)){
            holder.imvRefund.setImageResource(R.drawable.ic_baseline_check_box_24);
            holder.imvRefund.setOnClickListener(null);

        }else if(item.getStatus().equals(DBConstants.TRANSACTION_STATUS_NOT_REFUNDED)){
            holder.imvRefund.setImageResource(R.drawable.ic_baseline_wifi_protected_setup_24);
            holder.imvRefund.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.dialog_confirm_refund, null);
                    builder.setView(dialogView);
                    builder.setTitle("Trả Sách");
                    builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           item.setStatus(DBConstants.TRANSACTION_STATUS_REFUNDED);
                           generalDAO.saveTransactionDTO(item);
                            holder.imvRefund.setImageResource(R.drawable.ic_baseline_check_box_24);
                            holder.imvRefund.setClickable(false);
                            holder.imvRefund.setOnClickListener(null);
                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.create().show();
                }
            });
        }

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
