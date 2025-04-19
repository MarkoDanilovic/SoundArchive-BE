package com.example.soundarchive.dao;

import com.example.soundarchive.model.UserEntity;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserDAO {

    UserEntity findById(Integer id);

    ListPagedResultDTO<UserEntity> findAll(Pageable pageable, String firstName, String lastName, String displayName);

    UserEntity save(UserEntity userEntity);

    UserEntity update(UserEntity userEntity);

    void delete(Integer id);
}
