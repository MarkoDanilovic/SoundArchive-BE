package com.example.soundarchive.controller;

import com.example.soundarchive.model.dto.TrackDTO;
import com.example.soundarchive.model.dto.UpdateTrackDTO;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.dto.pagination.PagedResultDTO;
import com.example.soundarchive.service.TrackService;
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
@RequestMapping(value = "/api/soundArchive/track")
@Validated
public class TrackController {

    @Value("${conf.pagination.default-page}")
    private int defaultPage;

    @Value("${conf.pagination.default-size}")
    private int defaultSize;

    private final TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TrackDTO> findById(@Valid @PathVariable("id") Integer id) {

        TrackDTO trackDTO = trackService.findById(id);

        return new ResponseEntity<>(trackDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<List<TrackDTO>> findByName(@PathVariable("name") String name) {

        List<TrackDTO> trackDTOs = trackService.findByName(name);//dodati paginaciju...

        return new ResponseEntity<>(trackDTOs, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedResultDTO<TrackDTO>> findAll(@RequestParam (required = false, defaultValue = "1", name = "page") Integer page,
                                                  @RequestParam (required = false, defaultValue = "10", name = "size") Integer size,
                                                  @RequestParam (required = false)
                                                      @Pattern(regexp = Constants.SORT_ORDER_ASC+"|"+ Constants.SORT_ORDER_DESC,
                                                              message = "Field order can have either value "+Constants.SORT_ORDER_ASC
                                                                      +" or value "+Constants.SORT_ORDER_DESC) String order,
                                                  @RequestParam (required = false, name = "sortBy") String sortBy,
                                                  @RequestParam (required = false, name = "name") String name,
                                                  @RequestParam (required = false, name = "genreId") Integer genreId,
                                                  @RequestParam (required = false, name = "mediumId") Integer mediumId,
                                                  @RequestParam (required = false, name = "artistName") String artistName,
                                                  @RequestParam (required = false, name = "artistId") Integer artistId) {

        Pageable pageable = PageableHelper.createPageable(page, size, sortBy, order, defaultPage, defaultSize);

        ListPagedResultDTO<TrackDTO> trackList = trackService.findAll(pageable, name, genreId, mediumId, artistName, artistId);

        int totalPages =(int) Math.ceil((double) trackList.getTotal() / pageable.getPageSize());
        if (totalPages == 0) {
            totalPages = 1;
        }

        PagedResultDTO<TrackDTO> trackPagedResultDTO = new PagedResultDTO<>(pageable.getPageNumber(), totalPages,
                pageable.getPageSize(), trackList.getTotal(), trackList.getList());

        return new ResponseEntity<>(trackPagedResultDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TrackDTO> save(@RequestBody TrackDTO trackDTO) {

        TrackDTO trackDTOReturn = trackService.save(trackDTO);

        return new ResponseEntity<>(trackDTOReturn, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TrackDTO> update(@PathVariable("id") Integer id,
                                           @RequestBody UpdateTrackDTO updateTrackDTO) {

        TrackDTO trackDTOReturn = trackService.update(updateTrackDTO, id);

        return new ResponseEntity<>(trackDTOReturn, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {

        trackService.delete(id);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
