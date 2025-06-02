package com.example.soundarchive.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuantityDTO {

    private Integer trackId;

    private Integer mediumId;

    private Integer quantity;

    private String action;
}
