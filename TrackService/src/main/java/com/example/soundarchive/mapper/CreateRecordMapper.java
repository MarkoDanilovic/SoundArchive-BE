package com.example.soundarchive.mapper;

import com.example.soundarchive.model.dto.CreateRecordDTO;
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
public class CreateRecordMapper {

    private final ObjectUtilMapper mapper;

    @Autowired
    public CreateRecordMapper(ObjectUtilMapper mapper) {
        this.mapper = mapper;
    }

    public CreateRecordDTO mapDTO(RecordEntity recordEntity, String mediumName) {

        CreateRecordDTO recordDTO = mapper.map(recordEntity, CreateRecordDTO.class);
        //recordDTO.getMedium().setId(recordEntity.getMediumId());
        recordDTO.getMedium().setName(mediumName);

        return recordDTO;
    }

    public RecordEntity mapEntity(CreateRecordDTO recordDTO) {//Integer mediumId?

        RecordEntity recordEntity = mapper.map(recordDTO, RecordEntity.class);
        //recordEntity.setMediumId(recordDTO.getMedium().getId());

        return recordEntity;
    }

    public List<CreateRecordDTO> mapDTOs(List<RecordEntity> recordEntities, List<MediumEntity> mediumEntities) {

        List<CreateRecordDTO> createRecordDTOs = new ArrayList<>();

        HashMap<Integer, MediumDTO> mediums = new HashMap<>();
        for (MediumEntity mediumEntity : mediumEntities) {
            mediums.put(mediumEntity.getId(),mapper.map(mediumEntity, MediumDTO.class));
        }

        for (RecordEntity recordEntity : recordEntities) {

            CreateRecordDTO createRecordDTO = mapper.map(recordEntity, CreateRecordDTO.class);

            //createRecordDTO.setMedium(mediums.get(recordEntity.getMediumId()));

            createRecordDTOs.add(createRecordDTO);
        }

        return createRecordDTOs;
    }
}
