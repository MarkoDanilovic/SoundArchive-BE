package com.example.soundarchive.model.dto;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {

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

    private Boolean active;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
