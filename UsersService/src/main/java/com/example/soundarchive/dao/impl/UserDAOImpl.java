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
    public ListPagedResultDTO<UserEntity> findAll(Pageable pageable, String firstName, String lastName, String username) {

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
            if (username != null) {
                countQueryStr.append(" AND lower(a.username) LIKE lower(concat('%', :username, '%'))");
            }

            TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr.toString(), Long.class);

            if (firstName != null) {
                countQuery.setParameter("firstName", firstName);
            }
            if (lastName != null) {
                countQuery.setParameter("lastName", lastName);
            }
            if (username != null) {
                countQuery.setParameter("username", username);
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
            if (username != null && !username.isEmpty()) {
                queryStr.append(" AND lower(a.username) LIKE lower(concat('%', :username, '%'))");
            }

            if (pageable.getSort().isSorted()) {
                for (Sort.Order order : pageable.getSort()) {
                    if (!order.getProperty().equals("firstName") && !order.getProperty().equals("lastName") && !order.getProperty().equals("username")) {
                        throw new InternalServerErrorException("Available sort value is only firstName, lastName or username. " +
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
            if (username != null) {
                query.setParameter("username", username);
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

            entityManager.flush();

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

    @Transactional
    @Override
    public void changePassword(String username, String encodedPassword) {

        try {
            TypedQuery<UserEntity> query = entityManager.createQuery("FROM UserEntity u WHERE u.username = :username", UserEntity.class);
            query.setParameter("username", username);
            UserEntity user = query.getSingleResult();

            if (user == null) {
                throw new DataNotFoundException("User not found");
            }

            user.setPassword(encodedPassword);

            entityManager.merge(user);

            entityManager.flush();

        } catch (DataNotFoundException e){
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error in UserService, changePassword " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void changeUserPermission(String username, Integer permissionLevel) {

        try {
            TypedQuery<UserEntity> query = entityManager.createQuery("FROM UserEntity u WHERE u.username = :username", UserEntity.class);
            query.setParameter("username", username);
            UserEntity user = query.getSingleResult();


            if (user == null) {
                throw new DataNotFoundException("User not found");
            }

            user.setPermissionLevel(permissionLevel);

            entityManager.merge(user);

            entityManager.flush();

        } catch (DataNotFoundException e){
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error in UserService, giveArtistPermission " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public UserEntity updateUserArtist(Integer userId, Integer artistId) {

        try {
            UserEntity userEntity = entityManager.find(UserEntity.class, userId);

            if(userEntity == null) throw new DataNotFoundException("User with id: " + userId + " not found.");

            userEntity.setArtistId(artistId);

            entityManager.merge(userEntity);

            entityManager.flush();

            return userEntity;

        } catch (DataNotFoundException e){
            throw new DataNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error in UserService, updateUserArtist " + e.getMessage());
        }
    }
}
