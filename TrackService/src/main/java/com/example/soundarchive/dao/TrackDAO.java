package com.example.soundarchive.dao;

import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.entity.TrackEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrackDAO {

    public TrackEntity findById(Integer id);

    public List<TrackEntity> findByName(String name);

    public ListPagedResultDTO<TrackEntity> findAll(Pageable pageable, String name, Integer genreId, Integer mediumId, String artistName, Integer artistId);

    public TrackEntity save(TrackEntity trackEntity);

    public TrackEntity update(TrackEntity updateTrackEntity);

    public void delete(TrackEntity id);

    TrackEntity updatePicture(Integer id, String relativePath);
}
