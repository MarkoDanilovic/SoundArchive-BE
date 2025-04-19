package com.example.soundarchive.controller;

import com.example.soundarchive.model.dto.GenreDTO;
import com.example.soundarchive.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/soundArchive/genre")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GenreDTO> findById(@PathVariable("id") Integer id) {

        GenreDTO genreDTO = genreService.findById(id);

        return new ResponseEntity<>(genreDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GenreDTO>> findAll() {

        List<GenreDTO> genreDTOS = genreService.findAll();

        return new ResponseEntity<>(genreDTOS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GenreDTO> save(@RequestBody GenreDTO genreDTO) {

        GenreDTO genreDTO1 = genreService.save(genreDTO);

        return new ResponseEntity<>(genreDTO1, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<GenreDTO> update(@RequestBody GenreDTO genreDTO) {

        GenreDTO genreDTO1 = genreService.update(genreDTO);

        return new ResponseEntity<>(genreDTO1, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {

        genreService.delete(id);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
