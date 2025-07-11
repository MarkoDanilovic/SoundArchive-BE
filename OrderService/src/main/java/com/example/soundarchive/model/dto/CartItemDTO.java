package com.example.soundarchive.model.dto;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {

    private Integer itemQuantity;

    private Integer trackId;

    private Integer mediumId;

    private Double price;

    private TrackDTO track;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}