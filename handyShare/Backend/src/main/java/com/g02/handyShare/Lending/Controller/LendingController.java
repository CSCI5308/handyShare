package com.g02.handyShare.Lending.Controller;

import com.g02.handyShare.Lending.Entity.LentItem;
import com.g02.handyShare.Lending.Service.LentItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/all/lending")
@CrossOrigin(origins = "*")
public class LendingController {

    @Autowired
    private LentItemService lentItemService;

    @GetMapping("/items")
    public ResponseEntity<List<LentItem>> getAllLentItems() {
        try {
            List<LentItem> items = lentItemService.getAllLentItems();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/item")
    public ResponseEntity<LentItem> addLentItem(@RequestBody LentItem lentItem) {
        try {
            LentItem savedItem = lentItemService.addLentItem(lentItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
        } catch (Exception e) {
            e.printStackTrace(); // This will log the stack trace
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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

    @PutMapping("/item/{id}")
    public ResponseEntity<LentItem> updateLentItem(@PathVariable Long id, @RequestBody LentItem lentItemDetails) {
        try {
            LentItem updatedItem = lentItemService.updateLentItem(id, lentItemDetails);
            return ResponseEntity.ok(updatedItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
