package com.example.librarymanagerment.view.admin.phienmuonsach;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.dto.BookDTO;

import java.util.List;

public class SuggestBookAdapter extends BaseAdapter {
    private Context context;
    private List<BookDTO> items;

    public SuggestBookAdapter(Context context, List<BookDTO> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.item_book_suggest, viewGroup, false);

        BookDTO item = items.get(i);
        TextView tvName = rootView.findViewById(R.id.tv_item_suggest_book_name);
        TextView tvInfo = rootView.findViewById(R.id.tv_item_suggest_book_info);
        ImageView imgAddBook = rootView.findViewById(R.id.img_item_suggest_add_book);
        tvName.setText(item.getName());
        tvInfo.setText("Số lượng: " + item.getQuantity() + " | " + "Vị trí: " + item.getLocate());
        imgAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("listBookNew.isEmpty()", String.valueOf(AddTransactionActivity.listBookPicked.isEmpty()));
                if (AddTransactionActivity.listBookPicked.isEmpty()) {
                    item.setQuantity(1);
                    AddTransactionActivity.listBookPicked.add(item);
                    Log.e("1", "1");
                } else {
                    boolean flag = true;
                    for (int index = 0; index < AddTransactionActivity.listBookPicked.size(); index++) {
                        if (AddTransactionActivity.listBookPicked.get(index).getId().toString().equals(item.getId().toString())) {
                            AddTransactionActivity.listBookPicked.get(index).setQuantity(AddTransactionActivity.listBookPicked.get(index).getQuantity() + 1);
                            Log.e("2", "2");
                            flag = false;
                            break;
                        }
                    }

                    if (flag) {
                        item.setQuantity(1);
                        AddTransactionActivity.listBookPicked.add(item);
                    }

                }

//                AddTransactionActivity.Log();
                AddTransactionActivity.pickedBookAdapter.notifyDataSetChanged();

            }
        });
        return rootView;
    }
}
