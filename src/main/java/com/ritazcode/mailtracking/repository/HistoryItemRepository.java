package com.ritazcode.mailtracking.repository;

import com.ritazcode.mailtracking.entity.HistoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {

    Page<HistoryItem> findByItemId(Long itemId, Pageable pageable);

}
