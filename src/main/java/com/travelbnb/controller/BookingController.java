package com.travelbnb.controller;
import com.travelbnb.entity.Property;
import com.travelbnb.entity.AppUser;
import com.travelbnb.entity.Booking;
import com.travelbnb.repository.BookingRepository;
import com.travelbnb.repository.PropertyRepository;
import com.travelbnb.service.PDFService;
import com.travelbnb.service.TwilioService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private PropertyRepository propertyRepository;
    private BookingRepository bookingRepository;
    private TwilioService twilioService;

    private PDFService pdfService;

    public BookingController(PropertyRepository propertyRepository, BookingRepository bookingRepository, TwilioService twilioService, PDFService pdfService) {
        this.propertyRepository = propertyRepository;
        this.bookingRepository = bookingRepository;
        this.twilioService = twilioService;
        this.pdfService = pdfService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBookings(
            @RequestParam long propertyId,
            @AuthenticationPrincipal AppUser user,
            @RequestBody Booking booking

    ){

        Property property = propertyRepository.findById(propertyId).get();
        int nightlyPrice = property.getPrice();
        int totalPrice = nightlyPrice*booking.getTotalNights();

        booking.setProperty(property);
        booking.setAppUser(user);
        booking.setPrice(totalPrice);
        Booking savedBooking = bookingRepository.save(booking);
        boolean b = pdfService.generatePDF("F://STS//" + "bookings-confirmation-id" + savedBooking.getId() + ".pdf", savedBooking);
        if (b){
            try {
                MultipartFile file = BookingController.convert("F://STS//" + "bookings-confirmation-id" + savedBooking.getId() + ".pdf");
                String uploadedFileUrl = "http://confirmation.html";
                String smsId = twilioService.sendSms(booking.getMobile(),
                        "Your booking is confirmed. Click for details: " + uploadedFileUrl);
                System.out.println(smsId);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return new ResponseEntity<>(savedBooking,HttpStatus.CREATED);

    }

    public static  MultipartFile convert(String filePath) throws IOException{

        File file = new File(filePath);

        byte[] fileContent = Files.readAllBytes(file.toPath());

        ByteArrayResource resource = new ByteArrayResource(fileContent);

        MultipartFile multipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return file.getName();
            }

            @Override
            public String getOriginalFilename() {
                return file.getName();
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return fileContent.length == 0;
            }

            @Override
            public long getSize() {
                return fileContent.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return fileContent;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return resource.getInputStream();
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                Files.write(dest.toPath(),fileContent);
            }
        };
        return multipartFile;
    }
}
