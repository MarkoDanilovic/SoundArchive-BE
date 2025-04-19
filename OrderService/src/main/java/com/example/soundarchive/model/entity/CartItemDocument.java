package com.example.soundarchive.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
public class CartItemDocument {

    private Integer itemQuantity;

    private Integer trackId;

    private Integer mediumId;

    private Double price;
}
