package com.example.soundarchive.service;

import com.example.soundarchive.dao.CartRepository;
import com.example.soundarchive.exception.InternalServerErrorException;
import com.example.soundarchive.model.dto.QuantityDTO;
import com.example.soundarchive.model.entity.CartDocument;
import com.example.soundarchive.model.entity.CartItemDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class ScheduledTasksService {

    @Value("${services.recordBaseUrl}")
    private String recordBaseUrl;

    private final CartRepository cartRepository;

    private final RestTemplate restTemplate;

    @Autowired
    public ScheduledTasksService(CartRepository cartRepository, RestTemplate restTemplate) {
        this.cartRepository = cartRepository;
        this.restTemplate = restTemplate;
    }

    //@Scheduled(cron = "0 0 2 * * ?")//Jednom dnevno u 2
    @Scheduled(cron = "0 0 * * * ?")//svakih sat vremena
    public void removeOldReservations() {
        System.out.println("Running daily job at 2:00 AM");

        try {
            List<CartDocument> cartDocuments = cartRepository.findByStatus("reserved");

            for (CartDocument cartDocument : cartDocuments) {
                if(cartDocument.getOrderDate().before(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)))) {

                    for (CartItemDocument cartItemDocument : cartDocument.getItems()) {
                        QuantityDTO quantityDTO = new QuantityDTO();
                        quantityDTO.setTrackId(cartItemDocument.getTrackId());
                        quantityDTO.setMediumId(cartItemDocument.getMediumId());
                        quantityDTO.setQuantity(cartItemDocument.getItemQuantity());
                        quantityDTO.setAction("add");

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        HttpEntity<QuantityDTO> requestEntity = new HttpEntity<>(quantityDTO, headers);

                        restTemplate.exchange(
                                recordBaseUrl + "/updateQuantity",
                                HttpMethod.PUT,
                                requestEntity,
                                Void.class
                        );
                    }

                    cartRepository.delete(cartDocument);
                }
            }

        } catch (Exception e) {
            throw new InternalServerErrorException("An error occured while removing old reservations! " + e.getMessage());
        }
    }

    //Okida ponedeljkom u 3 i 15
    //@Scheduled(cron = "0 15 3 ? * MON")
    @Scheduled(cron = "0 15 3 * * ?")
    public void removeOldCarts() {
        System.out.println("Running weekly job on Monday at 3:00 AM");

        try {
            List<CartDocument> cartDocuments = cartRepository.findByStatus("new");

            for (CartDocument cartDocument : cartDocuments) {
                if(cartDocument.getOrderDate().before(Date.from(Instant.now().minus(7, ChronoUnit.DAYS)))) {

                    cartRepository.delete(cartDocument);
                }
            }

        } catch (Exception e) {
            throw new InternalServerErrorException("An error occured while removing old carts! " + e.getMessage());
        }
    }
}
