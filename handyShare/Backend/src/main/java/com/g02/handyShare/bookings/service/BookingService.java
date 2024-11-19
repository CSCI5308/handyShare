package com.g02.handyShare.bookings.service;

import com.g02.handyShare.Config.Firebase.FirebaseService;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.Product.Service.CustomException;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.bookings.repository.BookingRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

 private FirebaseService firebaseService;

 @Autowired
 public void Controller(FirebaseService firebaseService) {
     this.firebaseService = firebaseService;
 }

 @Autowired
 PriceCalculator calculator;


    public Bookings addBorrowTransaction(Bookings borrowInstance) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = userRepository.findByEmail(authentication.getName());
    Product product = productRepository.findById(borrowInstance.getProduct().getId())
            .orElseThrow(() -> new CustomException("Product not found with id: " + borrowInstance.getProduct().getId()));

    System.out.println(product);

    borrowInstance.setBorrower(user);
    borrowInstance.setProduct(product);

    // Set timerStart using the passed date and time
    LocalDateTime timerStart = borrowInstance.getTimerStart();

    if (timerStart == null) {
        throw new CustomException("Start time is required.");
    }

    // Calculate the end time based on the duration
    int durationInHours = borrowInstance.getDuration();
    LocalDateTime endTimer = timerStart.plusHours(durationInHours);
    borrowInstance.setTimerEnd(endTimer);

    // Check if there is an existing overlapping booking for the same product by the same user
    boolean isOverlap = bookingRepository.existsByBorrowerAndProductAndOverlappingTime(
             product, timerStart, endTimer);

    if (isOverlap) {
        throw new CustomException("The product is already booked by you for the selected time period.");
    }

    // Save the borrow instance
     productRepository.save(product);
    return bookingRepository.save(borrowInstance);
}

    public List<Bookings> getBorrowedItems() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User borrower = userRepository.findByEmail(email);

        // Fetch all Borrow records for the authenticated user
        return bookingRepository.findAllByBorrowerId(borrower.getId());
    }


    public List<Bookings> getLendedItems() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User lender = userRepository.findByEmail(email);
        System.out.println("Authenticated User ID: " + lender.getId());


        // Fetch all Borrow records for the authenticated user
        return bookingRepository.findAllByLenderId(lender.getId());
    }

    public ResponseEntity<?> productReturnedLender(Long borrowId) {
        //fetch booking details from the booking table
        Bookings borrowInstance = bookingRepository.findById(borrowId)
                .orElseThrow(() -> new CustomException("Borrow entry not found with id: " + borrowId));

        //extract the productId from the booking details
        Long productId = borrowInstance.getProduct().getId();

        //based on the productId fetch the product from the product table.
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException("Product not found with id: " + productId));

        LocalDateTime returnTime = LocalDateTime.now();
        LocalDateTime timerEnd = borrowInstance.getTimerEnd();

        //set the return date and time as the current date and time
        borrowInstance.setReturnDateTime(returnTime);

        //call the price calculator method which check for the penulty and calculate the payment based on the 2% platform fees
        double price = calculator.calculatePayment(returnTime,timerEnd,borrowInstance, product);

        // Mark product as available, so that it becomes available for further usage
        product.setAvailable(true);

        //set the totalPament field of the database.
        borrowInstance.setTotalPayment(price);

        //save the updated booking details
        bookingRepository.save(borrowInstance);

        //save the updated product instance
        productRepository.save(product);

        // Create response message
        String responseMessage = "Product " + product.getName() + " has been returned to " + product.getLender().getName();
        if (borrowInstance.getPenalty() != null && borrowInstance.getPenalty() > 0) {
            responseMessage += " with a late return penalty of $" + borrowInstance.getPenalty();
        }

        return ResponseEntity.ok().body(responseMessage);
    }

    public ResponseEntity<?> productReturnedBorrower(Long borrowId, MultipartFile file) throws IOException{
        String path = "/returnProofByBorrower";

        //upload return proof to the firbase and get the link for it.
        String imageUrl = firebaseService.uploadFile(file, path);

        //fetch the booking instance based on the id
        Bookings borrowInstance = bookingRepository.findById(borrowId)
                .orElseThrow(() -> new CustomException("Borrow entry not found with id: " + borrowId));

        //set the image url stored in the firebase in the booking instance
        borrowInstance.setReturnImage(imageUrl);
        //set the return date and time in the booking instance.
        LocalDateTime returnTime = LocalDateTime.now();
        borrowInstance.setReturnByBorrowerTime(returnTime);

        //save updated booking instance in the database.
        bookingRepository.save(borrowInstance);
                return ResponseEntity.ok().body("Product successfully marked as return.");
    }
}
