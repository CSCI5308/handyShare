package com.g02.handyShare.Lending.Repository;

import com.g02.handyShare.Lending.Entity.LentItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LentItemRepository extends JpaRepository<LentItem, Long> {
}

