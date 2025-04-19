package com.example.soundarchive.dao;

import com.example.soundarchive.model.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthDAO {
    UserEntity getUserbyUsername(String username);
}
