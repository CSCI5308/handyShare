// package com.g02.handyShare.Category.Controller;

// import com.g02.handyShare.Category.DTO.ProductIDRequest;
// import com.g02.handyShare.Category.Entity.Trending;
// import com.g02.handyShare.Category.Service.TrendingService;
// import com.g02.handyShare.Product.Entity.Product;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.mockito.Mockito.*;

// import java.util.Arrays;
// import java.util.List;

// @ExtendWith(MockitoExtension.class)
// @WebMvcTest(TrendingController.class)  // Test only the controller layer
// class TrendingControllerTest {

//     @Mock
//     private TrendingService trendingService;

//     @InjectMocks
//     private TrendingController trendingController;

//     private MockMvc mockMvc;

//     @BeforeEach
//     void setUp() {
//         mockMvc = MockMvcBuilders.standaloneSetup(trendingController).build();
//     }

//     @Test
//     void testGetTrendingByCategory_Success() throws Exception {
//         // Arrange
//         String category = "Tools";
//         Product product1 = new Product(1L, "Hammer", "Description", category, "hammer.jpg", 10.0, null, null, true);
//         Product product2 = new Product(2L, "Drill", "Description", category, "drill.jpg", 20.0, null, null, true);
        
//         Trending trending1 = new Trending(1L, product1);
//         Trending trending2 = new Trending(2L, product2);
        
//         List<Trending> trendingList = Arrays.asList(trending1, trending2);
        
//         // Use doReturn to mock the service's behavior
//         doReturn(trendingList).when(trendingService).fetchTrendingsByCategory("Tools");

//         // Act & Assert
//         mockMvc.perform(get("/api/v1/user/getTrendingByCategory")
//                         .param("category", category)
//                         .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].product.id").value(1L))
//                 .andExpect(jsonPath("$[1].product.id").value(2L))
//                 .andExpect(jsonPath("$[0].product.name").value("Hammer"))
//                 .andExpect(jsonPath("$[1].product.name").value("Drill"));
//     }

//     @Test
//     void testGetTrendingByCategory_NoResults() throws Exception {
//         // Arrange
//         String category = "NonExistentCategory";
        
//         // Use doReturn instead of thenReturn
//         doReturn(java.util.Collections.emptyList()).when(trendingService).fetchTrendingsByCategory(category);

//         // Act & Assert
//         mockMvc.perform(get("/api/v1/user/getTrendingByCategory")
//                         .param("category", category)
//                         .contentType(MediaType.APPLICATION_JSON))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$").isEmpty());  // Check if the response body is empty
//     }

//     @Test
//     void testAddToTrending_Success() throws Exception {
//         // Arrange
//         Long productId = 1L;
//         Product product = new Product(1L, "Hammer", "Description", "Tools", "hammer.jpg", 10.0, null, null, true);
//         Trending expectedTrending = new Trending(1L, product);

//         ProductIDRequest request = new ProductIDRequest();
//         request.setProduct_id(productId);

//         when(trendingService.addToTrending(productId)).thenReturn(expectedTrending);

//         // Act & Assert
//         mockMvc.perform(post("/api/v1/all/addToTrending")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(new ObjectMapper().writeValueAsString(request)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.product.id").value(1L))
//                 .andExpect(jsonPath("$.product.name").value("Hammer"));
//     }

//     @Test
//     void testAddToTrending_ProductNotFound() throws Exception {
//         // Arrange
//         Long productId = 999L;
//         ProductIDRequest request = new ProductIDRequest();
//         request.setProduct_id(productId);

//         when(trendingService.addToTrending(productId)).thenThrow(new RuntimeException("Product not found with id: " + productId));

//         // Act & Assert
//         mockMvc.perform(post("/api/v1/all/addToTrending")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(new ObjectMapper().writeValueAsString(request)))
//                 .andExpect(status().isInternalServerError())
//                 .andExpect(jsonPath("$.message").value("Something went wrong while adding the product to the trending table: Product not found with id: " + productId));
//     }
// }
