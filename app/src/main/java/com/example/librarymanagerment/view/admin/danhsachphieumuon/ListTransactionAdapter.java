package com.example.librarymanagerment.view.admin.danhsachphieumuon;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.constants.GoogleSheetConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.ResponseDTO;
import com.example.librarymanagerment.core.dto.StudentDTO;
import com.example.librarymanagerment.core.dto.TransactionDTO;
import com.example.librarymanagerment.core.utils.ObjectMapperUtils;
import com.example.librarymanagerment.core.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        holder.tvListBook.setText(StringUtils.genBooksByListBook(item.getListBookDTO()));
        holder.tvStudentInfo.setText(item.getStudentName() + " | " + item.getClassRoom());
        holder.tvTimeline.setText(item.getFromDate().substring(0,10) + " - " + item.getToDate().substring(0,10));

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
//                           generalDAO.saveTransactionDTO(item);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, GoogleSheetConstants.END_POINT_URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    ResponseDTO responseDTO = ObjectMapperUtils.stringToDTO(response, ResponseDTO.class);
                                    if(responseDTO.getStatusCode().equals(GoogleSheetConstants.STATUS_SUCCESS)){
                                        holder.imvRefund.setImageResource(R.drawable.ic_baseline_check_box_24);
                                        holder.imvRefund.setClickable(false);
                                        holder.imvRefund.setOnClickListener(null);
                                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                                        StudentDTO studentDTO = new StudentDTO();
                                        studentDTO.setId(item.getStudentId());
                                        studentDTO.setStudentName(item.getStudentName());
                                        studentDTO.setClassRoom(item.getClassRoom());
                                        studentDTO.setBorrowedFlag(DBConstants.STATUS_NONE_BORROWED);
                                        generalDAO.updateQuantityBook(item.getListBookDTO(), DBConstants.OPERATOR_INCREASE);
                                        generalDAO.updateStudentDTO(studentDTO);
                                    }else {
                                        Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
//                                    btnSearchTransaction.setClickable(true);
//                                    btnSearchTransaction.setEnabled(true);
                                    Toast.makeText(context, "Bạn chưa bật kết nối Internet ?", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("action", GoogleSheetConstants.ACTION_UPDATE_STATUS_TRANSACTION_BY_ID);
                                    params.put("id", item.getId().toString());
                                    Log.e("id",item.getId().toString() );
                                    params.put("status", DBConstants.TRANSACTION_STATUS_REFUNDED.toString());
                                    return params;
                                }
                            };

                            RetryPolicy retryPolicy = new DefaultRetryPolicy(
                                    50000,
                                    0,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            stringRequest.setRetryPolicy(retryPolicy);
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);

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
