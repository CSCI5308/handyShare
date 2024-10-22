// package com.g02.handyShare.Product.Controller;

// import com.g02.handyShare.Product.Entity.Product;
// import com.g02.handyShare.Product.Service.ProductService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(ProductController.class)
// public class ProductControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private ProductService productService;

//     private Product product;

//     @BeforeEach
//     public void setup() {
//         product = new Product();
//         product.setId(1L);
//         product.setName("Test Product");
//         product.setDescription("Test Description");
//         product.setCategory("Test Category");
//         product.setRentalPrice(100.0);
//         product.setAvailable(true);
//     }

//     @Test
//     public void testViewProductById() throws Exception {
//         when(productService.getProductById(1L)).thenReturn(product);

//         mockMvc.perform(get("/api/v1/products/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name").value("Test Product"))
//                 .andExpect(jsonPath("$.description").value("Test Description"));
//     }
// }
