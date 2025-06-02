package com.example.soundarchive.controller;

import com.example.soundarchive.model.dto.AddCartItem;
import com.example.soundarchive.model.dto.CartDTO;
import com.example.soundarchive.model.dto.CreateCartDTO;
import com.example.soundarchive.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/soundArchive/cart")
@Validated
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CartDTO> findById(@Valid @PathVariable("id") String id) {

        CartDTO cartDTO = cartService.findById(id);

        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartDTO>> getCartsByUserId(@PathVariable Integer userId) {

        List<CartDTO> cartDTOs = cartService.findByUserId(userId);

        return new ResponseEntity<>(cartDTOs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CartDTO> save(@RequestBody CreateCartDTO createCartDTO) {

        CartDTO cartDTO = cartService.save(createCartDTO);

        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<CartDTO> update(@RequestBody CartDTO cartDTO) {

        CartDTO cartDTO1 = cartService.update(cartDTO);

        return new ResponseEntity<>(cartDTO1, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        cartService.delete(id);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    @GetMapping("/unordered/user/{userId}")
    public ResponseEntity<CartDTO> getUnorderedCart(@PathVariable Integer userId) {

        CartDTO cartDTO = cartService.getUnorderedCartByUserId(userId);

        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/addToCart")
    public ResponseEntity<CartDTO> addToCart(@RequestBody AddCartItem addCartItem) {

        CartDTO cartDTO = cartService.addToCart(addCartItem);

        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/removeFromCart")
    public ResponseEntity<CartDTO> removeFromCart(@RequestBody AddCartItem addCartItem) {

        CartDTO cartDTO = cartService.removeFromCart(addCartItem);

        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}/changeStatus/{status}")
    public ResponseEntity<Void> changeStatus(@PathVariable String id,@PathVariable String status) {

        cartService.changeStatus(id, status);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
