package com.g02.handyShare.borrow.Service;

import com.g02.handyShare.Product.Service.CustomException;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.borrow.entity.Borrow;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.borrow.repository.BorrowRepository;
import com.g02.handyShare.borrow.service.BorrowService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowServiceTest {

    @InjectMocks
    private BorrowService borrowService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private BorrowRepository borrowRepository;

    private User user;
    private Product product;
    private Borrow borrowInstance;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");

        product = new Product();
        product.setId(1L);
        product.setAvailable(true);

        borrowInstance = new Borrow();
        borrowInstance.setProduct(product);
        borrowInstance.setDuration(24);

        MockitoAnnotations.openMocks(this);

        // Set up the SecurityContext to return the mocked Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Ensure that the authentication mock is not null and configured correctly
        when(authentication.getName()).thenReturn("test@example.com");
    }


    
    @Test
    void testAddBorrowTransaction_ProductNotFound() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(CustomException.class, () -> borrowService.addBorrowTransaction(borrowInstance));
    }

    // Test case for the getLendedItems() method

    @Test
    void testGetLendedItems_Success() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        // Mock BorrowRepository to return a list of borrow records for the authenticated user
        Borrow borrow1 = new Borrow();
        borrow1.setProduct(product);
        borrow1.setDuration(24);
        
        Borrow borrow2 = new Borrow();
        borrow2.setProduct(product);
        borrow2.setDuration(48);
        
        when(borrowRepository.findAllByLenderId(user.getId())).thenReturn(List.of(borrow1, borrow2));

        // Act
        List<Borrow> result = borrowService.getLendedItems();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(borrow1, result.get(0));
        assertEquals(borrow2, result.get(1));
        verify(borrowRepository, times(1)).findAllByLenderId(user.getId());
    }

    @Test
    void testGetLendedItems_UserNotFound() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        // Act and Assert
        assertThrows(NullPointerException.class, () -> borrowService.getLendedItems());
    }
    
}
