package com.example.nasa.controller;

import com.example.nasa.exception.RoverException;
import com.example.nasa.model.Mars;
import com.example.nasa.model.Photo;
import com.example.nasa.service.RoverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mars")
@Slf4j
public class RoverController {

    @Autowired
    RoverService roverService;

    @CrossOrigin(origins = "https://api.nasa.gov")
    @GetMapping("/photos")
    public ResponseEntity getMarsPhotos(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
                    String earth_date) {
        List<Photo> photos = roverService.getMarsPhotos(earth_date);
        if (photos.size() == 0)
            throw new RoverException(earth_date);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/allphotos")
    public ResponseEntity getAllmarsPhotos() {
        List<Mars> marsList = roverService.getAllMarsPhotos();
        return ResponseEntity.ok(marsList);
    }
}
