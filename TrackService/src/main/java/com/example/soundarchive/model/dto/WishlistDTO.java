package com.example.soundarchive.model.dto;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistDTO {

    private Integer userId;

    private Integer trackId;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
