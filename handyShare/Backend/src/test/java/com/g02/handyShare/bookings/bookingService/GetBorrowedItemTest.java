package com.g02.handyShare.bookings.bookingService;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.bookings.service.BookingService;
import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.bookings.repository.BookingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class GetBorrowedItemTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    private User testUser;
    private List<Bookings> testBookingsList;

    @Before
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Set up SecurityContext to return the mocked Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mocking authentication to return a valid email
        when(authentication.getName()).thenReturn("test@handyshare.com");

        // Initialize test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@handyshare.com");

        // Initialize test bookings list
        testBookingsList = new ArrayList<>();
        Bookings booking1 = new Bookings();
        booking1.setId(1L);
        booking1.setBorrower(testUser);

        Bookings booking2 = new Bookings();
        booking2.setId(2L);
        booking2.setBorrower(testUser);

        testBookingsList.add(booking1);
        testBookingsList.add(booking2);

        // Mocking userRepository to return test user
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(testUser);
        // Mocking bookingRepository to return test bookings list
        when(bookingRepository.findAllByBorrowerId(testUser.getId())).thenReturn(testBookingsList);
    }

    // Test case 1: Get borrowed items successfully
    @Test
    public void getBorrowedItems_success() {
        // Act
        List<Bookings> result = bookingService.getBorrowedItems();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testUser, result.get(0).getBorrower());
        assertEquals(Long.valueOf(1L), result.get(0).getId());
        assertEquals(Long.valueOf(2L), result.get(1).getId());
    }

    // Test case 2: No borrowed items found
    @Test
    public void getBorrowedItems_noBorrowedItems() {
        // Arrange
        when(bookingRepository.findAllByBorrowerId(testUser.getId())).thenReturn(new ArrayList<>());

        // Act
        List<Bookings> result = bookingService.getBorrowedItems();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test case 3: User not found
    @Test(expected = NullPointerException.class)
    public void getBorrowedItems_userNotFound() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(null);

        // Act
        bookingService.getBorrowedItems();
    }

    // Test case 4: Authentication returns null (Edge Case)
    @Test(expected = NullPointerException.class)
    public void getBorrowedItems_authenticationIsNull() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        bookingService.getBorrowedItems();
    }
}
