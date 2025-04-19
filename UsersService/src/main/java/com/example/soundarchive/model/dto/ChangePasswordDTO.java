package com.example.soundarchive.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {

    private String username;
    private String oldPassword1;
    private String oldPassword2;
    private String newPassword;
}
