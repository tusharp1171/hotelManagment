package com.review.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.review.model.Review;
import com.review.service.ReviewService;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@Validated
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.findAll();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable long id) {
        Optional<Review> review = reviewService.findById(id);
        if (review.isPresent()) {
            return new ResponseEntity<>(review.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(createResponse("Review not found for get by id", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody Review review) {
        Review savedReview = reviewService.save(review);
        return new ResponseEntity<>(createResponse("Review created successfully", HttpStatus.CREATED, savedReview), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable long id, @Valid @RequestBody Review review) {
        if (reviewService.findById(id).isPresent()) {
            review.setId(id);
            Review updatedReview = reviewService.save(review);
            return new ResponseEntity<>(createResponse("Review updated successfully", HttpStatus.OK, updatedReview), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(createResponse("Review not found for update", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable long id) {
        if (reviewService.findById(id).isPresent()) {
            reviewService.deleteById(id);
            return new ResponseEntity<>(createResponse("Review deleted successfully", HttpStatus.OK), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(createResponse("Review not found for delete", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byCompany/{companyId}")
    public ResponseEntity<?> getReviewsByCompanyId(@PathVariable Long companyId) {
        List<Review> reviews = reviewService.findReviewsByCompanyId(companyId);
        if (!reviews.isEmpty()) {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(createResponse("No reviews found for company id: " + companyId, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }
    
    private Map<String, Object> createResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("message", message);
        return response;
    }

    private Map<String, Object> createResponse(String message, HttpStatus status, Object data) {
        Map<String, Object> response = createResponse(message, status);
        response.put("data", data);
        return response;
    }
}