package com.example.soundarchive.util;

import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.model.dto.CartDTO;
import com.example.soundarchive.model.dto.CartItemDTO;
import com.example.soundarchive.model.dto.QuantityDTO;
import com.example.soundarchive.model.dto.TrackDTO;
import com.example.soundarchive.model.entity.CartDocument;
import com.example.soundarchive.model.entity.CartItemDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class OrderHelper {

    private final RestTemplate restTemplate;

    @Autowired
    public OrderHelper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${services.trackBaseUrl}")
    private String trackBaseUrl;

    @Value("${services.recordBaseUrl}")
    private String recordBaseUrl;

    public void populateWithTracks(CartDTO cartDTO) {

        if (cartDTO.getItems() == null) return;

        for (CartItemDTO item : cartDTO.getItems()) {
            String trackUrl = String.format("%s/%d", trackBaseUrl, item.getTrackId());
            try {
                TrackDTO track = restTemplate.getForObject(trackUrl, TrackDTO.class);

                if(track != null) {
                    track.getRecords().removeIf(record -> !Objects.equals(record.getMedium().getId(), item.getMediumId()));

                    item.setTrack(track);

                    System.out.println("\n\n\n\nOvo je setovan track.\n" + track.toString() + "\n\n\n\n");
                }

            } catch (Exception e) {
                System.out.println("Greska pri pozivu backend track, findById. " + e.getMessage());

                item.setTrack(null);
            }
        }
    }

    public void calculateTotalDTO(CartDTO cartDTO) {
        double subtotal = 0;
        for (CartItemDTO cartItemDTO : cartDTO.getItems()) {
            subtotal += cartItemDTO.getItemQuantity() * cartItemDTO.getPrice();
        }
        subtotal = Math.round(subtotal * 100.0) / 100.0;
        cartDTO.setSubtotal(subtotal);
    }

    public void calculateTotalDocument(CartDocument cartDocument) {
        double subtotal = 0;
        for (CartItemDocument cartItemDocument : cartDocument.getItems()) {
            subtotal += cartItemDocument.getItemQuantity() * cartItemDocument.getPrice();
        }
        subtotal = Math.round(subtotal * 100.0) / 100.0;
        cartDocument.setSubtotal(subtotal);
    }

    public void addOrRemoveQuantity(CartDocument cartIDocument, String action) {

        if(cartIDocument == null) return;
        if(cartIDocument.getItems() == null ||cartIDocument.getItems().isEmpty()) return;

        for (CartItemDocument cartItemDocument : cartIDocument.getItems()) {
            try {
                QuantityDTO quantityDTO = new QuantityDTO();
                quantityDTO.setTrackId(cartItemDocument.getTrackId());
                quantityDTO.setMediumId(cartItemDocument.getMediumId());
                quantityDTO.setQuantity(cartItemDocument.getItemQuantity());
                quantityDTO.setAction(action);

                String quantityUrl = String.format("%s/updateQuantity", recordBaseUrl);
                restTemplate.put(quantityUrl, quantityDTO, Void.class);
            } catch(Exception e) {
                System.out.println("Greska pri pozivu backend track, updateQuantity. " + e.getMessage());

                throw new InternalServerErrorException("Greska pri pozivu backend track, updateQuantity. " + e.getMessage());
            }
        }
    }
}
