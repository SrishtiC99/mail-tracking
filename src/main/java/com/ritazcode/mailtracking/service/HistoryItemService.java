package com.ritazcode.mailtracking.service;

import com.ritazcode.mailtracking.dto.postalItem.PostalItemHistoryDto;
import com.ritazcode.mailtracking.entity.PostalItem;
import com.ritazcode.mailtracking.exception.BadAttributeValueException;
import com.ritazcode.mailtracking.exception.NoSuchElementException;
import com.ritazcode.mailtracking.mapper.HistoryItemMapper;
import com.ritazcode.mailtracking.repository.HistoryItemRepository;
import com.ritazcode.mailtracking.repository.PostalItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryItemService {
    private final HistoryItemRepository repository;
    private final HistoryItemMapper mapper;
    private final PostalItemRepository postalItemRepository;

    Page<PostalItemHistoryDto> findByItemId(Long itemId, PageRequest pageRequest) {
        if (itemId == null)
            throw new BadAttributeValueException("postal item id is not valid");
        Optional<PostalItem> item = postalItemRepository.findById(itemId);
        if (item.isEmpty())
            throw new NoSuchElementException(String.format("postal item  with id (%s) is not found", itemId));
        return repository.findByItemId(itemId, pageRequest).map(mapper::HistoryItem_To_PostalItemHistoryDto);
    }

}
