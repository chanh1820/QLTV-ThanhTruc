package com.example.librarymanagerment.view.admin.danhsachtaikhoan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.dto.ClassRoomDTO;

import java.util.List;

public class SPNChooseClassroomAdapter extends BaseAdapter {
    Context context;
    List<ClassRoomDTO> items;

    public SPNChooseClassroomAdapter(Context context, List<ClassRoomDTO> items) {
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
        ClassRoomDTO item = items.get(i);
        tvContent.setText(item.getClassRoom());
        rootView.setTag(item);

        return rootView;
    }
}
