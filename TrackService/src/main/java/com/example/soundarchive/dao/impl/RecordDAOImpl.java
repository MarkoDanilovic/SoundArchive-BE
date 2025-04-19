package com.example.soundarchive.dao.impl;

import com.example.soundarchive.dao.RecordDAO;
import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.model.entity.RecordEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecordDAOImpl implements RecordDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<RecordEntity> findByTrackId(Integer trackId) {

        List<RecordEntity> recordEntities;

        try {
            TypedQuery<RecordEntity> query = entityManager.createQuery("FROM RecordEntity r WHERE r.recordId.trackId = :trackId", RecordEntity.class);
            query.setParameter("trackId", trackId);
            recordEntities = query.getResultList();

            if (recordEntities.isEmpty()) {
                throw new DataNotFoundException("Records for track with id: " + trackId + " not found.");
            }

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return recordEntities;
    }

    @Override
    public RecordEntity findByTrackAndMedium(Integer trackId, Integer mediumId) {

        RecordEntity recordEntity;

        try {
            TypedQuery<RecordEntity> query = entityManager.createQuery("FROM RecordEntity r WHERE r.recordId.trackId = :trackId AND r.medium.id = :mediumId", RecordEntity.class);
            query.setParameter("trackId", trackId);
            query.setParameter("mediumId", mediumId);
            recordEntity = query.getSingleResult();

            if (recordEntity == null) {
                throw new DataNotFoundException("Record with trackId: " + trackId + " and mediumId: " + mediumId + " not found.");
            }

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return recordEntity;
    }

    @Transactional
    @Override
    public RecordEntity save(RecordEntity recordEntity) {

        try {
            entityManager.merge(recordEntity);

        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return recordEntity;
    }

    @Transactional
    @Override
    public RecordEntity update(RecordEntity recordEntity) {

        try {
            entityManager.merge(recordEntity);

        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return recordEntity;
    }

    @Transactional
    @Override
    public void deleteByTrackId(Integer trackId) {

        try {
            int deletedCount = entityManager.createQuery("DELETE FROM RecordEntity r WHERE r.recordId.trackId = :trackId")
                    .setParameter("trackId", trackId)
                    .executeUpdate();

            if (deletedCount == 0) {
                throw new DataNotFoundException("Records for track with id: " + trackId + " not found.");
            }
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }
    }
}
