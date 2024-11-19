package com.g02.handyShare.bookings.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.bookings.entity.Bookings;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, Long> {

    // Custom query to find all Booking records for a specific borrower ID
    @Query("SELECT b FROM Bookings b WHERE b.borrower.id = :borrowerId")
    List<Bookings> findAllByBorrowerId(@Param("borrowerId") Long borrowerId);

//    // Find all Booking records for a specific lender ID where returnDateTime is null
//    @Query("SELECT b FROM Bookings b WHERE b.product.lender.id = :lenderId AND b.returnDateTime IS NULL")
//    List<Bookings> findAllByLenderId(@Param("lenderId") Long lenderId);

    @Query("SELECT b FROM Bookings b " +
            "WHERE b.product.lender.id = :lenderId " +
            "AND b.product.available = false " +
            "AND b.timerStart <= CURRENT_TIMESTAMP " +
            "AND b.returnDateTime IS NULL")
    List<Bookings> findAllByLenderId(@Param("lenderId") Long lenderId);


    //    // Check for overlapping bookings by borrower and product
//    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
//            "FROM Bookings b WHERE b.product = :product " +
//            "AND b.borrower = :borrower " +
//            "AND b.timerEnd > :timerStart " +
//            "AND b.timerStart < :timerEnd")
//    boolean existsByBorrowerAndProductAndOverlappingTime(
//            @Param("borrower") User borrower,
//            @Param("product") Product product,
//            @Param("timerStart") LocalDateTime timerStart,
//            @Param("timerEnd") LocalDateTime timerEnd
//    );
@Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END " +
        "FROM Bookings b " +
        "WHERE b.product = :product " +
         
        "AND (b.returnDateTime IS NULL AND b.timerEnd > :timerStart AND b.timerStart < :timerEnd)")
boolean existsByBorrowerAndProductAndOverlappingTime(
        
        @Param("product") Product product,
        @Param("timerStart") LocalDateTime timerStart,
        @Param("timerEnd") LocalDateTime timerEnd
);

}
