package com.ritazcode.mailtracking.service;

import com.ritazcode.mailtracking.dto.postOffice.CreatePostOfficeDto;
import com.ritazcode.mailtracking.dto.postOffice.ResponsePostOfficeDto;
import com.ritazcode.mailtracking.dto.postOffice.UpdatePostOfficeDto;
import com.ritazcode.mailtracking.entity.PostOffice;
import com.ritazcode.mailtracking.exception.BadAttributeValueException;
import com.ritazcode.mailtracking.exception.NoSuchElementException;
import com.ritazcode.mailtracking.mapper.PostOfficeMapper;
import com.ritazcode.mailtracking.repository.PostOfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostOfficeService {
    private final PostOfficeRepository repository;
    private final PostOfficeMapper mapper;

    /**
     * get post office information from db
     *
     * @param id id of post office
     * @return post office information
     */
    public ResponsePostOfficeDto view(Long id) {
        if (id == null)
            throw new BadAttributeValueException("post office id is not valid");
        Optional<PostOffice> dbOffice = repository.findById(id);
        if (dbOffice.isEmpty())
            throw new NoSuchElementException(String.format("No such post office with id (%s)", id));
        return mapper.DbPostOffice_To_ResponsePostOfficeDto(dbOffice.get());
    }

    /**
     * update a specific post office
     *
     * @param updatedOfficeDto updated post office
     * @return updated post office information
     */
    public ResponsePostOfficeDto update(UpdatePostOfficeDto updatedOfficeDto) {
        if (updatedOfficeDto == null || updatedOfficeDto.getId() == null)
            throw new BadAttributeValueException("the passed post office values or post office id is not valid");
        Optional<PostOffice> dbPost = repository.findById(updatedOfficeDto.getId());
        if (dbPost.isEmpty())
            throw new NoSuchElementException(String.format("No such post office with id (%s)", updatedOfficeDto.getId()));

        PostOffice updatedOffice = repository.save(
                mapper.UpdatePostOfficeDto_To_DbPostOffice(updatedOfficeDto,
                        dbPost.get()));
        return mapper.DbPostOffice_To_ResponsePostOfficeDto(updatedOffice);
    }

    /**
     * create a new post office by user
     *
     * @param newDto new post office information
     * @return created post office information
     */
    public ResponsePostOfficeDto create(CreatePostOfficeDto newDto) {
        if (newDto == null)
            throw new BadAttributeValueException("the passed value of post office is not valid");

        return mapper.DbPostOffice_To_ResponsePostOfficeDto(
                repository.save(
                        mapper.CreatePostOfficeDto_To_DbPostOffice(newDto)
                ));
    }

    /**
     * delete a specific post office by its id
     *
     * @param id id of post office
     */
    public void delete(Long id) {
        if (id == null)
            throw new BadAttributeValueException(String.format("the passed id (%s) is not valid", id));
        Optional<PostOffice> dbPost = repository.findById(id);
        if (dbPost.isEmpty())
            throw new NoSuchElementException(String.format("No such post office with id (%s)", id));
        repository.deleteById(id);
    }
}
