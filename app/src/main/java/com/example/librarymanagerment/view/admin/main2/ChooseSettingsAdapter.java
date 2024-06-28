package com.example.librarymanagerment.view.admin.main2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.dto.SettingDTO;

import java.util.List;

public class ChooseSettingsAdapter extends BaseAdapter {

    List<SettingDTO> items;
    Context context;

    public ChooseSettingsAdapter(List<SettingDTO> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.item_setting, parent, false);
        SettingDTO item = (SettingDTO) getItem(position);
        TextView tvName = rootView.findViewById(R.id.tv_settings_name);
        ImageView imvIcon = rootView.findViewById(R.id.imvIconSetting);
        tvName.setText(item.getName());
        imvIcon.setImageResource(item.getIdIcon());
        return rootView;
    }
}
