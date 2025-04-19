package com.example.soundarchive.dao.impl;

import com.example.soundarchive.dao.UserDAO;
import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.model.UserEntity;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserEntity findById(Integer id) {

        UserEntity userEntity;

        try {

            userEntity = entityManager.find(UserEntity.class, id);

            if(userEntity == null) throw new DataNotFoundException("User with id: " + id + " not found.");

        } catch(DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error in UserService, findById " + e.getMessage());
        }

        return userEntity;
    }

    @Override
    public ListPagedResultDTO<UserEntity> findAll(Pageable pageable, String firstName, String lastName, String displayName) {

        int total;
        List<UserEntity> userEntities;

        try {
            StringBuilder countQueryStr = new StringBuilder("SELECT COUNT(a) FROM UserEntity a");
            countQueryStr.append(" WHERE 1=1");
            if (firstName != null) {
                countQueryStr.append(" AND lower(a.firstName) LIKE lower(concat('%', :firstName, '%'))");
            }
            if (lastName != null) {
                countQueryStr.append(" AND lower(a.lastName) LIKE lower(concat('%', :lastName, '%'))");
            }
            if (displayName != null) {
                countQueryStr.append(" AND lower(a.displayName) LIKE lower(concat('%', :displayName, '%'))");
            }

            TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr.toString(), Long.class);

            if (firstName != null) {
                countQuery.setParameter("firstName", firstName);
            }
            if (lastName != null) {
                countQuery.setParameter("lastName", lastName);
            }
            if (displayName != null) {
                countQuery.setParameter("displayName", displayName);
            }

            total = countQuery.getSingleResult().intValue();


            StringBuilder queryStr = new StringBuilder("SELECT a FROM UserEntity a");
            queryStr.append(" WHERE 1=1");
            if (firstName != null && !firstName.isEmpty()) {
                queryStr.append(" AND lower(a.firstName) LIKE lower(concat('%', :firstName, '%'))");
            }
            if (lastName != null && !lastName.isEmpty()) {
                queryStr.append(" AND lower(a.lastName) LIKE lower(concat('%', :lastName, '%'))");
            }
            if (displayName != null && !displayName.isEmpty()) {
                queryStr.append(" AND lower(a.displayName) LIKE lower(concat('%', :displayName, '%'))");
            }

            if (pageable.getSort().isSorted()) {
                for (Sort.Order order : pageable.getSort()) {
                    if (!order.getProperty().equals("firstName") && !order.getProperty().equals("lastName") && !order.getProperty().equals("displayName")) {
                        throw new InternalServerErrorException("Available sort value is only firstName, lastName or displayName. " +
                                "ArtistDAOImpl, findAll method.");
                    }
                    queryStr.append(" ORDER BY a.").append(order.getProperty()).append(" ").append(order.getDirection().name());
                }
            }

            TypedQuery<UserEntity> query = entityManager.createQuery(queryStr.toString(), UserEntity.class);

            if (firstName != null) {
                query.setParameter("firstName", firstName);
            }
            if (lastName != null) {
                query.setParameter("lastName", lastName);
            }
            if (displayName != null) {
                query.setParameter("displayName", displayName);
            }

            query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
            query.setMaxResults(pageable.getPageSize());

            userEntities = query.getResultList();


            if(userEntities.isEmpty()) throw new DataNotFoundException("No users found.");

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error in UserService, findAll " + e.getMessage());
        }

        return new ListPagedResultDTO<>(total, userEntities);
    }

    @Transactional
    @Override
    public UserEntity save(UserEntity userEntity) {

        try {
            entityManager.persist(userEntity);

        } catch (Exception e) {
            throw new InternalServerErrorException("Error in UserService, save " + e.getMessage());
        }

        return userEntity;
    }

    @Transactional
    @Override
    public UserEntity update(UserEntity userEntity) {

        try {
            entityManager.merge(userEntity);

        } catch (Exception e) {
            throw new InternalServerErrorException("Error in UserService, update " + e.getMessage());
        }

        return userEntity;
    }

    @Transactional
    @Override
    public void delete(Integer id) {

        try {

            UserEntity userEntity = entityManager.find(UserEntity.class, id);

            if (userEntity != null) {
                entityManager.remove(userEntity);
            }
            else {
                throw new DataNotFoundException("User with id: " + id + " not found.");
            }

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        }catch (Exception e) {
            throw new InternalServerErrorException("Error in UserService, delete " + e.getMessage());
        }
    }
}
