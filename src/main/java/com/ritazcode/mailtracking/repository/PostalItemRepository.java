package com.ritazcode.mailtracking.repository;

import com.ritazcode.mailtracking.entity.PostalItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostalItemRepository extends JpaRepository<PostalItem, Long> {
    Page<PostalItem> findAll(Pageable pageable);
    Page<PostalItem> findAllByPostOfficeId(Long officeId,Pageable pageable);
}
