package com.travelbnb.controller;

import com.travelbnb.entity.AppUser;
import com.travelbnb.entity.Image;
import com.travelbnb.entity.Property;
import com.travelbnb.repository.ImageRepository;
import com.travelbnb.repository.PropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private PropertyRepository propertyRepository;
    private ImageRepository imageRepository;

    public ImageController(PropertyRepository propertyRepository, ImageRepository imageRepository) {
        this.propertyRepository = propertyRepository;
        this.imageRepository = imageRepository;
    }


    @PostMapping(path = "upload/file/{bucketName}/property/{propertyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPropertyPhotos(
            @RequestParam MultipartFile file,
            @PathVariable String bucketName,
            @PathVariable long propertyId,
            @AuthenticationPrincipal AppUser user
            ){

        Property property = propertyRepository.findById(propertyId).get();

        Image image = new Image();
        image.setProperty(property);


        Image savedImageEntity = imageRepository.save(image);
        return new ResponseEntity<>(savedImageEntity, HttpStatus.CREATED);
    }
}
