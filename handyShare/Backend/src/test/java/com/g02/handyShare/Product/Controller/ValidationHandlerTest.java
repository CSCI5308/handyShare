package com.g02.handyShare.Product.Controller;

import com.g02.handyShare.Product.Service.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationHandlerTest {

    private ValidationHandler validationHandler;

    @BeforeEach
    void setUp() {
        validationHandler = new ValidationHandler();
    }

    @Test
    void handleValidationExceptions_shouldReturnBadRequestWithFieldErrors() {
        // Arrange
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(new FieldError("testObject", "field1", "Field1 is invalid"));
        bindingResult.addError(new FieldError("testObject", "field2", "Field2 cannot be null"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<Map<String, String>> response = validationHandler.handleValidationExceptions(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Field1 is invalid", errors.get("field1"));
        assertEquals("Field2 cannot be null", errors.get("field2"));
    }

    @Test
    void handleProductNotFoundException_shouldReturnNotFoundWithMessage() {
        // Arrange
        String errorMessage = "Product not found";
        CustomException customException = new CustomException(errorMessage);

        // Act
        ResponseEntity<String> response = validationHandler.handleProductNotFoundException(customException);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody());
    }
}


