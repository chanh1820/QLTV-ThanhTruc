package com.example.librarymanagerment.view.admin.danhsachphieumuon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.dto.TransactionStatusDTO;

import java.util.List;

public class SPNChooseTransactionStatusAdapter extends BaseAdapter {
    Context context;
    List<TransactionStatusDTO> items;

    public SPNChooseTransactionStatusAdapter(Context context, List<TransactionStatusDTO> items) {
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
        TransactionStatusDTO item = items.get(i);
        tvContent.setText(item.getName());
        rootView.setTag(item);

        return rootView;
    }
}
