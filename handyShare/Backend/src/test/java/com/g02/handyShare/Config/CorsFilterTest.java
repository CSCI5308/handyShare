package com.g02.handyShare.Config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MvcResult;
import jakarta.servlet.Filter;
import com.g02.handyShare.Config.CorsFilter;
import com.g02.handyShare.Constants;  // Adjust the path if Constants is in a different package

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class CorsFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Constants constant;  // Mock the Constants class if it is used in CorsFilter

    @InjectMocks
    private CorsFilter corsFilter;  // Inject mocks into the filter

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    public void testCorsFilterWithOPTIONSRequest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .options("/api/v1/all/upload")  // Assuming a path for testing
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH"))
                .andExpect(header().string("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token"))
                .andExpect(header().string("Access-Control-Expose-Headers", "xsrf-token"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"))
                .andReturn();

        // Optionally, you can assert the response body (it could be empty or something else as needed)
    }

    @Test
    public void testCorsFilterWithGETRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/all/upload")  // Assuming a path for testing
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH"))
                .andExpect(header().string("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token"))
                .andExpect(header().string("Access-Control-Expose-Headers", "xsrf-token"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }

    @Test
    public void testCorsFilterWithInvalidOrigin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/all/upload")
                        .header("Origin", "http://invalid-origin.com"))
                .andExpect(status().isOk())  // Ensure status is OK even with an invalid origin
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));  // No CORS headers should be set for an invalid origin
    }
}
