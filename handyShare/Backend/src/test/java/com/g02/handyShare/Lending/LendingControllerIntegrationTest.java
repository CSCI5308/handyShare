package com.g02.handyShare.Lending;

 import com.g02.handyShare.Lending.Entity.LentItem;
 import com.g02.handyShare.Lending.Repository.LentItemRepository;
 import org.junit.jupiter.api.AfterEach;
 import org.junit.jupiter.api.Test;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
 import org.springframework.boot.test.context.SpringBootTest;
 import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
    MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "test image content".getBytes());
    String lentItemJson = "{\"name\":\"Test Item\",\"description\":\"Test Description\",\"price\":10.0,\"availability\":\"Available\",\"category\":\"Category1\",\"city\":\"City1\",\"state\":\"State1\",\"pincode\":\"123456\",\"address\":\"Address1\"}";

    mockMvc.perform(multipart("/api/v1/all/lending/item")
            .file(image)
            .param("lentItem", lentItemJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Test Item"))
            .andExpect(jsonPath("$.price").value(10.0));
}

    @Test
    void testGetAllLentItems() throws Exception {
        LentItem item = new LentItem();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setPrice(10.0);
        item.setAvailability("Available");
        lentItemRepository.save(item);

        mockMvc.perform(get("/api/v1/all/lending/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Item"));
    }
}
