package com.g02.handyShare.Lending.Service;

import com.g02.handyShare.Lending.Entity.LentItem;
import java.util.List;

public interface LentItemService {
    List<LentItem> getAllLentItems();
    LentItem addLentItem(LentItem lentItem);
}
