package com.example.soundarchive.service;

import com.example.soundarchive.dao.impl.UserDAOImpl;
import com.example.soundarchive.mapper.ObjectUtilMapper;
import com.example.soundarchive.mapper.UserMapper;
import com.example.soundarchive.model.UserEntity;
import com.example.soundarchive.model.dto.UpdateUserDTO;
import com.example.soundarchive.model.dto.UserDTO;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDAOImpl userDAO;

    private final ObjectUtilMapper mapper;

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserDAOImpl userDAO, ObjectUtilMapper mapper, UserMapper userMapper) {
        this.userDAO = userDAO;
        this.mapper = mapper;
        this.userMapper = userMapper;
    }

    public UserDTO findById(Integer id) {

        UserEntity userEntity = userDAO.findById(id);

        return mapper.map(userEntity, UserDTO.class);
    }

    public ListPagedResultDTO<UserDTO> findAll(Pageable pageable, String firstName, String lastName, String username) {

        ListPagedResultDTO<UserEntity> listPagedResultReturned = userDAO.findAll(pageable, firstName, lastName, username);

        return new ListPagedResultDTO<>(listPagedResultReturned.getTotal(),
                mapper.mapList(listPagedResultReturned.getList(), UserDTO.class));
    }

    public UserDTO save(UserDTO userDTO) {

        UserEntity userEntity = mapper.map(userDTO, UserEntity.class);

        userEntity = userDAO.save(userEntity);

        return mapper.map(userEntity, UserDTO.class);
    }

    public UserDTO update(UpdateUserDTO updateUserDTO, Integer id) {

        UserEntity userEntity = userDAO.findById(id);

        if(updateUserDTO.getActive() == null) updateUserDTO.setActive(true);

        userMapper.mapUpdate(userEntity, updateUserDTO);

        userEntity = userDAO.update(userEntity);

        return mapper.map(userEntity, UserDTO.class);
    }

    public void delete(Integer id) {

        userDAO.delete(id);
    }

    public void changePassword(String username, String encodedPassword) {

        userDAO.changePassword(username, encodedPassword);
    }

    public void changeUserPermission(String username, Integer permissionLevel) {

        userDAO.changeUserPermission(username, permissionLevel);
    }

    public UserDTO updateUserArtist(Integer userId, Integer artistId) {

        UserEntity userEntity = userDAO.updateUserArtist(userId, artistId);

        return mapper.map(userEntity, UserDTO.class);
    }
}
