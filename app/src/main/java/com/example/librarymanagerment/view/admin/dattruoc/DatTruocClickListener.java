package com.example.librarymanagerment.view.admin.dattruoc;

import com.example.librarymanagerment.core.dto.BookDTO;
import com.example.librarymanagerment.core.dto.OrderDTO;
import com.example.librarymanagerment.core.dto.OrderPickedDTO;

public interface DatTruocClickListener {
    void onItemClick(OrderDTO item);

    void onButtonPickClick(OrderDTO item);

    void onButtonRejectClick(OrderDTO item);
}
