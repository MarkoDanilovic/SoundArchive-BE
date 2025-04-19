package com.example.soundarchive.controller;

import com.example.soundarchive.model.dto.ArtistDTO;
import com.example.soundarchive.model.dto.pagination.ListPagedResultDTO;
import com.example.soundarchive.model.dto.pagination.PagedResultDTO;
import com.example.soundarchive.service.ArtistService;
import com.example.soundarchive.util.constants.Constants;
import com.example.soundarchive.util.pagination.PageableHelper;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/soundArchive/artist")
public class ArtistController {

    @Value("${conf.pagination.default-page}")
    private int defaultPage;

    @Value("${conf.pagination.default-size}")
    private int defaultSize;

    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ArtistDTO> findById(@PathVariable("id") Integer id) {

        ArtistDTO artistDTO = artistService.findById(id);

        return new ResponseEntity<>(artistDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<List<ArtistDTO>> findByName(@PathVariable("name") String name) {

        List<ArtistDTO> artistDTOs = artistService.findByName(name);

        return new ResponseEntity<>(artistDTOs, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedResultDTO<ArtistDTO>> findAll(@RequestParam (required = false, defaultValue = "1", name = "page") Integer page,
                                                             @RequestParam (required = false, defaultValue = "10", name = "size") Integer size,
                                                             @RequestParam (required = false)
                                                                 @Pattern(regexp = Constants.SORT_ORDER_ASC+"|"+ Constants.SORT_ORDER_DESC,
                                                                         message = "Field order can have either value "+Constants.SORT_ORDER_ASC
                                                                                 +" or value "+Constants.SORT_ORDER_DESC) String order,
                                                             @RequestParam (required = false, name = "sortBy") String sortBy,
                                                             @RequestParam (required = false, name = "name") String name) {

        Pageable pageable = PageableHelper.createPageable(page, size, sortBy, order, defaultPage, defaultSize);

        ListPagedResultDTO<ArtistDTO> artistList = artistService.findAll(pageable, name);

        int totalPages =(int) Math.ceil((double) artistList.getTotal() / pageable.getPageSize());
        if (totalPages == 0) {
            totalPages = 1;
        }

        PagedResultDTO<ArtistDTO> artistPagedResultDTO = new PagedResultDTO<>(pageable.getPageNumber(), totalPages,
                pageable.getPageSize(), artistList.getTotal(), artistList.getList());

        return new ResponseEntity<>(artistPagedResultDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ArtistDTO> save(@RequestBody ArtistDTO artistDTO) {

        ArtistDTO artistDTO1 = artistService.save(artistDTO);

        return new ResponseEntity<>(artistDTO1, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ArtistDTO> update(@RequestBody ArtistDTO artistDTO) {

        ArtistDTO artistDTO1 = artistService.update(artistDTO);

        return new ResponseEntity<>(artistDTO1, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {

        artistService.delete(id);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
