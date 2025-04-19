package com.example.soundarchive.controller;

import com.example.soundarchive.model.dto.TrackDTO;
import com.example.soundarchive.model.dto.UpdateTrackDTO;
import com.example.soundarchive.model.dto.WishlistDTO;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.dto.pagination.PagedResultDTO;
import com.example.soundarchive.service.WishlistService;
import com.example.soundarchive.util.constants.Constants;
import com.example.soundarchive.util.pagination.PageableHelper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/soundArchive/wishlist")
@Validated
public class WishlistController {

    @Value("${conf.pagination.default-page}")
    private int defaultPage;

    @Value("${conf.pagination.default-size}")
    private int defaultSize;

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }


    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<PagedResultDTO<TrackDTO>> findByUserId(@RequestParam (required = false, defaultValue = "1", name = "page") Integer page,
                                                        @RequestParam (required = false, defaultValue = "10", name = "size") Integer size,
                                                        @RequestParam (required = false)
                                                            @Pattern(regexp = Constants.SORT_ORDER_ASC+"|"+ Constants.SORT_ORDER_DESC,
                                                                    message = "Field order can have either value "+Constants.SORT_ORDER_ASC
                                                                            +" or value "+Constants.SORT_ORDER_DESC) String order,
                                                        @RequestParam (required = false, name = "sortBy") String sortBy,
                                                        @PathVariable("userId") Integer userId) {

        Pageable pageable = PageableHelper.createPageable(page, size, sortBy, order, defaultPage, defaultSize);

        ListPagedResultDTO<TrackDTO> trackList = wishlistService.findByUserId(pageable, userId);

        int totalPages =(int) Math.ceil((double) trackList.getTotal() / pageable.getPageSize());
        if (totalPages == 0) {
            totalPages = 1;
        }

        PagedResultDTO<TrackDTO> trackPagedResultDTO = new PagedResultDTO<>(pageable.getPageNumber(), totalPages,
                pageable.getPageSize(), trackList.getTotal(), trackList.getList());

        return new ResponseEntity<>(trackPagedResultDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WishlistDTO> save(@RequestBody WishlistDTO wishlistDTO) {

        WishlistDTO wishlistDTO1 = wishlistService.save(wishlistDTO);

        return new ResponseEntity<>(wishlistDTO1, HttpStatus.OK);
    }

    @DeleteMapping(value = "/user/{userId}/track/{trackId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") Integer userId,
                                       @PathVariable("trackId") Integer trackId) {

        wishlistService.delete(userId, trackId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping(value = "/user/{userId}")
    public ResponseEntity<Void> deleteByUserId(@PathVariable("userId") Integer userId) {

        wishlistService.deleteByUserId(userId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
