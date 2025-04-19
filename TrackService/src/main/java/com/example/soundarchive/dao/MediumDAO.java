package com.example.soundarchive.dao;

import com.example.soundarchive.model.entity.MediumEntity;

import java.util.List;

public interface MediumDAO {

    MediumEntity findById(Integer id);

    MediumEntity findByName(String name);

    List<MediumEntity> findAll();

    MediumEntity save(MediumEntity mediumEntity);

    MediumEntity update(MediumEntity mediumEntity);

    void delete(Integer id);
}
