package com.example.soundarchive.service;

import com.example.soundarchive.dao.CartRepository;
import com.example.soundarchive.exception.BadRequestException;
import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.mapper.ObjectUtilMapper;
import com.example.soundarchive.model.dto.CartDTO;
import com.example.soundarchive.model.dto.CartItemDTO;
import com.example.soundarchive.model.dto.CreateCartDTO;
import com.example.soundarchive.model.entity.CartDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final ObjectUtilMapper mapper;

    @Autowired
    public CartService(CartRepository cartRepository, ObjectUtilMapper mapper) {
        this.cartRepository = cartRepository;
        this.mapper = mapper;
    }

    public CartDTO findById(String id) {

        CartDocument cartDocument;

        try {
            cartDocument= cartRepository.findById(id).orElse(null);

            if(cartDocument == null) throw new DataNotFoundException("Cart with id: " + id + " not found. ");

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch(Exception e) {
            throw new InternalServerErrorException("Greska: " + e.getMessage() + " stack trace: " + Arrays.toString(e.getStackTrace()));
        }

        return mapper.map(cartDocument, CartDTO.class);
    }

    public List<CartDTO> findByUserId(Integer userId) {

        List<CartDocument> cartDocuments;

        try {
            cartDocuments =  cartRepository.findByUserId(userId);

            if(cartDocuments == null || cartDocuments.isEmpty()) throw new DataNotFoundException("Cart with userId: " + userId + " not found. ");

        } catch(Exception e) {
            throw new InternalServerErrorException("Greska: " + e.getMessage() + " stack trace: " + Arrays.toString(e.getStackTrace()));
        }

        return mapper.mapList(cartDocuments, CartDTO.class);
    }

    public CartDTO save(CreateCartDTO createCartDTO) {

        CartDTO cartDTO = mapper.map(createCartDTO, CartDTO.class);

        double subtotal = 0;
        for (CartItemDTO cartItemDTO : cartDTO.getItems()) {
            subtotal += cartItemDTO.getItemQuantity() * cartItemDTO.getPrice();
        }
        subtotal = Math.round(subtotal * 100.0) / 100.0;
        cartDTO.setSubtotal(subtotal);

        CartDocument cartDocument = mapper.map(cartDTO, CartDocument.class);

        try {
            cartRepository.save(cartDocument);

        } catch(Exception e) {
            throw new InternalServerErrorException("Greska: " + e.getMessage() + " stack trace: " + Arrays.toString(e.getStackTrace()));
        }

        return mapper.map(cartDocument, CartDTO.class);
    }

    public CartDTO update(CartDTO cartDTO) {

        if (cartDTO.getId() == null) throw new BadRequestException("No id provided in the update method");

        double subtotal = 0;
        for (CartItemDTO cartItemDTO : cartDTO.getItems()) {
            subtotal += cartItemDTO.getItemQuantity() * cartItemDTO.getPrice();
        }
        subtotal = Math.round(subtotal * 100.0) / 100.0;
        cartDTO.setSubtotal(subtotal);

        CartDocument cartDocument = mapper.map(cartDTO, CartDocument.class);

        try {
            cartRepository.save(cartDocument);

        } catch(Exception e) {
            throw new InternalServerErrorException("Greska: " + e.getMessage() + " stack trace: " + Arrays.toString(e.getStackTrace()));
        }

        return mapper.map(cartDocument, CartDTO.class);
    }

    public void delete(String id) {

        try {
            cartRepository.deleteById(id);

        } catch(Exception e) {
            throw new InternalServerErrorException("Greska: " + e.getMessage() + " stack trace: " + Arrays.toString(e.getStackTrace()));
        }
    }

}
