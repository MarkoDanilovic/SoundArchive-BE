package com.example.soundarchive.model.dto;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CartDTO {

    private String id;

    private Date orderDate;

    private Double subtotal;

    private String comment;

    private String paymentMethod;

    private String status;

    private String address;

    private String city;

    private Integer userId;

    private List<CartItemDTO> items;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
