package com.example.soundarchive.dao;

import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.entity.TrackEntity;
import com.example.soundarchive.model.entity.UpdateTrackEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrackDAO {

    public TrackEntity findById(Integer id);

    public List<TrackEntity> findByName(String name);

    public ListPagedResultDTO<TrackEntity> findAll(Pageable pageable, String name, Integer genreId, Integer mediumId, String artist);

    public TrackEntity save(TrackEntity trackEntity);

    public TrackEntity update(UpdateTrackEntity updateTrackEntity);

    public void delete(Integer id);
}
