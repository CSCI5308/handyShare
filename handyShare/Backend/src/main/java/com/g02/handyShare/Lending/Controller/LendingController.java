package com.g02.handyShare.Lending.Controller;

import com.g02.handyShare.Lending.Entity.LentItem;
import com.g02.handyShare.Lending.Service.LentItemService;
import com.g02.handyShare.Config.Firebase.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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

    @GetMapping("/items")
    public ResponseEntity<List<LentItem>> getAllLentItems() {
        try {
            List<LentItem> items = lentItemService.getAllLentItems();
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
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageName = firebaseService.uploadFile(imageFile, "lent_item_images");
                lentItemDetails.setImageName(imageName);
            }
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
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageName = firebaseService.uploadFile(imageFile, "lent_item_images");
                lentItemDetails.setImageName(imageName);
            }
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
    public ResponseEntity<Void> deleteLentItem(@PathVariable Long id) {
        try {
            lentItemService.deleteLentItem(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
