package com.g02.handyShare.Lending.Service;

import java.util.List;

import com.g02.handyShare.Lending.Entity.LentItem;
import com.g02.handyShare.User.Entity.User;

public interface LentItemService {
    List<LentItem> getAllLentItems();
    LentItem addLentItem(LentItem lentItem);
    LentItem updateLentItem(Long id, LentItem lentItemDetails);
    void deleteLentItem(Long id);
    List<LentItem> getLentItemsByUser(User user);
}
