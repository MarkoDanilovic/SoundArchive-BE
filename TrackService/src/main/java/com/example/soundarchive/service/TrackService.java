package com.example.soundarchive.service;

import com.example.soundarchive.dao.impl.*;
import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.mapper.ObjectUtilMapper;
import com.example.soundarchive.mapper.TrackMapper;
import com.example.soundarchive.model.dto.TrackDTO;
import com.example.soundarchive.model.dto.UpdateTrackDTO;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.entity.MediumEntity;
import com.example.soundarchive.model.entity.RecordEntity;
import com.example.soundarchive.model.entity.TrackEntity;
import com.example.soundarchive.model.entity.UpdateTrackEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrackService {

    private final TrackDAOImpl trackDAO;

    private final TrackMapper trackMapper;

    private final ObjectUtilMapper mapper;

    private final ImageUploadService imageUploadService;

    @Autowired
    public TrackService(TrackDAOImpl trackDAO, TrackMapper trackMapper, ObjectUtilMapper mapper, ImageUploadService imageUploadService) {
        this.trackDAO = trackDAO;
        this.trackMapper = trackMapper;
        this.mapper = mapper;
        this.imageUploadService = imageUploadService;
    }

    public TrackDTO findById(Integer id) {

        TrackEntity trackEntity = trackDAO.findById(id);

        return trackMapper.mapDTO(trackEntity);
    }

    public List<TrackDTO> findByName(String name) {

        List<TrackEntity> trackEntities = trackDAO.findByName(name);

        return trackMapper.mapDTOs(trackEntities);
    }

    public ListPagedResultDTO<TrackDTO> findAll(Pageable pageable, String name, Integer genreId, Integer mediumId, String artistName, Integer artistId) {

        ListPagedResultDTO<TrackEntity> listPagedResultReturned = trackDAO.findAll(pageable, name, genreId, mediumId, artistName, artistId);

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

        TrackEntity trackEntity = trackDAO.findById(id);

        trackMapper.mapUpdate(trackEntity, updateTrackDTO);

        trackEntity = trackDAO.update(trackEntity);

        return trackMapper.mapDTO(trackEntity);

    }

    @Transactional
    public void delete(Integer id) {

        try {

            TrackEntity trackEntity = trackDAO.findById(id);

            if (trackEntity.getPicture() != null && !trackEntity.getPicture().isEmpty()) {
                imageUploadService.deleteImage(trackEntity.getPicture());
            }

            trackDAO.delete(trackEntity);

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public TrackDTO updatePicture(Integer id, String relativePath) {

        TrackEntity trackEntity = trackDAO.updatePicture(id, relativePath);

        return trackMapper.mapDTO(trackEntity);
    }
}
