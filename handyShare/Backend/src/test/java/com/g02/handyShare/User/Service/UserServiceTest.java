//package com.g02.handyShare.User.Service;
//
//import com.g02.handyShare.Product.Service.ProductService;
//import com.g02.handyShare.User.DTO.LenderDetailsDTO;
//import com.g02.handyShare.User.Entity.User;
//import com.g02.handyShare.Product.Entity.Product;
//import com.g02.handyShare.User.Repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private ProductService productService;
//
//    @InjectMocks
//    private UserService userService;
//
//    @Test
//    void getLenderDetails_ShouldReturnLenderDetailsDTO() {
//        // Arrange
//        Long lenderId = 1L;
//        User lender = new User();
//        lender.setId(lenderId);
//        lender.setName("Jane Doe");
//        lender.setEmail("jane@example.com");
//        lender.setAddress("123 Main St");
//        lender.setPhone("555-1234");
//        lender.setPincode("123456");
//        lender.setImageData("http://example.com/image.jpg");
//
//        List<Product> products = Arrays.asList(
//                new Product(/* initialize as needed */),
//                new Product(/* initialize as needed */)
//        );
//
//        when(userRepository.findById(lenderId)).thenReturn(Optional.of(lender));
//        when(productService.getProductsByLenderEmail(lender.getEmail())).thenReturn(products);
//
//        // Act
//        LenderDetailsDTO result = userService.getLenderDetails(lenderId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(lenderId, result.getId());
//        assertEquals("Jane Doe", result.getName());
//        assertEquals("jane@example.com", result.getEmail());
//        assertEquals("123 Main St", result.getAddress());
//        assertEquals("555-1234", result.getPhone());
//        assertEquals("123456", result.getPincode());
//        assertEquals("http://example.com/image.jpg", result.getImageData());
//        assertEquals(products, result.getProducts());
//    }
//
//    @Test
//    void getLenderDetails_ShouldThrowException_WhenLenderNotFound() {
//        // Arrange
//        Long lenderId = 2L;
//        when(userRepository.findById(lenderId)).thenReturn(Optional.empty());
//        // Act & Assert
//        assertThrows(RuntimeException.class, () -> userService.getLenderDetails(lenderId));
//    }
//}

