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

import java.util.List;

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

    public ListPagedResultDTO<UserDTO> findAll(Pageable pageable, String firstName, String lastName, String displayName) {

        ListPagedResultDTO<UserEntity> listPagedResultReturned = userDAO.findAll(pageable, firstName, lastName, displayName);

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

        userMapper.mapUpdate(userEntity, updateUserDTO);

        userEntity = userDAO.update(userEntity);

        return mapper.map(userEntity, UserDTO.class);
    }

    public void delete(Integer id) {

        userDAO.delete(id);
    }
}
