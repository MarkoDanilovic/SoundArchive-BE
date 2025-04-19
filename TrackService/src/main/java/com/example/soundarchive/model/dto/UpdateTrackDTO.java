package com.example.soundarchive.model.dto;

import com.example.soundarchive.model.entity.ArtistEntity;
import com.example.soundarchive.model.entity.GenreEntity;
import com.example.soundarchive.model.entity.RecordEntity;
import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class UpdateTrackDTO {

    private String name;

    private Timestamp publishDate;

    private Double duration;

    private String picture;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}