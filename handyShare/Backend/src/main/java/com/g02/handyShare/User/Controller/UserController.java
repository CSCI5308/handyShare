package com.g02.handyShare.User.Controller;

import com.g02.handyShare.Config.CustomUserDetailsService;
import com.g02.handyShare.Config.Jwt.JwtUtil;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.User.Service.UserService;
import com.g02.handyShare.User.DTO.LenderDetailsDTO;

import io.jsonwebtoken.lang.Arrays;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//API starting with /all are accessible by all.
//API starting with /user are accessible by user and admin both.
//API starting with /admin are accessible by admin only.

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

 @Autowired
    CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    UserRepository repo;


    

     @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/all/register")
    public ResponseEntity<String> registerUser( @RequestBody User user) {

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        } else {
            String result = userService.registerUser(user);

            if (result.contains("already registered")) {
                return ResponseEntity.badRequest().body(result);
            }

            return ResponseEntity.ok(result);
        }
    }


    

    @GetMapping("/admin/getUser") //Api accessible by the ADMIN only
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }
    @PostMapping("/perform_login")
    public String login(){
        return "loggedin";
    }

    @GetMapping("/all/verifyUser")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        String response = userService.verifyUser(token);
            if(response.contains("Successfully")){
                return ResponseEntity.ok().body("Email verified");
            }
       
        return ResponseEntity.badRequest().body("Invalid or expired token.");
    }

    @PostMapping("/all/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
            
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            User existingUser = repo.findByEmail(userDetails.getUsername());

            if (!existingUser.is_email_verified()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Verify email first"));
            }

            // Send back role information along with the JWT token
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("token", jwt);
            responseBody.put("role", existingUser.getRole()); // Adding role to the response
            responseBody.put("userId", String.valueOf(existingUser.getId()));

            return ResponseEntity.ok().body(responseBody);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body(Map.of("error", "Bad credentials!"));
        }
    }

    @GetMapping("/user/lender/{id}")
    public ResponseEntity<?> getLenderDetails(@PathVariable Long id) {
        try {
            LenderDetailsDTO lenderDetails = userService.getLenderDetails(id);
            return ResponseEntity.ok().body(lenderDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(e.getMessage());
        }
    }

}