//package com.g02.handyShare.User.Service;
//
//import com.g02.handyShare.Constants;
//import com.g02.handyShare.User.DTO.LenderDetailsDTO;
//import com.g02.handyShare.User.Entity.User;
//import com.g02.handyShare.User.Repository.UserRepository;
//import com.g02.handyShare.Product.Entity.Product;
//import com.g02.handyShare.Product.Service.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private EmailService emailService;
//
//    @Mock
//    private Constants constants;
//
//    @Mock
//    private ProductService productService;
//
//    @InjectMocks
//    private UserService userService;
//
//    private User testUser;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        testUser = new User();
//        testUser.setId(1L);
//        testUser.setEmail("test@example.com");
//        testUser.setPassword("password123");
//        testUser.setName("Test User");
//        testUser.set_email_verified(false);
//    }
//
//    @Test
//    void registerUser_Success() {
//        // Arrange
//        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(null);
//        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
//        when(constants.getSERVER_URL()).thenReturn("http://172.17.0.99:8080");
//        when(emailService.sendEmail(eq(testUser.getEmail()), anyString(), anyString())).thenReturn("Success");
//
//        // Act
//        String result = userService.registerUser(testUser);
//
//        // Assert
//        assertEquals("User registered successfully. Please check your email for verification.", result);
//        verify(userRepository).save(testUser);
//        assertNotNull(testUser.getVerificationToken());
//    }
//
//    @Test
//    void registerUser_AlreadyRegistered() {
//        // Arrange
//        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(testUser);
//
//        // Act
//        String result = userService.registerUser(testUser);
//
//        // Assert
//        assertEquals("already registered", result);
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void verifyUser_Success() {
//        // Arrange
//        String token = UUID.randomUUID().toString();
//        testUser.setVerificationToken(token);
//        when(userRepository.findByVerificationToken(token)).thenReturn(testUser);
//
//        // Act
//        String result = userService.verifyUser(token);
//
//        // Assert
//        assertEquals("Successfully verified email", result);
//        assertTrue(testUser.get_email_verified());
//        assertNull(testUser.getVerificationToken());
//        verify(userRepository).save(testUser);
//    }
//
//    @Test
//    void verifyUser_Failure() {
//        // Arrange
//        String token = UUID.randomUUID().toString();
//        when(userRepository.findByVerificationToken(token)).thenReturn(null);
//
//        // Act
//        String result = userService.verifyUser(token);
//
//        // Assert
//        assertEquals("Failed verifying the email", result);
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void getAllUsers() {
//        // Arrange
//        List<User> userList = new ArrayList<>();
//        userList.add(testUser);
//        when(userRepository.findAll()).thenReturn(userList);
//
//        // Act
//        List<User> result = userService.getAllUsers();
//
//        // Assert
//        assertEquals(1, result.size());
//        assertEquals(testUser, result.get(0));
//    }
//
//    @Test
//    void findUserById_Success() {
//        // Arrange
//        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
//
//        // Act
//        Optional<User> result = userService.findUserById(testUser.getId());
//
//        // Assert
//        assertTrue(result.isPresent());
//        assertEquals(testUser, result.get());
//    }
//
//    @Test
//    void findUserById_NotFound() {
//        // Arrange
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        // Act
//        Optional<User> result = userService.findUserById(1L);
//
//        // Assert
//        assertFalse(result.isPresent());
//    }
//
//    @Test
//    void getLenderDetails_Success() {
//        // Arrange
//        List<Product> productList = new ArrayList<>();
//        Product product = new Product();
//        product.setId(1L);
//        productList.add(product);
//
//        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
//        when(productService.getProductsByLenderEmail(testUser.getEmail())).thenReturn(productList);
//
//        // Act
//        LenderDetailsDTO result = userService.getLenderDetails(testUser.getId());
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(testUser.getName(), result.getName());
//        assertEquals(productList, result.getProducts());
//    }
//
//    @Test
//    void getLenderDetails_NotFound() {
//        // Arrange
//        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(RuntimeException.class, () -> userService.getLenderDetails(testUser.getId()));
//    }
//}
package com.g02.handyShare.User.Service;

import com.g02.handyShare.Constants;
import com.g02.handyShare.User.DTO.LenderDetailsDTO;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

   @Mock
   private UserRepository userRepository;

   @Mock
   private PasswordEncoder passwordEncoder;

   @Mock
   private EmailService emailService;

   @Mock
   private Constants constants;

   @Mock
   private ProductService productService;

   @InjectMocks
   private UserService userService;

   private User testUser;

   @BeforeEach
   void setUp() {
       MockitoAnnotations.openMocks(this);

       testUser = new User();
       testUser.setId(1L);
       testUser.setEmail("test@example.com");
       testUser.setPassword("password123");
       testUser.setName("Test User");
       testUser.set_email_verified(false); // Updated setter
   }

   @Test
   void registerUser_Success() {
       // Arrange
       when(userRepository.findByEmail(testUser.getEmail())).thenReturn(null);
       when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
       when(constants.getSERVER_URL()).thenReturn("http://172.17.0.99:8080");
       when(emailService.sendEmail(eq(testUser.getEmail()), anyString(), anyString())).thenReturn("Success");

       // Act
       String result = userService.registerUser(testUser);

       // Assert
       assertEquals("User registered successfully. Please check your email for verification.", result);
       verify(userRepository).save(testUser);
       assertNotNull(testUser.getVerificationToken());
   }

   @Test
   void registerUser_AlreadyRegistered() {
       // Arrange
       when(userRepository.findByEmail(testUser.getEmail())).thenReturn(testUser);

       // Act
       String result = userService.registerUser(testUser);

       // Assert
       assertEquals("already registered", result);
       verify(userRepository, never()).save(any());
   }

   @Test
   void verifyUser_Success() {
       // Arrange
       String token = UUID.randomUUID().toString();
       testUser.setVerificationToken(token);
       when(userRepository.findByVerificationToken(token)).thenReturn(testUser);

       // Act
       String result = userService.verifyUser(token);

       // Assert
       assertEquals("Successfully verified email", result);
       assertTrue(testUser.is_email_verified()); // Updated getter
       assertNull(testUser.getVerificationToken());
       verify(userRepository).save(testUser);
   }

   @Test
   void verifyUser_Failure() {
       // Arrange
       String token = UUID.randomUUID().toString();
       when(userRepository.findByVerificationToken(token)).thenReturn(null);

       // Act
       String result = userService.verifyUser(token);

       // Assert
       assertEquals("Failed verifying the email", result);
       verify(userRepository, never()).save(any());
   }

   @Test
   void getAllUsers() {
       // Arrange
       List<User> userList = new ArrayList<>();
       userList.add(testUser);
       when(userRepository.findAll()).thenReturn(userList);

       // Act
       List<User> result = userService.getAllUsers();

       // Assert
       assertEquals(1, result.size());
       assertEquals(testUser, result.get(0));
   }

   @Test
   void findUserById_Success() {
       // Arrange
       when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

       // Act
       Optional<User> result = userService.findUserById(testUser.getId());

       // Assert
       assertTrue(result.isPresent());
       assertEquals(testUser, result.get());
   }

   @Test
   void findUserById_NotFound() {
       // Arrange
       when(userRepository.findById(1L)).thenReturn(Optional.empty());

       // Act
       Optional<User> result = userService.findUserById(1L);

       // Assert
       assertFalse(result.isPresent());
   }

   @Test
   void getLenderDetails_Success() {
       // Arrange
       List<Product> productList = new ArrayList<>();
       Product product = new Product();
       product.setId(1L); // Initializing the product as needed
       productList.add(product);

       when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
       when(productService.getProductsByLenderEmail(testUser.getEmail())).thenReturn(productList);

       // Act
       LenderDetailsDTO result = userService.getLenderDetails(testUser.getId());

       // Assert
       assertNotNull(result);
       assertEquals(testUser.getName(), result.getName());
       assertEquals(productList, result.getProducts());
   }

   @Test
   void getLenderDetails_NotFound() {
       // Arrange
       when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

       // Act & Assert
       assertThrows(RuntimeException.class, () -> userService.getLenderDetails(testUser.getId()));
   }
}
