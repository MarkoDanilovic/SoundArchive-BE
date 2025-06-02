package com.example.soundarchive.model.dto;

import com.google.gson.Gson;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartItem {

    @NotNull
    private Integer userId;
    @NotBlank
    @NotNull
    private String id;//cart id
    @NotNull
    private Integer trackId;
    @NotNull
    private Integer mediumId;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
