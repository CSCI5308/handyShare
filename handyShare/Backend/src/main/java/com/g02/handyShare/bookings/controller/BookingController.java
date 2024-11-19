package com.g02.handyShare.bookings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.bookings.service.BookingService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true" )

public class BookingController {

@Autowired
BookingService bookingService;

    @PostMapping("/user/borrowProduct")
    public ResponseEntity<?> borrowItem(@RequestBody Bookings borrowInstance){
        
        Bookings response = bookingService.addBorrowTransaction(borrowInstance);
        
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/user/product/ReturnedLender")
    public ResponseEntity<?> productReturnedLender(@RequestBody Map<String, Long> requestBody) {
        Long borrowId = requestBody.get("borrowId");
        if (borrowId == null) {
            return ResponseEntity.badRequest().body("Borrow ID is missing in the request");
        }
    
        ResponseEntity<?> response = bookingService.productReturnedLender(borrowId);
        return response;
    }

    @PostMapping("/user/product/ReturnedBorrower")
public ResponseEntity<?> productReturnedBorrower(
        @RequestParam("borrowId") Long borrowId, 
        @RequestParam("productImage") MultipartFile file) throws IOException {
    
    if (borrowId == null) {
        return ResponseEntity.badRequest().body("Borrow ID is missing in the request");
    }

    ResponseEntity<?> response = bookingService.productReturnedBorrower(borrowId, file);
    return response;
}


    @GetMapping("/user/borrowedProducts")
    public  List<Bookings> getBorrowedList(){
        List<Bookings> response = bookingService.getBorrowedItems();
        return  response;
    }

    @GetMapping("/user/lendedProducts")
    public  List<Bookings> getLendedItems(){
        List<Bookings> response = bookingService.getLendedItems();
        return  response;
    }

   

}
