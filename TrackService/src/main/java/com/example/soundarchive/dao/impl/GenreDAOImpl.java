package com.example.soundarchive.dao.impl;

import com.example.soundarchive.dao.GenreDAO;
import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.model.entity.GenreEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GenreDAOImpl implements GenreDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public GenreEntity findById(Integer id) {

        GenreEntity genreEntity;

        try {

            genreEntity = entityManager.find(GenreEntity.class, id);

            if(genreEntity == null) throw new DataNotFoundException("Genre with id: " + id + " not found.");

        } catch(DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return genreEntity;
    }

    @Override
    public GenreEntity findByName(String name) {

        GenreEntity genreEntity;

        try {
            TypedQuery<GenreEntity> query = entityManager.createQuery("FROM GenreEntity WHERE name = :name", GenreEntity.class);
            genreEntity = query.getSingleResult();

            if(genreEntity == null) throw new DataNotFoundException("Genre with name: " + name + " not found.");

        } catch(DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return genreEntity;
    }

    @Override
    public List<GenreEntity> findAll() {

        List<GenreEntity> genreEntities;

        try {
            TypedQuery<GenreEntity> query = entityManager.createQuery("FROM GenreEntity", GenreEntity.class);
            genreEntities = query.getResultList();

            if(genreEntities.isEmpty()) throw new DataNotFoundException("No genres found.");

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return genreEntities;
    }

    @Transactional
    @Override
    public GenreEntity save(GenreEntity genreEntity) {

        try {
            entityManager.persist(genreEntity);

        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return genreEntity;
    }

    @Transactional
    @Override
    public GenreEntity update(GenreEntity genreEntity) {

        try {
            entityManager.merge(genreEntity);

        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return genreEntity;
    }

    @Transactional
    @Override
    public void delete(Integer id) {

        try {
            GenreEntity genreEntity = entityManager.find(GenreEntity.class, id);

            if (genreEntity != null) {
                entityManager.remove(genreEntity);
            }
            else {
                throw new DataNotFoundException("Genre with id: " + id + " not found.");
            }

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }
    }


}
