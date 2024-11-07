package com.g02.handyShare.Lending.Repository;

import com.g02.handyShare.Lending.Entity.LentItem;
import com.g02.handyShare.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LentItemRepository extends JpaRepository<LentItem, Long> {
    List<LentItem> findByUser(User user);
}

