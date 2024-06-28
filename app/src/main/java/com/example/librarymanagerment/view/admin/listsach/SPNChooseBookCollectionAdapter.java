package com.example.librarymanagerment.view.admin.listsach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.dto.BookCollectionDTO;

import java.util.List;

public class SPNChooseBookCollectionAdapter extends BaseAdapter {
    Context context;
    List<BookCollectionDTO> items;

    public SPNChooseBookCollectionAdapter(Context context, List<BookCollectionDTO> items) {
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
                .inflate(R.layout.item_spinner, viewGroup, false);
        TextView tvContent = rootView.findViewById(R.id.tv_spn_content_1);
        tvContent.setText(items.get(i).getName());
        rootView.setTag(items.get(i));
        return rootView;
    }
}