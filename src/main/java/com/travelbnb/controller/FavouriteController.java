package com.travelbnb.controller;

import com.travelbnb.entity.AppUser;
import com.travelbnb.entity.Favourite;
import com.travelbnb.entity.Property;
import com.travelbnb.repository.FavouriteRepository;
import com.travelbnb.repository.PropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favourites")
public class FavouriteController {

    private FavouriteRepository favouriteRepository;
    public PropertyRepository propertyRepository;

    public FavouriteController(FavouriteRepository favouriteRepository) {
        this.favouriteRepository = favouriteRepository;
    }

    @PostMapping("/addFavourite")
    public ResponseEntity<Favourite>addFavourite(
            @AuthenticationPrincipal AppUser user,
            @RequestParam long propertyId,
            @RequestBody Favourite favourite

            ){
        Property property = propertyRepository.findById(propertyId).get();
        favourite.setAppUser(user);
        favourite.setProperty(property);
        Favourite savedEntity = favouriteRepository.save(favourite);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);

    }
}
