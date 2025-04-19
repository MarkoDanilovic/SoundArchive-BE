package com.example.soundarchive.service;

import com.example.soundarchive.dao.impl.*;
import com.example.soundarchive.mapper.ObjectUtilMapper;
import com.example.soundarchive.mapper.TrackMapper;
import com.example.soundarchive.model.dto.TrackDTO;
import com.example.soundarchive.model.dto.UpdateTrackDTO;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.entity.RecordEntity;
import com.example.soundarchive.model.entity.TrackEntity;
import com.example.soundarchive.model.entity.UpdateTrackEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {

    private final TrackDAOImpl trackDAO;

    private final TrackMapper trackMapper;

    private final ObjectUtilMapper mapper;

    @Autowired
    public TrackService(TrackDAOImpl trackDAO, TrackMapper trackMapper, ObjectUtilMapper mapper) {
        this.trackDAO = trackDAO;
        this.trackMapper = trackMapper;
        this.mapper = mapper;
    }

    public TrackDTO findById(Integer id) {

        TrackEntity trackEntity = trackDAO.findById(id);

        return trackMapper.mapDTO(trackEntity);
    }

    public List<TrackDTO> findByName(String name) {

        List<TrackEntity> trackEntities = trackDAO.findByName(name);

        return trackMapper.mapDTOs(trackEntities);
    }

    public ListPagedResultDTO<TrackDTO> findAll(Pageable pageable, String name, Integer genreId, Integer mediumId, String artist) {

        ListPagedResultDTO<TrackEntity> listPagedResultReturned = trackDAO.findAll(pageable, name, genreId, mediumId, artist);

        return new ListPagedResultDTO<>(listPagedResultReturned.getTotal(),
                trackMapper.mapDTOs(listPagedResultReturned.getList()));
    }

    public TrackDTO save(TrackDTO trackDTO) {

        TrackEntity trackEntity = trackMapper.mapEntity(trackDTO);

        for (RecordEntity recordEntity : trackEntity.getRecords()) {

            recordEntity.setTrack(trackEntity);
        }

        trackEntity = trackDAO.save(trackEntity);

        return trackMapper.mapDTO(trackEntity);
    }

    public TrackDTO update(UpdateTrackDTO updateTrackDTO, Integer id) {

        UpdateTrackEntity updateTrackEntity = mapper.map(updateTrackDTO, UpdateTrackEntity.class);
        updateTrackEntity.setId(id);

        TrackEntity trackEntity = trackDAO.update(updateTrackEntity);

        return trackMapper.mapDTO(trackEntity);

    }

    public void delete(Integer id) {

        trackDAO.delete(id);
    }
}
