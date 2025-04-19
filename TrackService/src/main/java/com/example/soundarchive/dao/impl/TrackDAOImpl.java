package com.example.soundarchive.dao.impl;

import com.example.soundarchive.dao.TrackDAO;
import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.entity.TrackEntity;
import com.example.soundarchive.model.entity.UpdateTrackEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TrackDAOImpl implements TrackDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TrackEntity findById(Integer id) {

        TrackEntity trackEntity;

        try {

            trackEntity = entityManager.find(TrackEntity.class, id);

            if(trackEntity == null) throw new DataNotFoundException("Track with id: " + id + " not found.");

        } catch(DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return trackEntity;
    }

    @Override
    public List<TrackEntity> findByName(String name) {

        List<TrackEntity> trackEntities;

//        if(name.equals(":name")) {
//            trackEntities = findAll();
//
//            return trackEntities;
//        }

        try {
            String hql = "FROM TrackEntity WHERE lower(name) LIKE lower(concat('%', :name, '%'))";
            TypedQuery<TrackEntity> query = entityManager.createQuery(hql, TrackEntity.class);
            query.setParameter("name", name);
            trackEntities = query.getResultList();

        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return trackEntities;
    }

    @Override
    public ListPagedResultDTO<TrackEntity> findAll(Pageable pageable, String name, Integer genreId, Integer mediumId, String artist) {

        int total;
        List<TrackEntity> trackEntities;

        try {
            StringBuilder countQueryStr = new StringBuilder("SELECT COUNT(DISTINCT t) FROM TrackEntity t");

            if (genreId != null) {
                countQueryStr.append(" INNER JOIN t.genre g");
            }
            if (artist != null && !artist.isEmpty()) {
                countQueryStr.append(" INNER JOIN t.artist a");
            }
            if (mediumId != null) {
                countQueryStr.append(" INNER JOIN t.records r INNER JOIN r.medium m");
            }

            countQueryStr.append(" WHERE 1=1");

            if (genreId != null) {
                countQueryStr.append(" AND g.id = :genreId");
            }
            if (name != null && !name.isEmpty()) {
                countQueryStr.append(" AND lower(t.name) LIKE lower(concat('%', :name, '%'))");
            }
            if (artist != null && !artist.isEmpty()) {
                countQueryStr.append(" AND lower(a.artistName) LIKE lower(concat('%', :artist, '%'))");
            }
            if (mediumId != null) {
                countQueryStr.append(" AND m.id = :mediumId");
            }

            TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr.toString(), Long.class);

            if (name != null && !name.isEmpty()) countQuery.setParameter("name", name);
            if (genreId != null) countQuery.setParameter("genreId", genreId);
            if (artist != null && !artist.isEmpty()) countQuery.setParameter("artist", artist);
            if (mediumId != null) countQuery.setParameter("mediumId", mediumId);

            total = countQuery.getSingleResult().intValue();



            StringBuilder queryStr = new StringBuilder("SELECT DISTINCT t FROM TrackEntity t");

            if (genreId != null) {
                queryStr.append(" INNER JOIN t.genre g");
            }
            if (artist != null && !artist.isEmpty()) {
                queryStr.append(" INNER JOIN t.artist a");
            }
            if (mediumId != null) {
                queryStr.append(" INNER JOIN t.records r INNER JOIN r.medium m");
            }

            queryStr.append(" WHERE 1=1");

            if (genreId != null) {
                queryStr.append(" AND g.id = :genreId");
            }
            if (name != null && !name.isEmpty()) {
                queryStr.append(" AND lower(t.name) LIKE lower(concat('%', :name, '%'))");
            }
            if (artist != null && !artist.isEmpty()) {
                queryStr.append(" AND lower(a.artistName) LIKE lower(concat('%', :artist, '%'))");
            }
            if (mediumId != null) {
                queryStr.append(" AND m.id = :mediumId");
            }

            if (pageable.getSort().isSorted()) {
                for (Sort.Order order : pageable.getSort()) {
                    if (!order.getProperty().equals("name") && !order.getProperty().equals("genre")) {
                        throw new InternalServerErrorException("Available sort value is either name or genre, " +
                                "TrackDAOImpl, findAll method.");
                    }
                    queryStr.append(" ORDER BY t.").append(order.getProperty()).append(" ").append(order.getDirection().name());
                }
            }

            TypedQuery<TrackEntity> query = entityManager.createQuery(queryStr.toString(), TrackEntity.class);

            if (name != null && !name.isEmpty()) query.setParameter("name", name);
            if (genreId != null) query.setParameter("genreId", genreId);
            if (artist != null && !artist.isEmpty()) query.setParameter("artist", artist);
            if (mediumId != null) query.setParameter("mediumId", mediumId);

            query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());

            trackEntities = query.getResultList();

            if (trackEntities.isEmpty()) throw new DataNotFoundException("No tracks found.");

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return new ListPagedResultDTO<>(total, trackEntities);
    }

    @Transactional
    @Override
    public TrackEntity save(TrackEntity trackEntity) {

        try {
            trackEntity = entityManager.merge(trackEntity);

        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        return trackEntity;
    }

//    @Transactional
//    @Override
//    public TrackEntity update(TrackEntity trackEntity) {
//
//        try {
//            //entityManager.merge(trackEntity);
//            //entityManager.flush();
//
//            entityManager.joinTransaction();
//            entityManager.merge(trackEntity);
//            entityManager.flush();
//            entityManager.clear();
//            entityManager.close();
//
//        } catch (Exception e) {
//            throw new InternalServerErrorException("Greska " + e.getMessage());
//        }
//
//        return entityManager.find(TrackEntity.class, trackEntity.getId());
//    }

    @Transactional
    @Override
    public TrackEntity update(UpdateTrackEntity updateTrackEntity) {

        try {
            entityManager.merge(updateTrackEntity);

            entityManager.flush();

        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }

        TrackEntity trackEntity = entityManager.find(TrackEntity.class, updateTrackEntity.getId());

        return trackEntity;
    }

    @Transactional
    @Override
    public void delete(Integer id) {

        try {

            TrackEntity trackEntity = entityManager.find(TrackEntity.class, id);

            if (trackEntity != null) {
                entityManager.remove(trackEntity);
            }
            else {
                throw new DataNotFoundException("Track with id: " + id + " not found.");
            }

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Greska " + e.getMessage());
        }
    }
}
