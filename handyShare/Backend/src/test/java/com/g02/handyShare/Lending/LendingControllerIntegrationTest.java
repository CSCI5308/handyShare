package com.g02.handyShare.Lending;

import com.g02.handyShare.Lending.Entity.LentItem;
import com.g02.handyShare.Lending.Repository.LentItemRepository;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LendingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LentItemRepository lentItemRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void cleanup() {
        lentItemRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void testAddLentItem() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "test image content".getBytes());
        String lentItemJson = "category=Category1&name=Test Item&description=Test Description&price=10.0&availability=Available&city=City1&state=State1&pincode=123456&address=Address1";

        mockMvc.perform(multipart("/api/v1/user/lending/item")
                .file(image)
                .param("category", "Category1")
                .param("name", "Test Item")
                .param("description", "Test Description")
                .param("price", "10.0")
                .param("availability", "Available")
                .param("city", "City1")
                .param("state", "State1")
                .param("pincode", "123456")
                .param("address", "Address1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.price").value(10.0));
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void testGetAllLentItems() throws Exception {
        LentItem item = new LentItem();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setPrice(10.0);
        item.setAvailability("Available");
        // Assume user is set via association
        lentItemRepository.save(item);

        mockMvc.perform(get("/api/v1/user/lending/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Item"));
    }
}
