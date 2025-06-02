package com.example.soundarchive.controller;

import com.example.soundarchive.model.dto.CreateRecordDTO;
import com.example.soundarchive.model.dto.QuantityDTO;
import com.example.soundarchive.model.dto.RecordDTO;
import com.example.soundarchive.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/soundArchive/record")
public class RecordController {

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping(value = "/track/{trackId}")
    public ResponseEntity<List<CreateRecordDTO>> findByTrackId(@PathVariable("trackId") Integer trackId) {

        List<CreateRecordDTO> createRecordDTOs = recordService.findByTrackId(trackId);

        return new ResponseEntity<>(createRecordDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/track/{trackId}/medium/{mediumId}")
    public ResponseEntity<CreateRecordDTO> findByTrackAndMedium(@PathVariable("trackId") Integer trackId,
                                                                @PathVariable("mediumId") Integer mediumId) {

        CreateRecordDTO createRecordDTO = recordService.findByTrackAndMedium(trackId, mediumId);

        return new ResponseEntity<>(createRecordDTO, HttpStatus.OK);
    }

    //FindByMediumId ???

    @PostMapping
    public ResponseEntity<CreateRecordDTO> save(@RequestBody CreateRecordDTO createRecordDTO) {

        CreateRecordDTO createRecordDTO1 = recordService.save(createRecordDTO);

        return new ResponseEntity<>(createRecordDTO1, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<CreateRecordDTO> update(@RequestBody CreateRecordDTO createRecordDTO) {

        CreateRecordDTO createRecordDTO1 = recordService.update(createRecordDTO);

        return new ResponseEntity<>(createRecordDTO1, HttpStatus.OK);
    }

    @DeleteMapping(value = "/track/{trackId}")
    public ResponseEntity<Void> deleteByTrackId(@PathVariable("trackId") Integer trackId) {

        recordService.deleteByTrackId(trackId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping(value = "/updateQuantity")
    public ResponseEntity<Void> updateQuantity(@RequestBody QuantityDTO quantityDTO) {

        recordService.updateQuantity(quantityDTO);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
