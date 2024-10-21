package com.g02.handyShare.Category.Controller;

import com.g02.handyShare.Category.DTO.ProductIDRequest;
import com.g02.handyShare.Category.Entity.Trending;
import com.g02.handyShare.Category.Service.TrendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class TrendingController {

    @Autowired
    TrendingService service;


    @GetMapping("all/getTrendingByCategory")
    public ResponseEntity<?> getTrendingByCategory(@RequestParam String category) {
         return ResponseEntity.ok().body(service.fetchTrendingsByCategory(category));
    }

    @PostMapping("/addToTrending")   //This API should be accesible by ADMIN only
    public ResponseEntity<?> addToTrending(@RequestBody ProductIDRequest request) {
        return ResponseEntity.ok().body(service.addToTrending(request.getProduct_id()));
    }


}
