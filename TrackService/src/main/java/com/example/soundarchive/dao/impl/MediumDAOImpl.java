package com.example.soundarchive.dao.impl;

import com.example.soundarchive.dao.MediumDAO;
import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.model.entity.MediumEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MediumDAOImpl implements MediumDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public MediumEntity findById(Integer id) {

        MediumEntity mediumEntity;

        try {
            mediumEntity = entityManager.find(MediumEntity.class, id);

            if(mediumEntity == null) throw new DataNotFoundException("Medium with id: " + id + " not found.");

        } catch(DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return mediumEntity;
    }

    @Override
    public MediumEntity findByName(String name) {//pitanje dal radi

        MediumEntity mediumEntity;

        try {
            TypedQuery<MediumEntity> query = entityManager.createQuery("FROM MediumEntity WHERE name = :name", MediumEntity.class);
            mediumEntity = query.getSingleResult();

            if(mediumEntity == null) throw new DataNotFoundException("Medium with name: " + name + " not found.");

        } catch(DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return mediumEntity;
    }

    @Override
    public List<MediumEntity> findAll() {

        List<MediumEntity> mediumEntities;

        try {

            TypedQuery<MediumEntity> query = entityManager.createQuery("FROM MediumEntity", MediumEntity.class);
            mediumEntities = query.getResultList();

            if(mediumEntities.isEmpty()) throw new DataNotFoundException("No mediums found.");

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return mediumEntities;
    }

    @Transactional
    @Override
    public MediumEntity save(MediumEntity mediumEntity) {

        try {

            entityManager.persist(mediumEntity);

        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return  mediumEntity;
    }

    @Transactional
    @Override
    public MediumEntity update(MediumEntity mediumEntity) {

        try {

            entityManager.merge(mediumEntity);

        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return  mediumEntity;

    }

    @Transactional
    @Override
    public void delete(Integer id) {

        try {

            MediumEntity mediumEntity = entityManager.find(MediumEntity.class, id);

            if (mediumEntity != null) {
                entityManager.remove(mediumEntity);
            }
            else {
                throw new DataNotFoundException("Medium with id: " + id + "not found");
            }

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

    }


}
