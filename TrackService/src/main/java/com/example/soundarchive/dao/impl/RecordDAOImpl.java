package com.example.soundarchive.dao.impl;

import com.example.soundarchive.dao.RecordDAO;
import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.model.dto.QuantityDTO;
import com.example.soundarchive.model.entity.RecordEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

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

    @Transactional
    @Override
    public void updateQuantity(QuantityDTO quantityDTO) {

        try {
            TypedQuery<RecordEntity> query = entityManager.createQuery("FROM RecordEntity r WHERE r.recordId.trackId = :trackId AND r.medium.id = :mediumId", RecordEntity.class);
            query.setParameter("trackId", quantityDTO.getTrackId());
            query.setParameter("mediumId", quantityDTO.getMediumId());
            RecordEntity recordEntity = query.getSingleResult();

            if (recordEntity == null) {
                throw new DataNotFoundException("Record with trackId: " + quantityDTO.getTrackId() + " and mediumId: " + quantityDTO.getMediumId() + " not found.");
            }

            if(Objects.equals(quantityDTO.getAction(), "add")) {
                recordEntity.setQuantity(recordEntity.getQuantity() + quantityDTO.getQuantity());
            } else if (Objects.equals(quantityDTO.getAction(), "remove")) {
                if((recordEntity.getQuantity() - quantityDTO.getQuantity()) >= 0) {
                    recordEntity.setQuantity(recordEntity.getQuantity() - quantityDTO.getQuantity());
                } else {
                    recordEntity.setQuantity(0);
                }
            } else {
                throw new BadRequestException("Error while calling updateQuantity, action must be either add or remove!");
            }

            entityManager.merge(recordEntity);

        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error while calling updateQuantity! " + e.getMessage());
        }
    }
}
