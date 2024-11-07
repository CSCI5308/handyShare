package com.g02.handyShare.Lending.Service;

import com.g02.handyShare.Lending.Entity.LentItem;
import com.g02.handyShare.Lending.Repository.LentItemRepository;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LentItemServiceImpl implements LentItemService {

    @Autowired
    private LentItemRepository lentItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<LentItem> getAllLentItems() {
        return lentItemRepository.findAll();
    }

    @Override
    public LentItem addLentItem(LentItem lentItem) {
        return lentItemRepository.save(lentItem);
    }

    @Override
    public LentItem updateLentItem(Long id, LentItem lentItemDetails) {
        LentItem lentItem = lentItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found"));

        // Ensure the item belongs to the user before updating
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        if (!lentItem.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized to update this item");
        }

        // Update fields
        lentItem.setName(lentItemDetails.getName());
        lentItem.setDescription(lentItemDetails.getDescription());
        lentItem.setPrice(lentItemDetails.getPrice());
        lentItem.setCategory(lentItemDetails.getCategory());
        lentItem.setCity(lentItemDetails.getCity());
        lentItem.setState(lentItemDetails.getState());
        lentItem.setPincode(lentItemDetails.getPincode());
        lentItem.setAddress(lentItemDetails.getAddress());
        lentItem.setImageName(lentItemDetails.getImageName());
        lentItem.setAvailability(lentItemDetails.getAvailability());

        return lentItemRepository.save(lentItem);
    }

    @Override
    public void deleteLentItem(Long id) {
        LentItem lentItem = lentItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found"));

        // Ensure the item belongs to the user before deleting
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        if (!lentItem.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized to delete this item");
        }

        lentItemRepository.delete(lentItem);
    }

    @Override
    public List<LentItem> getLentItemsByUser(User user) {
        return lentItemRepository.findByUser(user);
    }
}
