package com.example.soundarchive.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegisterDTO {

    private Integer id;
    @NotNull(message="Field firstName is mandatory.")
    @NotBlank(message="Field firstName is mandatory.")
    private String firstName;
    @NotNull(message="Field lastName is mandatory.")
    @NotBlank(message="Field lastName is mandatory.")
    private String lastName;
    private String dateOfBirth;
    @NotNull(message="Field email is mandatory.")
    @NotBlank(message="Field email is mandatory.")
    private String email;
    private String displayName;
    private String description;
    private String socialMediaLink;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private String creditCardNumber;

    @NotNull(message="Field username is mandatory.")
    @NotBlank(message="Field username is mandatory.")
    private String username;
    @NotNull(message="Field password1 is mandatory.")
    @NotBlank(message="Field password1 is mandatory.")
    private String password1;
    @NotNull(message="Field password2 is mandatory.")
    @NotBlank(message="Field password2 is mandatory.")
    private String password2;
    private Integer permissionLevel;

    private Integer artistId;//ne moraju neki parametri ovde
}
