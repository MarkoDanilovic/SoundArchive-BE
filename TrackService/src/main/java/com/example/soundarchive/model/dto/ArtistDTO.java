package com.example.soundarchive.model.dto;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ArtistDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private String artistName;

    private Timestamp birthday;

    private String country;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
