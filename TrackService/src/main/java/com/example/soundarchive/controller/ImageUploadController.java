package com.example.soundarchive.controller;

import com.example.soundarchive.model.dto.ArtistDTO;
import com.example.soundarchive.model.dto.TrackDTO;
import com.example.soundarchive.service.ArtistService;
import com.example.soundarchive.service.ImageUploadService;
import com.example.soundarchive.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/soundArchive/upload")
public class ImageUploadController {

    private final ImageUploadService imageUploadService;
    private final ArtistService artistService;
    private final TrackService trackService;

    @Autowired
    public ImageUploadController(ImageUploadService imageUploadService,
                                 ArtistService artistService,
                                 TrackService trackService) {
        this.imageUploadService = imageUploadService;
        this.artistService = artistService;
        this.trackService = trackService;
    }

    @PostMapping("/artist/{id}")
    public ResponseEntity<ArtistDTO> uploadArtistImage(@PathVariable Integer id,
                                                       @RequestParam("file") MultipartFile file) {
        ArtistDTO artist = artistService.findById(id);
        String path = imageUploadService.uploadImage(file, "artists", id.toString(), artist.getPicture());
        ArtistDTO updated = artistService.updatePicture(id, path);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PostMapping("/track/{id}")
    public ResponseEntity<TrackDTO> uploadTrackImage(@PathVariable Integer id,
                                                     @RequestParam("file") MultipartFile file) {
        TrackDTO track = trackService.findById(id);
        String path = imageUploadService.uploadImage(file, "tracks", id.toString(), track.getPicture());
        TrackDTO updated = trackService.updatePicture(id, path);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

}
