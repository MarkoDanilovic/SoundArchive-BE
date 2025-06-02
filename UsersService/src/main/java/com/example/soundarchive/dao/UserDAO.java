package com.example.soundarchive.dao;

import com.example.soundarchive.model.UserEntity;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import org.springframework.data.domain.Pageable;

public interface UserDAO {

    UserEntity findById(Integer id);

    ListPagedResultDTO<UserEntity> findAll(Pageable pageable, String firstName, String lastName, String username);

    UserEntity save(UserEntity userEntity);

    UserEntity update(UserEntity userEntity);

    void delete(Integer id);

    void changePassword(String username, String encodedPassword);

    void changeUserPermission(String username, Integer permissionLevel);

    UserEntity updateUserArtist(Integer userId, Integer artistId);
}
