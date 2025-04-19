package com.example.soundarchive.model.dto;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRecordDTO {

    private Integer trackId;

    private MediumDTO medium;

    private Integer quantity;

    private Double price;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
