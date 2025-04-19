package com.example.soundarchive.controller;

import com.example.soundarchive.model.dto.MediumDTO;
import com.example.soundarchive.service.MediumService;
import org.hibernate.query.sqm.mutation.internal.cte.CteInsertStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/soundArchive/medium")
public class MediumController {

    private final MediumService mediumService;

    @Autowired
    public MediumController(MediumService mediumService) {
        this.mediumService = mediumService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MediumDTO> findById(@PathVariable("id") Integer id) {

        MediumDTO mediumDTO = mediumService.findById(id);

        return new ResponseEntity<>(mediumDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MediumDTO>> findAll() {

        List<MediumDTO> mediumDTOS = mediumService.findAll();

        return new ResponseEntity<>(mediumDTOS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MediumDTO> save(@RequestBody MediumDTO mediumDTO) {

        MediumDTO mediumDTO1 = mediumService.save(mediumDTO);

        return new ResponseEntity<>(mediumDTO1, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<MediumDTO> update(@RequestBody MediumDTO mediumDTO) {

        MediumDTO mediumDTO1 = mediumService.update(mediumDTO);

        return new ResponseEntity<>(mediumDTO1, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {

        mediumService.delete(id);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
