package com.example.soundarchive.dao;

import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.entity.ArtistEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtistDAO {

    public ArtistEntity findById(Integer id);

    public List<ArtistEntity> findByName(String name);

    public ListPagedResultDTO<ArtistEntity> findAll(Pageable pageable, String name);

    public ArtistEntity save(ArtistEntity trackEntity);

    public ArtistEntity update(ArtistEntity trackEntity);

    public void delete(Integer id);
}
