package com.example.soundarchive.model.dto;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class TrackDTO {

    private Integer id;

    private String name;

    private Timestamp publishDate;

    private Double duration;

    private String picture;

    private GenreDTO genre;

    private ArtistDTO artist;

    private List<RecordDTO> records;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
