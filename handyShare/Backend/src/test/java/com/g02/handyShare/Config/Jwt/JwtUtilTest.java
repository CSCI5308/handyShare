// package com.g02.handyShare.Config.Jwt;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.security.Keys;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import java.util.Date;

// import static org.junit.jupiter.api.Assertions.*;

// class JwtUtilTest {

//     private JwtUtil jwtUtil;

//     @BeforeEach
//     void setUp() {
//         jwtUtil = new JwtUtil();
//     }

//     @Test
//     void testGenerateToken() {
//         // Arrange
//         String username = "test@example.com";

//         // Act
//         String token = jwtUtil.generateToken(username);

//         // Assert
//         assertNotNull(token);
//         assertTrue(token.startsWith("eyJ"));
//     }

//     @Test
//     void testExtractUsername() {
//         // Arrange
//         String username = "test@example.com";
//         String token = jwtUtil.generateToken(username);

//         // Act
//         String extractedUsername = jwtUtil.extractUsername(token);

//         // Assert
//         assertEquals(username, extractedUsername);
//     }

//     @Test
//     void testExtractExpiration() {
//         // Arrange
//         String token = jwtUtil.generateToken("test@example.com");

//         // Act
//         Date expirationDate = jwtUtil.extractExpiration(token);

//         // Assert
//         assertNotNull(expirationDate);
//         assertTrue(expirationDate.after(new Date())); // Token should not expire before it's generated
//     }

//     @Test
//     void testIsTokenExpired() {
//         // Arrange
//         String token = jwtUtil.generateToken("test@example.com");

//         // Act
//         boolean isExpired = jwtUtil.isTokenExpired(token);

//         // Assert
//         assertFalse(isExpired); // The token should not be expired immediately after creation
//     }

//     @Test
//     void testValidateToken_ValidToken() {
//         // Arrange
//         String token = jwtUtil.generateToken("test@example.com");

//         // Act
//         Boolean isValid = jwtUtil.validateToken(token);

//         // Assert
//         assertTrue(isValid); // The token should be valid if it's not expired
//     }

//     @Test
//     void testValidateToken_ExpiredToken() {
//         // Arrange
//         // Create a token with an expiration time in the past (expired 1 second ago)
//         String expiredToken = Jwts.builder()
//                 .setSubject("test@example.com")
//                 .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expired token (1 second ago)
//                 .signWith(jwtUtil.getSigningKey())
//                 .compact();
    
//         // Act & Assert
//         // Expect the validateToken to return false due to expiration
//         assertFalse(jwtUtil.validateToken(expiredToken), "The token should be invalid since it's expired");
//     }
    

// }
