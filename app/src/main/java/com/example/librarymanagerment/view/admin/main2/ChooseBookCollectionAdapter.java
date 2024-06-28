package com.example.librarymanagerment.view.admin.main2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.librarymanagerment.R;
import com.example.librarymanagerment.core.dto.BookCollectionDTO;

import java.util.List;

public class ChooseBookCollectionAdapter extends ArrayAdapter<BookCollectionDTO> {

    public ChooseBookCollectionAdapter(@NonNull Context context, @NonNull List<BookCollectionDTO> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_book_collection, parent, false);
        }
        TextView txtName = convertView.findViewById(R.id.tv_item_book_collection_name);
        BookCollectionDTO item = getItem(position);
        convertView.setTag(item);

        txtName.setText(item.getName());
        return convertView;
    }
}
