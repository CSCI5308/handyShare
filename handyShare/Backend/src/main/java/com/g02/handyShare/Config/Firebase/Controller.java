package com.g02.handyShare.Config.Firebase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class Controller {
    private final FirebaseService firebaseService;

    @Autowired
    public Controller(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("api/v1/all/upload")
    public ResponseEntity<String> uploadFile(@RequestPart MultipartFile file,
                                             @RequestParam String path) {
        try {
            return firebaseService.uploadFile(file, path);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String path) {
        try {
            return firebaseService.downloadFile(path);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
