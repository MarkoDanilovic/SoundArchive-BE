package com.example.soundarchive.model.dto;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private String displayName;
    private String description;
    private String socialMediaLink;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private String creditCardNumber;

    private String username;
    private String password;
    private Integer permissionLevel;

    private Boolean active;

    private Integer artistId;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
