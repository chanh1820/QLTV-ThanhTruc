package com.example.librarymanagerment.view.user.muonsach.sach;

import com.example.librarymanagerment.core.dto.BookCollectionParentDTO;
import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.dto.OrderPickedDTO;

public interface OnItemClickListener {
    void onItemClick(Object item);

    void onItemClickIncreaseButton(OrderPickedDTO item);

    void onItemClickDecreaseButton(OrderPickedDTO item);


}
