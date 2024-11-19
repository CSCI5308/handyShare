// package com.g02.handyShare.borrow.Controller;

// import java.lang.reflect.Field;
// import com.g02.handyShare.borrow.controller.BorrowController;
// import com.g02.handyShare.borrow.entity.Borrow;
// import com.g02.handyShare.borrow.service.BorrowService;
// import com.g02.handyShare.Product.Entity.Product;
// import com.g02.handyShare.User.Entity.User;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// import org.springframework.web.context.WebApplicationContext;

// import java.time.LocalDateTime;
// import java.util.Arrays;
// import java.util.List;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// class BorrowControllerTest {

//     @Mock
//     private BorrowService borrowService;

//     @InjectMocks
//     private BorrowController borrowController;

//     private MockMvc mockMvc;

//     private Borrow borrow;

//     @BeforeEach
//     void setUp() throws Exception {
//         mockMvc = MockMvcBuilders.standaloneSetup(borrowController).build();
    
//         // Mock the security context (authentication)
//         Authentication authentication = mock(Authentication.class);
//         when(authentication.getName()).thenReturn("testUser");
//         SecurityContextHolder.getContext().setAuthentication(authentication);
    
//         // Mock the User object (with reflection to set the username)
//         User borrower = new User(); // Initialize the User object
//         setField(borrower, "username", "testUser");  // Use reflection to set the field
    
//         // Initialize sample Borrow object
//         Product product = new Product();  // You can mock this or create a sample
//         product.setId(1L);
//         product.setName("Product 1");
    
//         borrow = new Borrow(1L, borrower, product, 5, 100, LocalDateTime.now(), LocalDateTime.now().plusDays(5), 0);
//     }
    
//     // Helper method to set the field using reflection
//     private void setField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
//         Field field = target.getClass().getDeclaredField(fieldName);
//         field.setAccessible(true); // Make private fields accessible
//         field.set(target, value); // Set the field value
//     }    

//     @Test
//     void testBorrowItem() throws Exception {
//         // Arrange: Mock the service to return the borrow object
//         when(borrowService.addBorrowTransaction(any(Borrow.class))).thenReturn(borrow);

//         // Act & Assert: Perform POST request and verify the response
//         mockMvc.perform(post("/api/v1/user/borrowProduct")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"borrower\": {\"id\": 1}, \"product\": {\"id\": 1}, \"duration\": 5, \"amount\": 100, \"timerStart\": \"2024-11-19T00:00:00\", \"timerEnd\": \"2024-11-24T00:00:00\", \"penalty\": 0}")
//                 .accept(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.amount").value(100));
//     }

//     @Test
//     void testGetBorrowedList() throws Exception {
//         // Arrange: Mock the service to return a list of borrowed items
//         List<Borrow> borrowedItems = Arrays.asList(borrow);
//         when(borrowService.getBorrowedItems()).thenReturn(borrowedItems);

//         // Act & Assert: Perform GET request and verify the response
//         mockMvc.perform(get("/api/v1/user/borrowedProducts")
//                 .accept(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].id").value(1))
//                 .andExpect(jsonPath("$[0].amount").value(100));
//     }

//     @Test
//     void testGetLendedItems() throws Exception {
//         // Arrange: Mock the service to return a list of lended items
//         List<Borrow> lendedItems = Arrays.asList(borrow);
//         when(borrowService.getLendedItems()).thenReturn(lendedItems);

//         // Act & Assert: Perform GET request and verify the response
//         mockMvc.perform(get("/api/v1/user/lendedProducts")
//                 .accept(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].id").value(1))
//                 .andExpect(jsonPath("$[0].amount").value(100));
//     }
// }
