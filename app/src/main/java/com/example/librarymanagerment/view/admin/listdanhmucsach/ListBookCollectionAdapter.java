package com.example.librarymanagerment.view.admin.listdanhmucsach;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.app.AlertDialog;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.BookCollectionDTO;

import java.util.List;

public class ListBookCollectionAdapter extends RecyclerView.Adapter<ListBookCollectionAdapter.MyViewHolder> {

    Context context;
    List<BookCollectionDTO> items;
    GeneralDAO generalDAO;

    public ListBookCollectionAdapter(Context context, List<BookCollectionDTO> items, GeneralDAO generalDAO) {
        this.context = context;
        this.items = items;
        this.generalDAO = generalDAO;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgItemBookCollectionDelete, imgItemBookCollectionEdit;
        TextView tvItemBookCollectionCode, tvItemBookCollectionName, tvItemBookCollectionDescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemBookCollectionDelete = itemView.findViewById(R.id.img_item_book_collection_delete);
            imgItemBookCollectionEdit = itemView.findViewById(R.id.img_item_book_collection_edit);
            tvItemBookCollectionCode = itemView.findViewById(R.id.tv_item_book_collection_code);
            tvItemBookCollectionName = itemView.findViewById(R.id.tv_item_book_collection_name);
//            tvItemBookCollectionDescription = itemView.findViewById(R.id.tv_item_book_collection_description);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_collection_book, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookCollectionDTO item = items.get(position);
        holder.tvItemBookCollectionCode.setText("Mã : " + item.getCode());
        holder.tvItemBookCollectionName.setText("Tên : " + item.getName());
//        holder.tvItemBookCollectionDescription.setText("Mô tả: " + item.getDescription());

        holder.imgItemBookCollectionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ListBookCollectionActivity.flagAdmin.equals(DBConstants.IS_ADMIN)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.dialog_save_book_collection, null);
                    builder.setView(dialogView);

                    TextView tvCode = dialogView.findViewById(R.id.edt_add_book_collection_code);
                    TextView tvName = dialogView.findViewById(R.id.edt_add_book_collection_name);
                    TextView tvDescription = dialogView.findViewById(R.id.edt_add_book_collection_description);

                    tvCode.setText(item.getCode());
                    tvName.setText(item.getName());
                    tvDescription.setText(item.getDescription());

                    builder.setTitle("Cập Nhật Loại Sách");
                    builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BookCollectionDTO bookCollectionDTO = new BookCollectionDTO();
                            bookCollectionDTO.setCode(tvCode.getText().toString().trim());
                            bookCollectionDTO.setName(tvName.getText().toString().trim());
                            bookCollectionDTO.setDescription(tvDescription.getText().toString().trim());
                            bookCollectionDTO.setId(item.getId());
                            generalDAO.saveBookCollectionDTO(bookCollectionDTO);
                            onUpdateItemList();
                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.create().show();

                } else {
                    Toast.makeText(context, "Bạn không có quyền", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imgItemBookCollectionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ListBookCollectionActivity.flagAdmin.equals(DBConstants.IS_ADMIN)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Xóa " + item.getCode());
                    builder.setIcon(R.drawable.ic_dele);
                    builder.setMessage("Bạn có muốn xóa không?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            generalDAO.deleteBookCollectionDTO(item.getId());
                            onUpdateItemList();
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();

                } else {
                    Toast.makeText(context, "Bạn không có quyền", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void onUpdateItemList() {
        items = generalDAO.findAllBookCollection();
        notifyDataSetChanged();
    }
}
