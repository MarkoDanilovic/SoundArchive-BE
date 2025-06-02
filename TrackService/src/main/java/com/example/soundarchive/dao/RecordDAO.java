package com.example.soundarchive.dao;

import com.example.soundarchive.model.dto.QuantityDTO;
import com.example.soundarchive.model.entity.RecordEntity;

import java.util.List;

public interface RecordDAO {

    List<RecordEntity> findByTrackId(Integer trackId);

    RecordEntity findByTrackAndMedium(Integer trackId, Integer mediumId);

    RecordEntity save(RecordEntity recordEntity);

    RecordEntity update(RecordEntity recordEntity);

    void deleteByTrackId(Integer trackId);

    void updateQuantity(QuantityDTO quantityDTO);
}
