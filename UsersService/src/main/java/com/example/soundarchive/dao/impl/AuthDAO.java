package com.example.soundarchive.dao.impl;

import com.example.soundarchive.dao.IAuthDAO;
import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class AuthDAO implements IAuthDAO {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public UserEntity getUserbyUsername(String username) {

        try {
            TypedQuery<UserEntity> query = entityManager.createQuery("FROM UserEntity u WHERE u.username = :username", UserEntity.class);
            query.setParameter("username", username);
            UserEntity user = query.getSingleResult();


            if (user == null) {
                throw new DataNotFoundException("User not found");
            }

            return user;

        } catch (DataNotFoundException e){
            throw new DataNotFoundException(e.getMessage());
        } catch (NoResultException e){
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
