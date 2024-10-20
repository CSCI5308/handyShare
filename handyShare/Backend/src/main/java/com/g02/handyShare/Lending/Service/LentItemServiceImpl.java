package com.g02.handyShare.Lending.Service;

import com.g02.handyShare.Lending.Entity.LentItem;
import com.g02.handyShare.Lending.Repository.LentItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LentItemServiceImpl implements LentItemService {

    @Autowired
    private LentItemRepository lentItemRepository;

    @Override
    public List<LentItem> getAllLentItems() {
        return lentItemRepository.findAll();
    }

    @Override
    public LentItem addLentItem(LentItem lentItem) {
        return lentItemRepository.save(lentItem);
    }
}
