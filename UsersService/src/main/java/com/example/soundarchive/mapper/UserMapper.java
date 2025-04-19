package com.example.soundarchive.mapper;

import com.example.soundarchive.model.UserEntity;
import com.example.soundarchive.model.dto.RegisterDTO;
import com.example.soundarchive.model.dto.UpdateUserDTO;
import com.example.soundarchive.model.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class UserMapper {

    public UserEntity mapUpdate(UserEntity userEntity, UpdateUserDTO updateUserDTO) {
        updateFieldIfNotNull(userEntity::setFirstName, updateUserDTO.getFirstName());
        updateFieldIfNotNull(userEntity::setLastName, updateUserDTO.getLastName());
        updateFieldIfNotNull(userEntity::setDateOfBirth, updateUserDTO.getDateOfBirth());
        updateFieldIfNotNull(userEntity::setEmail, updateUserDTO.getEmail());
        updateFieldIfNotNull(userEntity::setDisplayName, updateUserDTO.getDisplayName());
        updateFieldIfNotNull(userEntity::setSocialMediaLink, updateUserDTO.getSocialMediaLink());

        return userEntity;
    }

    private <T> void updateFieldIfNotNull(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }

    public UserDTO mapRegister(RegisterDTO registerDTO) {

        UserDTO userDTO = new UserDTO();

        userDTO.setId(registerDTO.getId());
        userDTO.setFirstName(registerDTO.getFirstName());
        userDTO.setLastName(registerDTO.getLastName());
        userDTO.setDateOfBirth(registerDTO.getDateOfBirth());
        userDTO.setEmail(registerDTO.getEmail());
        userDTO.setDisplayName(registerDTO.getDisplayName());
        userDTO.setDescription(registerDTO.getDescription());
        userDTO.setSocialMediaLink(registerDTO.getSocialMediaLink());
        userDTO.setPhoneNumber(registerDTO.getPhoneNumber());
        userDTO.setAddress(registerDTO.getAddress());
        userDTO.setCity(registerDTO.getCity());
        userDTO.setCountry(registerDTO.getCountry());
        userDTO.setCreditCardNumber(registerDTO.getCreditCardNumber());

        userDTO.setUsername(registerDTO.getUsername());
        userDTO.setPassword(registerDTO.getPassword1());
        userDTO.setPermissionLevel(registerDTO.getPermissionLevel());

        userDTO.setArtistId(registerDTO.getArtistId());

        return userDTO;
    }
}
