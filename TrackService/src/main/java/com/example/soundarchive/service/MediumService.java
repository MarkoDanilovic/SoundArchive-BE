package com.example.soundarchive.service;

import com.example.soundarchive.dao.impl.MediumDAOImpl;
import com.example.soundarchive.mapper.ObjectUtilMapper;
import com.example.soundarchive.model.dto.MediumDTO;
import com.example.soundarchive.model.entity.MediumEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediumService {

    private final MediumDAOImpl mediumDAO;

    private final ObjectUtilMapper mapper;

    @Autowired
    public MediumService(MediumDAOImpl mediumDAO, ObjectUtilMapper mapper) {
        this.mediumDAO = mediumDAO;
        this.mapper = mapper;
    }

    public MediumDTO findById(Integer id) {

        MediumEntity mediumEntity = mediumDAO.findById(id);

        return mapper.map(mediumEntity, MediumDTO.class);
    }

    public List<MediumDTO> findAll() {

        List<MediumEntity> mediumEntities = mediumDAO.findAll();

        return mapper.mapList(mediumEntities, MediumDTO.class);
    }

    public MediumDTO save(MediumDTO mediumDTO) {

        MediumEntity mediumEntity = mapper.map(mediumDTO, MediumEntity.class);

        mediumEntity = mediumDAO.save(mediumEntity);

        return mapper.map(mediumEntity, MediumDTO.class);
    }

    public MediumDTO update(MediumDTO mediumDTO) {

        MediumEntity mediumEntity = mapper.map(mediumDTO, MediumEntity.class);

        mediumEntity = mediumDAO.update(mediumEntity);

        return mapper.map(mediumEntity, MediumDTO.class);
    }

    public void delete(Integer id) {

        mediumDAO.delete(id);
    }
}
