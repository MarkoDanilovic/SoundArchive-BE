package com.example.soundarchive.service;

import com.example.soundarchive.dao.impl.MediumDAOImpl;
import com.example.soundarchive.dao.impl.RecordDAOImpl;
import com.example.soundarchive.mapper.CreateRecordMapper;
import com.example.soundarchive.model.dto.CreateRecordDTO;
import com.example.soundarchive.model.entity.MediumEntity;
import com.example.soundarchive.model.entity.RecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {

    private final RecordDAOImpl recordDAO;

    private final MediumDAOImpl mediumDAO;

    private final CreateRecordMapper createRecordMapper;

    @Autowired
    public RecordService(RecordDAOImpl recordDAO, MediumDAOImpl mediumDAO, CreateRecordMapper createRecordMapper) {
        this.recordDAO = recordDAO;
        this.mediumDAO = mediumDAO;
        this.createRecordMapper = createRecordMapper;
    }

    public List<CreateRecordDTO> findByTrackId(Integer trackId) {

        List<RecordEntity> recordEntities = recordDAO.findByTrackId(trackId);

        List<MediumEntity> mediumEntities = mediumDAO.findAll();

        return createRecordMapper.mapDTOs(recordEntities, mediumEntities);
    }

    public CreateRecordDTO findByTrackAndMedium(Integer trackId, Integer mediumId) {

        RecordEntity recordEntity = recordDAO.findByTrackAndMedium(trackId, mediumId);

        MediumEntity mediumEntity = mediumDAO.findById(mediumId);

        return createRecordMapper.mapDTO(recordEntity, mediumEntity.getName());
    }

    public CreateRecordDTO save(CreateRecordDTO createRecordDTO) {

        //MediumEntity mediumEntity = mediumDAO.findByName(createRecordDTO.getMedium().getName());

        RecordEntity recordEntity = createRecordMapper.mapEntity(createRecordDTO);//, mediumEntity.getId());

        recordEntity = recordDAO.save(recordEntity);

        return createRecordMapper.mapDTO(recordEntity, createRecordDTO.getMedium().getName());//mediumEntity.getName());
    }

    public CreateRecordDTO update(CreateRecordDTO createRecordDTO) {

        //MediumEntity mediumEntity = mediumDAO.findByName(createRecordDTO.getMedium().getName());

        RecordEntity recordEntity = createRecordMapper.mapEntity(createRecordDTO);

        recordEntity = recordDAO.update(recordEntity);

        return createRecordMapper.mapDTO(recordEntity, createRecordDTO.getMedium().getName());//mediumEntity.getName());
    }

    public void deleteByTrackId(Integer trackId) {

        recordDAO.deleteByTrackId(trackId);
    }
}
