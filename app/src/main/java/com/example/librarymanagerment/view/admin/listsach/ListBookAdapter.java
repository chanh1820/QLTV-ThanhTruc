package com.example.librarymanagerment.view.admin.listsach;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.constants.DBConstants;
import com.example.librarymanagerment.core.dao.GeneralDAO;
import com.example.librarymanagerment.core.dto.BookCollectionDTO;
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.utils.AssetUtils;
import com.example.librarymanagerment.core.utils.StringUtils;
import com.example.librarymanagerment.view.admin.main2.Main2Activity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListBookAdapter extends RecyclerView.Adapter<ListBookAdapter.MyViewHolder> {

    ImageView imvAddImageData;
    Context context;
    List<BookDTO> items;
    GeneralDAO generalDAO;

    public ListBookAdapter(Context context, List<BookDTO> items, GeneralDAO generalDAO) {
        this.context = context;
        this.items = items;
        this.generalDAO = generalDAO;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgItemBookDelete, imgItemBookEdit;
        TextView tvItemBookCode, tvItemBookName, tvItemBookDescription, tvItemBookLocate, tvItemBookQuantity, tvItemBookCollectionName;
        ImageView imvImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemBookDelete = itemView.findViewById(R.id.img_item_book_delete);
            imgItemBookEdit = itemView.findViewById(R.id.img_item_book_edit);

            imvImage = itemView.findViewById(R.id.tv_item_book_image);
            tvItemBookCode = itemView.findViewById(R.id.tv_item_book_code);
            tvItemBookName = itemView.findViewById(R.id.tv_item_book_name);
            tvItemBookCollectionName = itemView.findViewById(R.id.tv_item_book_book_collection_code);
            tvItemBookLocate = itemView.findViewById(R.id.tv_item_book_locate);
            tvItemBookQuantity = itemView.findViewById(R.id.tv_item_book_quantity);
//            tvItemBookDescription = itemView.findViewById(R.id.tv_item_book_description);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_book, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookDTO item = items.get(position);

        holder.tvItemBookCode.setText("Mã : " + item.getCode());
        holder.tvItemBookName.setText("Tên : " + item.getName());
        holder.tvItemBookCollectionName
                .setText("Danh mục: " + Main2Activity.mapListBookCollectionDTO.get(item.getCollectionCode()));
        holder.tvItemBookLocate.setText("Vị trí: " + item.getLocate());
        holder.tvItemBookQuantity.setText("Số lượng: " + item.getQuantity());
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
        holder.imgItemBookEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ListBookActivity.flagAdmin.equals(DBConstants.IS_ADMIN)) {


                    BookDTO bookDTO = new BookDTO();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.dialog_save_book, null);
                    builder.setView(dialogView);

                    ImageView imvAddBook = (ImageView) dialogView.findViewById(R.id.imv_save_book_add_image);
                    EditText edtCode = (EditText) dialogView.findViewById(R.id.edt_save_book_code);
                    EditText edtName = (EditText) dialogView.findViewById(R.id.edt_save_book_name);
                    Spinner spnCollectionName = (Spinner) dialogView.findViewById(R.id.spn_save_book_collection_name);
                    EditText edtLocate = (EditText) dialogView.findViewById(R.id.edt_save_book_locate);
                    EditText edtQuantity = (EditText) dialogView.findViewById(R.id.edt_save_book_quantity);
                    edtCode.setText(item.getCode());
                    edtName.setText(item.getName());
                    edtLocate.setText(item.getLocate());
                    edtQuantity.setText(item.getQuantity().toString());


                    List<BookCollectionDTO> bookCollectionDTOList = Main2Activity.listBookCollectionDTO;
                    Log.e("bookCollectionDTOList", bookCollectionDTOList.size() + "");
                    SPNChooseBookCollectionAdapter spnChooseBookCollectionAdapter
                            = new SPNChooseBookCollectionAdapter(context, bookCollectionDTOList);
                    Map<String, Integer> mapSectionSpinner = new HashMap<>();
                    for (int i = 0; i < bookCollectionDTOList.size(); i++) {
                        mapSectionSpinner.put(bookCollectionDTOList.get(i).getCode(), i);
                    }
                    mapSectionSpinner.put("", -1);
                    mapSectionSpinner.put(null, -1);
                    spnCollectionName.setSelection(mapSectionSpinner.get(item.getCollectionCode()));
                    spnCollectionName.setAdapter(spnChooseBookCollectionAdapter);
                    spnCollectionName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            bookDTO.setCollectionCode(Main2Activity.listBookCollectionDTO.get(i).getCode());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            bookDTO.setCollectionCode(Main2Activity.listBookCollectionDTO.get(0).getCode());
                        }
                    });
                    imvAddBook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imvAddImageData = imvAddBook;

                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            ((Activity) context).startActivityForResult(intent, DBConstants.REQUEST_BY_ADAPTER);
                        }
                    });
                    builder.setTitle("Cập Nhật Sách");
                    builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bookDTO.setId(item.getId());
                            bookDTO.setCode(edtCode.getText().toString().trim());
                            bookDTO.setName(edtName.getText().toString().trim());
                            bookDTO.setQuantity(Integer.parseInt(edtQuantity.getText().toString()));
                            bookDTO.setLocate(edtLocate.getText().toString().trim());
                            bookDTO.setCollectionCode(bookDTO.getCollectionCode());
                            bookDTO.setImageBase64(StringUtils.BitMapToString(((BitmapDrawable) imvAddImageData.getDrawable()).getBitmap()));

                            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                            try {
                                String json = ow.writeValueAsString(bookDTO);
                                Log.e("json", json);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Integer id = generalDAO.saveBookDTO(bookDTO);
                            Log.e("id", id + "");

                            if (id > 0) {
                                Toast.makeText(context, "Cập nhật Sách Thành Công", Toast.LENGTH_SHORT).show();
                                onUpdateItemList();
                            } else {
                                Toast.makeText(context, "Mã Sách Trùng Lặp \n Thêm Thất Bại", Toast.LENGTH_SHORT).show();
                            }
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

        holder.imgItemBookDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ListBookActivity.flagAdmin.equals(DBConstants.IS_ADMIN)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Xóa " + item.getCode());
                    builder.setIcon(R.drawable.ic_dele);
                    builder.setMessage("Bạn có muốn xóa không?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            generalDAO.deleteBookDTO(item.getId());
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
        items = generalDAO.findAllBook();
        notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imvAddImageData.setImageURI(selectedImage);
        }
    }
}
