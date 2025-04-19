package com.example.soundarchive.mapper;

import com.example.soundarchive.model.dto.CartDTO;
import com.example.soundarchive.model.dto.CartItemDTO;
import com.example.soundarchive.model.entity.CartDocument;
import com.example.soundarchive.model.entity.CartItemDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    private final ObjectUtilMapper mapper;

    @Autowired
    public CartMapper(ObjectUtilMapper mapper) {
        this.mapper = mapper;
    }

    public CartDocument mapCart(CartDTO cartDTO) {

        CartDocument cartDocument = mapper.map(cartDTO, CartDocument.class);

        //cartDocument.setItems(mapper.mapList(cartDTO.getItems(), CartItemDocument.class));

        return cartDocument;
    }

    public CartDTO mapCartDTO(CartDocument cartDocument) {

        CartDTO cartDTO = mapper.map(cartDocument, CartDTO.class);

        //cartDTO.setItems(mapper.mapList(cartDocument.getItems(), CartItemDTO.class));

        return cartDTO;
    }
}
