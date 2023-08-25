package com.ritazcode.mailtracking.service;

import com.ritazcode.mailtracking.dto.postalItem.PostalItemHistoryDto;
import com.ritazcode.mailtracking.dto.postalItem.RegisterItemDto;
import com.ritazcode.mailtracking.dto.postalItem.ResponseItemDto;
import com.ritazcode.mailtracking.dto.postalItem.UpdateItemDto;
import com.ritazcode.mailtracking.entity.HistoryItem;
import com.ritazcode.mailtracking.entity.ItemStatus;
import com.ritazcode.mailtracking.entity.PostOffice;
import com.ritazcode.mailtracking.entity.PostalItem;
import com.ritazcode.mailtracking.exception.BadAttributeValueException;
import com.ritazcode.mailtracking.exception.NoSuchElementException;
import com.ritazcode.mailtracking.mapper.PostalItemMapper;
import com.ritazcode.mailtracking.repository.PostOfficeRepository;
import com.ritazcode.mailtracking.repository.PostalItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostalItemService {

    private final PostalItemMapper itemMapper;
    private final PostalItemRepository itemRepository;
    private final PostOfficeRepository postOfficeRepository;
    private final HistoryItemService historyItemService;


    /**
     * get a paged list of postal items
     *
     * @param pageRequest contains page number and page size
     * @return paged list of postal items
     */
    public Page<ResponseItemDto> getPostalItems(PageRequest pageRequest) {
        return itemRepository.findAll(pageRequest).map(
                itemMapper::PostalItem_To_ResponseItemDto
        );
    }

    /**
     * get a specific postal item information
     *
     * @param id postal item id
     * @return postal item information
     */
    public ResponseItemDto view(Long id) {
        if (id == null)
            throw new BadAttributeValueException("item id is not valid");
        Optional<PostalItem> dbItem = itemRepository.findById(id);
        if (dbItem.isEmpty())
            throw new NoSuchElementException(String.format("item with id (%s) is not found", id));
        return itemMapper.PostalItem_To_ResponseItemDto(dbItem.get());
    }

    /**
     * get a paged list of postal items that are now in a specific post office
     *
     * @param pageRequest contains page number and page size
     * @return paged list of postal items of a specific office
     */
    public Page<ResponseItemDto> getOfficeItems(Long postOfficeId,PageRequest pageRequest) {
        if (postOfficeId == null)
            throw new BadAttributeValueException("post office id is not valid");
        Optional<PostOffice> office = postOfficeRepository.findById(postOfficeId);
        if (office.isEmpty())
            throw new NoSuchElementException(String.format("post office  with id (%s) is not found", postOfficeId));
        return itemRepository.findAllByPostOfficeId(postOfficeId, pageRequest).map(
                itemMapper::PostalItem_To_ResponseItemDto
        );
    }

    /**
     * register a new postal item
     *
     * @param newItem new item information
     * @return registered item information
     */
    public ResponseItemDto register(RegisterItemDto newItem) {
        if (newItem == null)
            throw new BadAttributeValueException("the passed value of postal item is not valid");
        PostalItem postalItem = itemRepository.save(itemMapper.RegisterItemDto_to_PostalItem(newItem, ItemStatus.REGISTERED));
        postalItem.getHistory().add(new HistoryItem(ItemStatus.REGISTERED, postalItem));
        itemRepository.save(postalItem);
        return itemMapper.PostalItem_To_ResponseItemDto(postalItem);
    }

    /**
     * update postal item status to arrived to post office
     *
     * @param updatedItem contains postal item id and post office id
     * @return updated postal item information
     */
    public ResponseItemDto arriveToPostOffice(UpdateItemDto updatedItem) {
        return updateItem(updatedItem, ItemStatus.ARRIVED);
    }

    /**
     * update postal item status to departed from post office
     *
     * @param updatedItem contains postal item id and post office id
     * @return updated postal item information
     */
    public ResponseItemDto departFromPostOffice(UpdateItemDto updatedItem) {
        return updateItem(updatedItem, ItemStatus.DEPARTED);
    }

    /**
     * update postal item status to received by recipient
     *
     * @param updatedItem contains postal item id and post office id
     * @return updated postal item information
     */
    public ResponseItemDto receiveByRecipient(UpdateItemDto updatedItem) {
        return updateItem(updatedItem, ItemStatus.RECEIVED);
    }

    /**
     * updates a specific postal item status to the new status that is passed
     *
     * @param updatedItem contains postal item id and post office id
     * @param status      new status that will be set to the postal item
     * @return updated postal item information
     */
    ResponseItemDto updateItem(UpdateItemDto updatedItem, ItemStatus status) {
        if (updatedItem.getId() == null)
            throw new BadAttributeValueException("item id is not valid");
        if (updatedItem.getPostOfficeId() == null)
            throw new BadAttributeValueException("post office id is not valid");
        //get postal item from db
        Optional<PostalItem> dbItem = itemRepository.findById(updatedItem.getId());
        if (dbItem.isEmpty())
            throw new NoSuchElementException(String.format("item with id (%s) is not found", updatedItem.getId()));
        //if item is already received, no further modifications can be done
        if (dbItem.get().getStatus() == ItemStatus.RECEIVED)
            throw new BadAttributeValueException(String.format("item with id (%s) has already been RECEIVED", updatedItem.getId()));
        //get post office from db

        Optional<PostOffice> postOffice = postOfficeRepository.findById(updatedItem.getPostOfficeId());
        if (postOffice.isEmpty())
            throw new NoSuchElementException(String.format("post office  with id (%s) is not found", updatedItem.getPostOfficeId()));

        //postal item can not depart from an office that is different from the one it arrived to
        //or in case it hasn't even arrived to this office (meaning its current status is not Arrived)
        if (status == ItemStatus.DEPARTED) {
            //if item has not arrived to post office
            if (dbItem.get().getPostOffice() == null)
                throw new BadAttributeValueException(String.format("item with id (%s) did NOT ARRIVE to post office yet", updatedItem.getId()));
            //if the item is departed from a different office (other that the one it arrived to)
            if ((!Objects.equals(dbItem.get().getPostOffice().getId(), postOffice.get().getId())))
                throw new BadAttributeValueException(String.format("item with id (%s) can not DEPART from a post office (%s) to which it did NOT ARRIVE", updatedItem.getId(), updatedItem.getPostOfficeId()));
        }
        //update item
        dbItem.get().setPostOffice(postOffice.get());
        dbItem.get().setStatus(status);
        dbItem.get().getHistory().add(new HistoryItem(status, dbItem.get()));

        //return updated item information
        return itemMapper.PostalItem_To_ResponseItemDto(
                itemRepository.save(dbItem.get())
        );
    }

    /**
     * get history of a specific postal item
     *
     * @param postalItemId id of postal item
     * @param pageRequest  contains page number and page size
     * @return paged list of history record of a postal item
     */
    public Page<PostalItemHistoryDto> getPostalItemHistory(Long postalItemId, PageRequest pageRequest) {
        if (postalItemId == null)
            throw new BadAttributeValueException("item id is not valid");
        if (!itemRepository.existsById(postalItemId))
            throw new NoSuchElementException(String.format("item with id (%s) is not found", postalItemId));
        return historyItemService.findByItemId(postalItemId, pageRequest);

    }


}
