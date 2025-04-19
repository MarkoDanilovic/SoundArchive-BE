package com.example.soundarchive.service;

import com.example.soundarchive.dao.impl.GenreDAOImpl;
import com.example.soundarchive.mapper.ObjectUtilMapper;
import com.example.soundarchive.model.dto.GenreDTO;
import com.example.soundarchive.model.entity.GenreEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreService {

    private final GenreDAOImpl genreDAO;

    private final ObjectUtilMapper mapper;

    @Autowired
    public GenreService(GenreDAOImpl genreDAO, ObjectUtilMapper mapper) {
        this.genreDAO = genreDAO;
        this.mapper = mapper;
    }

    public GenreDTO findById(Integer id) {

        GenreEntity genreEntity = genreDAO.findById(id);

        return mapper.map(genreEntity, GenreDTO.class);
    }

    public List<GenreDTO> findAll() {

        List<GenreEntity> genreEntities = genreDAO.findAll();

        return mapper.mapList(genreEntities, GenreDTO.class);
    }

    public GenreDTO save(GenreDTO genreDTO) {

        GenreEntity genreEntity = mapper.map(genreDTO, GenreEntity.class);

        genreEntity = genreDAO.save(genreEntity);

        return mapper.map(genreEntity, GenreDTO.class);
    }

    public GenreDTO update(GenreDTO genreDTO) {

        GenreEntity genreEntity = mapper.map(genreDTO, GenreEntity.class);

        genreEntity = genreDAO.update(genreEntity);

        return mapper.map(genreEntity, GenreDTO.class);
    }

    public void delete(Integer id) {

        genreDAO.delete(id);
    }
}
