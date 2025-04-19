package com.example.soundarchive.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LoginDTO {

    @NotNull(message="Field username is mandatory.")
    @NotBlank(message="Field username is mandatory.")
    private String username;
    @NotNull(message="Field password is mandatory.")
    @NotBlank(message="Field password is mandatory.")
    private String password;
}
