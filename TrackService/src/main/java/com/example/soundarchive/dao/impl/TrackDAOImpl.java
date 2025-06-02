package com.example.soundarchive.dao.impl;

import com.example.soundarchive.dao.TrackDAO;
import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.entity.TrackEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

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
            throw new InternalServerErrorException("Error in TrackDAOImpl, method findById. " + e.getMessage());
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
            throw new InternalServerErrorException("Error in TrackDAOImpl, method findByName. " + e.getMessage());
        }

        return trackEntities;
    }

    @Override
    public ListPagedResultDTO<TrackEntity> findAll(Pageable pageable, String name, Integer genreId, Integer mediumId, String artistName, Integer artistId) {

        int total;
        List<TrackEntity> trackEntities;

        try {
            StringBuilder countQueryStr = new StringBuilder("SELECT COUNT(DISTINCT t) FROM TrackEntity t");

            if (genreId != null) {
                countQueryStr.append(" INNER JOIN t.genre g");
            }
            if ((artistName != null && !artistName.isEmpty()) || artistId != null) {
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
            if (artistName != null && !artistName.isEmpty()) {
                countQueryStr.append(" AND lower(a.artistName) LIKE lower(concat('%', :artistName, '%'))");
            }
            if (mediumId != null) {
                countQueryStr.append(" AND m.id = :mediumId");
            }
            if (artistId != null) {
                countQueryStr.append(" AND a.id = :artistId");
            }

            TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr.toString(), Long.class);

            if (name != null && !name.isEmpty()) countQuery.setParameter("name", name);
            if (genreId != null) countQuery.setParameter("genreId", genreId);
            if (artistName != null && !artistName.isEmpty()) countQuery.setParameter("artistName", artistName);
            if (mediumId != null) countQuery.setParameter("mediumId", mediumId);
            if (artistId != null) countQuery.setParameter("artistId", artistId);

            total = countQuery.getSingleResult().intValue();



            StringBuilder queryStr = new StringBuilder("SELECT DISTINCT t FROM TrackEntity t");

            if (genreId != null) {
                queryStr.append(" INNER JOIN t.genre g");
            }
            if ((artistName != null && !artistName.isEmpty()) || artistId != null) {
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
            if (artistName != null && !artistName.isEmpty()) {
                queryStr.append(" AND lower(a.artistName) LIKE lower(concat('%', :artistName, '%'))");
            }
            if (mediumId != null) {
                queryStr.append(" AND m.id = :mediumId");
            }
            if (artistId != null) {
                queryStr.append(" AND a.id = :artistId");
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
            if (artistName != null && !artistName.isEmpty()) query.setParameter("artistName", artistName);
            if (mediumId != null) query.setParameter("mediumId", mediumId);
            if (artistId != null) query.setParameter("artistId", artistId);

            query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());

            trackEntities = query.getResultList();

            if (trackEntities.isEmpty()) throw new DataNotFoundException("No tracks found.");

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error in TrackDAOImpl, method findAll. " + e.getMessage());
        }

        return new ListPagedResultDTO<>(total, trackEntities);
    }

    @Transactional
    @Override
    public TrackEntity save(TrackEntity trackEntity) {

        try {
            TrackEntity savedEntity = entityManager.merge(trackEntity);

            entityManager.flush();

            return savedEntity;

        } catch (Exception e) {
            throw new InternalServerErrorException("Error in TrackDAOImpl, method save. " + e.getMessage());
        }
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
    public TrackEntity update(TrackEntity trackEntity) {

        try {
            entityManager.merge(trackEntity);

            entityManager.flush();

            return entityManager.find(TrackEntity.class, trackEntity.getId());

        } catch (Exception e) {
            throw new InternalServerErrorException("Error in TrackDAOImpl, method update. " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void delete(TrackEntity trackEntity) {

        try {
            if (trackEntity != null) {
                entityManager.remove(trackEntity);
            }
            else {
                throw new DataNotFoundException("Track not found.");
            }

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error in TrackDAOImpl, method delete. " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public TrackEntity updatePicture(Integer id, String relativePath) {

        TrackEntity trackEntity;

        try {

            trackEntity = entityManager.find(TrackEntity.class, id);

            if(trackEntity == null) throw new DataNotFoundException("Track with id: " + id + " not found.");

            trackEntity.setPicture(relativePath);

            entityManager.merge(trackEntity);

            entityManager.flush();

        } catch(DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error in TrackDAOImpl, method updatePicture. " + e.getMessage());
        }

        return trackEntity;
    }
}
