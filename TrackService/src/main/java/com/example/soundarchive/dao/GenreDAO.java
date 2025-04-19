package com.example.soundarchive.dao;

import com.example.soundarchive.model.entity.GenreEntity;

import java.util.List;

public interface GenreDAO {

    GenreEntity findById(Integer id);

    GenreEntity findByName(String name);

    List<GenreEntity> findAll();

    GenreEntity save(GenreEntity genreEntity);

    GenreEntity update(GenreEntity genreEntity);

    void delete(Integer id);
}
