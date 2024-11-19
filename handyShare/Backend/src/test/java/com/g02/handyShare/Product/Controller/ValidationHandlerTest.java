// package com.g02.handyShare.Product.Controller;

// import com.g02.handyShare.Product.Service.CustomException;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.FieldError;
// import org.springframework.validation.BindingResult;
// import org.springframework.web.bind.MethodArgumentNotValidException;

// import static org.mockito.Mockito.*;
// import static org.junit.jupiter.api.Assertions.*;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Map;

// class ValidationHandlerTest {

//     @InjectMocks
//     private ValidationHandler validationHandler;

//     @Mock
//     private BindingResult bindingResult;

//     @Mock
//     private FieldError fieldError;

//     @BeforeEach
//     void setUp() {
//         // Initialize mocks if necessary
//     }

//     @Test
//     void testHandleValidationExceptions() throws Exception {
//         // Arrange: Set up the mock for MethodArgumentNotValidException
//         List<FieldError> fieldErrors = Arrays.asList(
//             new FieldError("object", "field1", "Field 1 cannot be empty"),
//             new FieldError("object", "field2", "Field 2 is required")
//         );
//         when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

//         // MethodArgumentNotValidException constructor requires MethodParameter and BindingResult
//         MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

//         // Act: Call the handler method
//         ResponseEntity<Map<String, String>> response = validationHandler.handleValidationExceptions(exception);

//         // Assert: Verify that the response contains the expected errors
//         assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//         assertEquals(2, response.getBody().size());
//         assertEquals("Field 1 cannot be empty", response.getBody().get("field1"));
//         assertEquals("Field 2 is required", response.getBody().get("field2"));
//     }

//     @Test
//     void testHandleProductNotFoundException() {
//         // Arrange: Create a CustomException
//         CustomException customException = new CustomException("Product not found");

//         // Act: Call the handler method
//         ResponseEntity<String> response = validationHandler.handleProductNotFoundException(customException);

//         // Assert: Verify that the response contains the expected message and status
//         assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//         assertEquals("Product not found", response.getBody());
//     }
// }
