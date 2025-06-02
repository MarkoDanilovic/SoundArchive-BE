package com.example.soundarchive.service;

import com.example.soundarchive.dao.CartRepository;
import com.example.soundarchive.exception.BadRequestException;
import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.mapper.ObjectUtilMapper;
import com.example.soundarchive.model.dto.*;
import com.example.soundarchive.model.entity.CartDocument;
import com.example.soundarchive.model.entity.CartItemDocument;
import com.example.soundarchive.util.OrderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final ObjectUtilMapper mapper;

    private final RestTemplate restTemplate;

    private final OrderHelper orderHelper;

    @Autowired
    public CartService(CartRepository cartRepository, ObjectUtilMapper mapper, RestTemplate restTemplate, OrderHelper orderHelper) {
        this.cartRepository = cartRepository;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
        this.orderHelper = orderHelper;
    }

    @Value("${services.recordBaseUrl}")
    private String recordBaseUrl;

    @Value("${services.trackBaseUrl}")
    private String trackBaseUrl;

    public CartDTO findById(String id) {

        CartDocument cartDocument;

        try {
            cartDocument= cartRepository.findById(id).orElse(null);

            if(cartDocument == null) throw new DataNotFoundException("Cart with id: " + id + " not found. ");

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch(Exception e) {
            throw new InternalServerErrorException("Greska: " + e.getMessage());
        }

        return mapper.map(cartDocument, CartDTO.class);
    }

    public List<CartDTO> findByUserId(Integer userId) {

        List<CartDocument> cartDocuments;

        try {
            cartDocuments =  cartRepository.findByUserId(userId);

            if(cartDocuments == null || cartDocuments.isEmpty()) throw new DataNotFoundException("Cart with userId: " + userId + " not found. ");

        } catch(Exception e) {
            throw new InternalServerErrorException("Greska: " + e.getMessage());
        }

        return mapper.mapList(cartDocuments, CartDTO.class);
    }

    @Transactional
    public CartDTO save(CreateCartDTO createCartDTO) {

        CartDTO cartDTO = mapper.map(createCartDTO, CartDTO.class);

        orderHelper.calculateTotalDTO(cartDTO);

        CartDocument cartDocument = mapper.map(cartDTO, CartDocument.class);

        try {
            cartRepository.save(cartDocument);

        } catch(Exception e) {
            throw new InternalServerErrorException("Greska: " + e.getMessage());
        }

        return mapper.map(cartDocument, CartDTO.class);
    }

    @Transactional
    public CartDTO update(CartDTO cartDTO) {

        if (cartDTO.getId() == null) throw new BadRequestException("No id provided in the update method");

        orderHelper.calculateTotalDTO(cartDTO);

        CartDocument cartDocument = mapper.map(cartDTO, CartDocument.class);

        try {
            cartRepository.save(cartDocument);

        } catch(Exception e) {
            throw new InternalServerErrorException("Greska: " + e.getMessage());
        }

        return mapper.map(cartDocument, CartDTO.class);
    }

    @Transactional
    public void delete(String id) {

        try {
            cartRepository.deleteById(id);

        } catch(Exception e) {
            throw new InternalServerErrorException("Greska: " + e.getMessage());
        }
    }

    @Transactional
    public CartDTO getUnorderedCartByUserId(Integer userId) {

        CartDocument cartDocument;
        List<String> statuses = Arrays.asList("reserved", "new");

        CartDTO cartDTO = new CartDTO();

        try {
            Sort sort = Sort.by(Sort.Order.desc("status"));

            cartDocument = cartRepository
                    .findFirstByUserIdAndStatusIn(userId, statuses, sort)
                    .orElse(null);

            if (cartDocument == null) {
                cartDocument = new CartDocument();
                cartDocument.setUserId(userId);
                cartDocument.setStatus("new");
                cartDocument.setOrderDate(new Date());

                cartRepository.save(cartDocument);
            }

            cartDTO = mapper.map(cartDocument, CartDTO.class);

            if (cartDTO.getItems() == null) return cartDTO;

            orderHelper.populateWithTracks(cartDTO);

            return cartDTO;

        } catch (Exception e) {
            throw new InternalServerErrorException("Error while calling getUnorderedCartByUserId, userId " + userId + ", Error:" + e.getMessage());
        }
    }

    @Transactional
    public CartDTO addToCart(AddCartItem addCartItem) {

        CartDocument cartDocument;

        try {
            cartDocument = cartRepository.findById(addCartItem.getId()).orElse(null);

            if (cartDocument == null)
                throw new DataNotFoundException("Error while calling addToCart. Cart with id: " + addCartItem.getId() + " not found. ");

            String url = String.format("%s/track/%d/medium/%d", recordBaseUrl, addCartItem.getTrackId(), addCartItem.getMediumId());
            RecordDTO recordDTO = restTemplate.getForObject(url, RecordDTO.class);

            if (recordDTO == null)
                throw new DataNotFoundException("Error while calling addToCart. Record with trackId: " + addCartItem.getTrackId() +
                        "Record with mediumId: " + addCartItem.getMediumId() + " not found. ");
            if (recordDTO.getQuantity() <= 0)
                throw new BadRequestException("Error while calling addToCart. Record with trackId: " + addCartItem.getTrackId() +
                        "Record with mediumId: " + addCartItem.getMediumId() + " has no items in stock! ");

            boolean exists = false;

            if (cartDocument.getItems() != null) {
                for (CartItemDocument item : cartDocument.getItems()) {

                    if (Objects.equals(item.getTrackId(), addCartItem.getTrackId()) && Objects.equals(item.getMediumId(), addCartItem.getMediumId())) {

                        if (item.getItemQuantity() == null) {
                            item.setItemQuantity(1);
                        } else {
                            item.setItemQuantity(item.getItemQuantity() + 1);
                        }

                        item.setPrice(recordDTO.getPrice());//in case of price change

                        exists = true;
                    }
                }
            }


            if(!exists) {
                CartItemDocument cartItemDocument = new CartItemDocument();
                cartItemDocument.setTrackId(recordDTO.getTrackId());
                cartItemDocument.setMediumId(recordDTO.getMedium().getId());
                cartItemDocument.setItemQuantity(1);
                cartItemDocument.setPrice(recordDTO.getPrice());

                if(cartDocument.getItems() == null) {
                    List<CartItemDocument> cartItemDocuments = new ArrayList<>();
                    cartItemDocuments.add(cartItemDocument);

                    cartDocument.setItems(cartItemDocuments);
                } else {
                    cartDocument.getItems().add(cartItemDocument);
                }
            }

            cartDocument.setOrderDate(new Date());
            orderHelper.calculateTotalDocument(cartDocument);
            cartRepository.save(cartDocument);

            if(Objects.equals(cartDocument.getStatus(), "reserved")) {
                QuantityDTO quantityDTO = new QuantityDTO();
                quantityDTO.setTrackId(addCartItem.getTrackId());
                quantityDTO.setMediumId(addCartItem.getMediumId());
                quantityDTO.setQuantity(1);
                quantityDTO.setAction("add");

                String quantityUrl = String.format("%s/updateQuantity", recordBaseUrl);
                restTemplate.put(url, quantityDTO, Void.class);
            }

            CartDTO cartDTO = mapper.map(cartDocument, CartDTO.class);

            if (cartDTO.getItems() == null) return cartDTO;

            orderHelper.populateWithTracks(cartDTO);

            return cartDTO;

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        }catch (Exception e) {
            throw new InternalServerErrorException("Error while calling addToCart: " + e.getMessage());
        }
    }

    @Transactional
    public CartDTO removeFromCart(AddCartItem addCartItem) {
        CartDocument cartDocument;

        try {
            cartDocument= cartRepository.findById(addCartItem.getId()).orElse(null);

            if(cartDocument == null) throw new DataNotFoundException("Error while calling addToCart. Cart with id: " + addCartItem.getId() + " not found. ");

            if(!Objects.equals(cartDocument.getStatus(), "new") && !Objects.equals(cartDocument.getStatus(), "reserved")) throw new BadRequestException("\"Error while calling addToCart. Cart is neither new nor reserved! ");

            String url = String.format("%s/track/%d/medium/%d", recordBaseUrl, addCartItem.getTrackId(), addCartItem.getMediumId());
            RecordDTO recordDTO = restTemplate.getForObject(url, RecordDTO.class);

            if(recordDTO == null) throw new DataNotFoundException("Error while calling addToCart. Record with trackId: " + addCartItem.getTrackId() +
                    "Record with mediumId: " + addCartItem.getMediumId() + " not found. ");
            if(recordDTO.getQuantity() <= 0) throw new BadRequestException("Error while calling addToCart. Record with trackId: " + addCartItem.getTrackId() +
                    "Record with mediumId: " + addCartItem.getMediumId() + " has no items in stock! ");

            CartItemDocument itemForRemoval = null;
            for (CartItemDocument item : cartDocument.getItems()) {

                if(Objects.equals(item.getTrackId(), addCartItem.getTrackId()) && Objects.equals(item.getMediumId(), addCartItem.getMediumId())) {

                    if(item.getItemQuantity() > 1) {
                        item.setItemQuantity(item.getItemQuantity() - 1);
                        item.setPrice(recordDTO.getPrice());//in case of price change
                    } else {
                        itemForRemoval = item;
                    }
                }
            }

            if(itemForRemoval != null) cartDocument.getItems().remove(itemForRemoval);

            cartDocument.setOrderDate(new Date());
            orderHelper.calculateTotalDocument(cartDocument);
            cartRepository.save(cartDocument);

            if(Objects.equals(cartDocument.getStatus(), "reserved")) {
                QuantityDTO quantityDTO = new QuantityDTO();
                quantityDTO.setTrackId(addCartItem.getTrackId());
                quantityDTO.setMediumId(addCartItem.getMediumId());
                quantityDTO.setQuantity(1);
                quantityDTO.setAction("remove");

                String quantityUrl = String.format("%s/updateQuantity", recordBaseUrl);
                restTemplate.put(url, quantityDTO, Void.class);
            }

            CartDTO cartDTO = mapper.map(cartDocument, CartDTO.class);

            if (cartDTO.getItems() == null) return cartDTO;

            orderHelper.populateWithTracks(cartDTO);

            return cartDTO;

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        }catch (Exception e) {
            throw new InternalServerErrorException("Error while calling removeFromCart: " + e.getMessage());
        }
    }

    public void changeStatus(String id, String status) {

        CartDocument cartDocument;

        try {
            cartDocument = cartRepository.findById(id).orElse(null);

            if(cartDocument == null) throw new DataNotFoundException("Cart with id: " + id + " not found. ");

            String oldStatus = cartDocument.getStatus();

            cartDocument.setOrderDate(new Date());

            if(status != null && !status.isEmpty() && (status.equals("new") || status.equals("reserved") || status.equals("ordered")  || status.equals("cancelled"))) {
                
                if((oldStatus.equals("new") && status.equals("reserved")) ||
                        (oldStatus.equals("new") && status.equals("ordered"))) {

                    orderHelper.addOrRemoveQuantity(cartDocument, "remove");

                } else if ((oldStatus.equals("reserved") && status.equals("new")) ||
                        (oldStatus.equals("reserved") && status.equals("cancelled")) ||
                        (oldStatus.equals("ordered") && status.equals("cancelled"))) {

                    orderHelper.addOrRemoveQuantity(cartDocument, "add");
                }

                cartDocument.setStatus(status);
            }

            cartRepository.save(cartDocument);

        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch(Exception e) {
            throw new InternalServerErrorException("Greska: " + e.getMessage());
        }
    }
}
