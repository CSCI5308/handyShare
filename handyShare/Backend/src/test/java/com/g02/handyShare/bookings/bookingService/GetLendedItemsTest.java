package com.g02.handyShare.bookings.bookingService;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.bookings.repository.BookingRepository;
import com.g02.handyShare.bookings.service.BookingService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

public class GetLendedItemsTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private BookingService bookingService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetLendedItems_Success() {
        // Mock authenticated user's email
        when(authentication.getName()).thenReturn("testuser@example.com");

        // Mock user entity
        User lender = new User();
        lender.setId(1L);
        lender.setEmail("testuser@example.com");
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(lender);

        // Mock bookings
        Bookings booking1 = new Bookings();
        booking1.setId(101L);

        Bookings booking2 = new Bookings();
        booking2.setId(102L);

        List<Bookings> bookings = Arrays.asList(booking1, booking2);
        when(bookingRepository.findAllByLenderId(1L)).thenReturn(bookings);

        // Call the method
        List<Bookings> result = bookingService.getLendedItems();

        // Validate
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Long.valueOf(101), result.get(0).getId());
        assertEquals(Long.valueOf(102), result.get(1).getId());

        // Verify interactions
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByEmail("testuser@example.com");
        verify(bookingRepository, times(1)).findAllByLenderId(1L);
    }

    @Test
    public void testGetLendedItems_NoBookings() {
        // Mock authenticated user's email
        when(authentication.getName()).thenReturn("testuser@example.com");

        // Mock user entity
        User lender = new User();
        lender.setId(1L);
        lender.setEmail("testuser@example.com");
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(lender);

        // Mock no bookings
        when(bookingRepository.findAllByLenderId(1L)).thenReturn(Arrays.asList());

        // Call the method
        List<Bookings> result = bookingService.getLendedItems();

        // Validate
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify interactions
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByEmail("testuser@example.com");
        verify(bookingRepository, times(1)).findAllByLenderId(1L);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLendedItems_UserNotFound() {
        // Mock authenticated user's email
        when(authentication.getName()).thenReturn("nonexistent@example.com");

        // Mock no user found
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        // Call the method (should throw NullPointerException)
        bookingService.getLendedItems();

        // Verify interactions
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
        verify(bookingRepository, never()).findAllByLenderId(anyLong());
    }
}
