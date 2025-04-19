package com.example.soundarchive.mapper;

import com.example.soundarchive.model.RecordId;
import com.example.soundarchive.model.dto.MediumDTO;
import com.example.soundarchive.model.dto.RecordDTO;
import com.example.soundarchive.model.entity.MediumEntity;
import com.example.soundarchive.model.entity.RecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class RecordMapper {

    private final ObjectUtilMapper mapper;

    @Autowired
    public RecordMapper(ObjectUtilMapper mapper) {
        this.mapper = mapper;
    }

    public RecordDTO mapDTO(RecordEntity recordEntity) {//, String mediumName) {

        RecordDTO recordDTO = mapper.map(recordEntity, RecordDTO.class);
        recordDTO.getMedium().setId(recordEntity.getMedium().getId());
        recordDTO.getMedium().setName(recordEntity.getMedium().getName());

        return recordDTO;
    }

    public RecordEntity mapEntity(RecordDTO recordDTO, Integer trackId, Integer mediumId) {

        RecordEntity recordEntity = mapper.map(recordDTO, RecordEntity.class);

        //recordEntity.getRecordId().setTrackId(trackId);
        //recordEntity.getRecordId().setMediumId(mediumId);
        recordEntity.setRecordId(new RecordId(trackId, mediumId));

        return recordEntity;
    }

    public List<RecordDTO> mapDTOs(List<RecordEntity> recordEntities) {//, List<MediumEntity> mediumEntities) {

        List<RecordDTO> recordDTOs = new ArrayList<>();

//        HashMap<Integer, MediumDTO> mediums = new HashMap<>();
//        for (MediumEntity mediumEntity : mediumEntities) {
//            mediums.put(mediumEntity.getId(),mapper.map(mediumEntity, MediumDTO.class));
//        }

        for (RecordEntity recordEntity : recordEntities) {

            RecordDTO recordDTO = mapper.map(recordEntity, RecordDTO.class);
            recordDTO.setMedium(mapper.map(recordEntity.getMedium(), MediumDTO.class));
            //recordDTO.setMedium(mediums.get(recordEntity.getMediumId()));
            //recordDTO.setMedium(mapper.map(recordEntity.getMedium(), MediumDTO.class));

            recordDTOs.add(recordDTO);
        }

        return recordDTOs;
    }

    public List<RecordEntity> mapEntities(List<RecordDTO> recordDTOs, Integer trackId) {

        List<RecordEntity> recordEntities = new ArrayList<>();

        for(RecordDTO recordDTO : recordDTOs) {

            RecordEntity recordEntity = mapper.map(recordDTO, RecordEntity.class);

            //recordEntity.setMediumId(recordDTO.getMedium().getId());
            //recordEntity.setTrackId(trackId);

            recordEntity.setRecordId(new RecordId(recordDTO.getMedium().getId(), trackId));

            recordEntities.add(recordEntity);
        }

        return recordEntities;
    }

//    public RecordDTO mapDTO(RecordEntity recordEntity, String mediumName) {
//
//        RecordDTO recordDTO = mapper.map(recordEntity, RecordDTO.class);
//        recordDTO.getMedium().setId(recordEntity.getMediumId());
//        recordDTO.getMedium().setName(mediumName);
//
//        return recordDTO;
//    }
//
//    public RecordEntity mapEntity(RecordDTO recordDTO, Integer trackId, Integer mediumId) {
//
//        RecordEntity recordEntity = mapper.map(recordDTO, RecordEntity.class);
//        recordEntity.setTrackId(trackId);
//        recordEntity.setMediumId(mediumId);
//
//        return recordEntity;
//    }
//
//    public List<RecordDTO> mapDTOs(List<RecordEntity> recordEntities, List<MediumEntity> mediumEntities) {
//
//        List<RecordDTO> recordDTOs = new ArrayList<>();
//
//        HashMap<Integer, MediumDTO> mediums = new HashMap<>();
//        for (MediumEntity mediumEntity : mediumEntities) {
//            mediums.put(mediumEntity.getId(),mapper.map(mediumEntity, MediumDTO.class));
//        }
//
//        for (RecordEntity recordEntity : recordEntities) {
//
//            RecordDTO recordDTO = mapper.map(recordEntity, RecordDTO.class);
//
//            recordDTO.setMedium(mediums.get(recordEntity.getMediumId()));
//
//            recordDTOs.add(recordDTO);
//        }
//
//        return recordDTOs;
//    }
//
//    public List<RecordEntity> mapEntities(List<RecordDTO> recordDTOs, Integer trackId) {
//
//        List<RecordEntity> recordEntities = new ArrayList<>();
//
//        for(RecordDTO recordDTO : recordDTOs) {
//
//            RecordEntity recordEntity = mapper.map(recordDTO, RecordEntity.class);
//
//            recordEntity.setMediumId(recordDTO.getMedium().getId());
//
//            recordEntity.setTrackId(trackId);
//
//            recordEntities.add(recordEntity);
//        }
//
//        return recordEntities;
//    }
}
