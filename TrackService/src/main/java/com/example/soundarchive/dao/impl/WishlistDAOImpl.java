package com.example.soundarchive.dao.impl;

import com.example.soundarchive.dao.WishlistDAO;
import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.model.WishlistId;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.entity.TrackEntity;
import com.example.soundarchive.model.entity.WishlistEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class WishlistDAOImpl implements WishlistDAO {

//    @Value("${spring.datasource.hikari.schema}")
//    private String schemaName;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ListPagedResultDTO<TrackEntity> findByUserId(Pageable pageable, Integer userId) {

        int total;
        List<TrackEntity> trackEntities;

        try {
            String sortBy = "";
            String orderDirection = "";
            if (pageable.getSort().isSorted()) {
                for (Sort.Order order : pageable.getSort()) {
                    if (!order.getProperty().equals("trackId")) {
                        throw new InternalServerErrorException("Available sort value is only trackId, " +
                                "WishlistDAOImpl, findByUserId method.");
                    }
                    orderDirection = order.getDirection().name();
                    sortBy = order.getProperty();
                }
            }

            StringBuilder countQueryStr = new StringBuilder("SELECT COUNT(w) FROM WishlistEntity w ")
                    .append("INNER JOIN w.trackEntity t ")
                    .append("ON w.id.trackId = t.id ")
                    .append("WHERE w.id.userId = :userId ");

            TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr.toString(), Long.class);

            if (userId != null) {
                countQuery.setParameter("userId", userId);
            }

            total = countQuery.getSingleResult().intValue();



            StringBuilder queryStr = new StringBuilder("SELECT t FROM WishlistEntity w ")
                    .append("INNER JOIN w.trackEntity t ")
                    .append("ON w.id.trackId = t.id ")
                    .append("WHERE w.id.userId = :userId ")
                    .append("ORDER BY t.id ").append(orderDirection).append(" ");

            TypedQuery<TrackEntity> query = entityManager.createQuery(queryStr.toString(), TrackEntity.class);

            if (userId != null) {
                query.setParameter("userId", userId);
            }

            query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());

            trackEntities = query.getResultList();

            if(total == 0 || trackEntities.isEmpty()) throw new DataNotFoundException("There are no tracks in the wishlist for the user with id: " + userId);

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch(DataAccessException e) {
            throw new InternalServerErrorException("DataAccessException occurred in WishlistDAOImpl, " +
                    "findByUserId method: " + e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error in WishlistDAOImpl, method findByUserId. " + e.getMessage());
        }

        return new ListPagedResultDTO<>(total, trackEntities);
    }

    @Transactional
    @Override
    public WishlistEntity save(WishlistEntity wishlistEntity) {

        try {
            TrackEntity trackEntity = entityManager.find(TrackEntity.class, wishlistEntity.getId().getTrackId());

            if(trackEntity == null) throw new DataNotFoundException("Track with id: " + wishlistEntity.getId().getTrackId() + " not found.");

            wishlistEntity.setTrackEntity(trackEntity);

            entityManager.persist(wishlistEntity);

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error in WishlistDAOImpl, method save. " + e.getMessage());
        }

        return wishlistEntity;
    }

    @Transactional
    @Override
    public void delete(WishlistId wishlistId) {

        try {
            WishlistEntity wishlistEntity = entityManager.find(WishlistEntity.class, wishlistId);

            if(wishlistEntity == null) throw new DataNotFoundException("There is no track with trackId: " + wishlistId.getTrackId() +
                    " in the wishlist of the user with userId: " + wishlistId.getUserId());

            entityManager.remove(wishlistEntity);

        } catch (Exception e) {
            throw new InternalServerErrorException("Error in WishlistDAOImpl, method delete. " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deleteByUserId(Integer userId) {

        try {
            String queryStr = "SELECT w FROM WishlistEntity w WHERE w.id.userId = :userId";
            TypedQuery<WishlistEntity> query = entityManager.createQuery(queryStr, WishlistEntity.class);
            query.setParameter("userId", userId);

            List<WishlistEntity> wishlistEntities = query.getResultList();

            if (wishlistEntities.isEmpty()) {
                throw new DataNotFoundException("No wishlist entries found for user with id: " + userId);
            }

            for (WishlistEntity wishlistEntity : wishlistEntities) {
                entityManager.remove(wishlistEntity);
            }

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error in WishlistDAOImpl, method delete. " + e.getMessage());
        }
    }
}
