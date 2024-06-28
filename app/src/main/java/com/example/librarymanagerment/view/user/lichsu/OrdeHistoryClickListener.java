package com.example.librarymanagerment.view.user.lichsu;

import com.example.librarymanagerment.core.dto.OrderDTO;

public interface OrdeHistoryClickListener {
    void onItemClick(OrderDTO item);

    void onButtonPickClick(OrderDTO item);

    void onButtonRejectClick(OrderDTO item);
}
