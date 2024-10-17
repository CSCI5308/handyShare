package com.g02.handyShare.User.Controller;

import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.User.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")

//API starting with /all are accessible by all.
//API starting with /user are accessible by user and admin both.
//API starting with /admin are accessible by admin only.
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository repo;

    @PostMapping("/all/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
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
        User user = userService.findByToken(token);
        if (user != null) {
            user.set_email_verified(true);
            user.setVerificationToken(null); // Set the verification token to null
            userService.registerUser(user); // Save the updated user to the repository
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Invalid or expired token.");
    }
}
