
package com.g02.handyShare.Product.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Service.ProductService;
import com.g02.handyShare.User.Service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @WithMockUser
    public void testAddProduct() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setCategory("Electronics");
        product.setRentalPrice(50.0);

        MultipartFile file = mock(MultipartFile.class);
        ResponseEntity<Product> responseEntity = new ResponseEntity<>(product, HttpStatus.CREATED);
        doReturn(responseEntity).when(productService).addProduct(any(Product.class), any(MultipartFile.class));

        mockMvc.perform(multipart("/api/v1/user/add")
                .file("image", "dummy content".getBytes())
                .param("name", product.getName())
                .param("category", product.getCategory())
                .param("rentalPrice", String.valueOf(product.getRentalPrice())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.rentalPrice").value(50.0));
    }

    @Test
    @WithMockUser
    public void testViewProductById() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setCategory("Electronics");
        product.setRentalPrice(50.0);

        doReturn(product).when(productService).getProductById(1L);

        mockMvc.perform(get("/api/v1/user/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.rentalPrice").value(50.0));
    }

    @Test
    @WithMockUser
    public void testDeleteProduct() throws Exception {
        doReturn(true).when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/user/product/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted Successfully!"));
    }

    @Test
    @WithMockUser
    public void testUpdateProduct() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setCategory("Home Appliances");
        updatedProduct.setRentalPrice(75.0);

        MultipartFile file = mock(MultipartFile.class);
        ResponseEntity<Product> responseEntity = new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        doReturn(responseEntity).when(productService).updateProduct(eq(1L), any(Product.class), any(MultipartFile.class));

        mockMvc.perform(multipart("/api/v1/user/product/update/1")
                .file("image", "dummy content".getBytes())
                .param("name", updatedProduct.getName())
                .param("category", updatedProduct.getCategory())
                .param("rentalPrice", String.valueOf(updatedProduct.getRentalPrice())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.category").value("Home Appliances"))
                .andExpect(jsonPath("$.rentalPrice").value(75.0));
    }

    @Test
    @WithMockUser
    public void testChangeAvailability() throws Exception {
        String requestBody = "{\"status\": true}";

        doReturn(ResponseEntity.ok("Product availability changed successfully")).when(productService).changeAvailability(1L, true);

        mockMvc.perform(put("/api/v1/user/product/changeAvailability/1")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Product availability changed successfully"));
    }

    @Test
    @WithMockUser
    public void testGetAllProducts() throws Exception {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setCategory("Electronics");
        product1.setRentalPrice(50.0);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setCategory("Home Appliances");
        product2.setRentalPrice(75.0);

        doReturn(List.of(product1, product2)).when(productService).getAllProducts();

        mockMvc.perform(get("/api/v1/user/allProducts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @WithMockUser
    public void testGetNewlyAddedProductsByCategory() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Newly Added Product");
        product.setCategory("Electronics");

        doReturn(List.of(product)).when(productService).getNewlyAddedProductsByCategory("electronics");

        mockMvc.perform(get("/api/v1/user/newly-added")
                .param("category", "electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser
    public void testListProductsForUser() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("User Product");
        product.setCategory("Electronics");

        doReturn(List.of(product)).when(productService).listProductsForUser();

        mockMvc.perform(get("/api/v1/user/listUserItems"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}
