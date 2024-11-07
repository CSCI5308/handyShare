package com.g02.handyShare.Lending.Controller;

import com.g02.handyShare.Lending.Entity.LentItem;
import com.g02.handyShare.Lending.Service.LentItemService;
import com.g02.handyShare.Config.Firebase.FirebaseService;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import jakarta.validation.ConstraintViolationException;

@RestController
@RequestMapping("/api/v1/user/lending")
@CrossOrigin(origins = "*") // Adjust origins as needed for security
public class LendingController {

    @Autowired
    private LentItemService lentItemService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/items")
    public ResponseEntity<List<LentItem>> getAllLentItems() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            User currentUser = userRepository.findByEmail(userEmail);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            List<LentItem> items = lentItemService.getLentItemsByUser(currentUser);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/item")
    public ResponseEntity<?> addLentItem(
        @Valid @ModelAttribute LentItem lentItemDetails,
        BindingResult bindingResult,
        @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            User currentUser = userRepository.findByEmail(userEmail);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageName = firebaseService.uploadFile(imageFile, "lent_item_images");
                lentItemDetails.setImageName(imageName);
            }

            // Associate the lent item with the current user
            lentItemDetails.setUser(currentUser);

            LentItem newItem = lentItemService.addLentItem(lentItemDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
        } catch (ConstraintViolationException e) { // Updated exception class
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add Lent Item.");
        }
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<?> updateLentItem(
        @PathVariable Long id,
        @Valid @ModelAttribute LentItem lentItemDetails,
        BindingResult bindingResult,
        @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            User currentUser = userRepository.findByEmail(userEmail);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Ensure the lent item belongs to the current user
            LentItem existingItem = lentItemService.getLentItemsByUser(currentUser)
                .stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unauthorized to update this item"));

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageName = firebaseService.uploadFile(imageFile, "lent_item_images");
                lentItemDetails.setImageName(imageName);
            }

            // Ensure the lentItemDetails has the current user set
            lentItemDetails.setUser(currentUser);

            LentItem updatedItem = lentItemService.updateLentItem(id, lentItemDetails);
            return ResponseEntity.ok(updatedItem);
        } catch (ConstraintViolationException e) { // Updated exception class
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update Lent Item.");
        }
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<?> deleteLentItem(@PathVariable Long id) {
        try {
            lentItemService.deleteLentItem(id);
            return ResponseEntity.ok("Lent Item deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete Lent Item.");
        }
    }
}
