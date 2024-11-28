package com.g02.handyShare.bookings.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.bookings.repository.BookingRepository;

@Service
public class AvailabilityTimer {
      @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;
   @Scheduled(fixedRate = 6000)
   @Transactional
   public void checkAndUpdateAvailability() {
       LocalDateTime currentTime = LocalDateTime.now();
       List<Bookings> bookingsList = bookingRepository.findAll();

       for (Bookings booking : bookingsList) {
           Product product = booking.getProduct();
           LocalDateTime startTime = booking.getTimerStart();
           LocalDateTime endTime = booking.getTimerEnd();
           LocalDateTime returnDateTime = booking.getReturnDateTime();

           // Check if returnDateTime is null
           if (returnDateTime == null) {
               // Check start time - make unavailable
               if (currentTime.isEqual(startTime) ||
                       (currentTime.isAfter(startTime) )) {

                   product.setAvailable(false);
                   productRepository.save(product);
                   System.out.println("Product " + product.getId() + " set to unavailable. Borrow start time: " + startTime);
               }
           }
       }
   }
 
}
