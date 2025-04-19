package com.example.soundarchive.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "cart")
public class CartDocument {

    @Id
    private String id;

    private Date orderDate;

    private Double subtotal;

    private String comment;

    private String paymentMethod;

    private String status;

    private String address;

    private String city;

    private Integer userId;

    private List<CartItemDocument> items;
}
