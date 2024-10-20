package com.g02.handyShare.Lending;

import com.g02.handyShare.Lending.Entity.LentItem;
import com.g02.handyShare.Lending.Repository.LentItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LendingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LentItemRepository lentItemRepository;

    @AfterEach
    void cleanup() {
        lentItemRepository.deleteAll();
    }

    @Test
    void testAddLentItem() throws Exception {
        String newItemJson = "{\"name\":\"Test Item\",\"description\":\"Test Description\",\"price\":10.0,\"availability\":\"Available\"}";

        mockMvc.perform(post("/api/v1/lending/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newItemJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Item"));
    }

    @Test
    void testGetAllLentItems() throws Exception {
        LentItem item = new LentItem();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setPrice(10.0);
        item.setAvailability("Available");
        lentItemRepository.save(item);

        mockMvc.perform(get("/api/v1/lending/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Item"));
    }
}
