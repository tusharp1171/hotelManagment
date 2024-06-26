package com.review.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.review.model.Review;
import com.review.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Optional<Review> findById(long id) {
        return reviewRepository.findById(id);
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public void deleteById(long id) {
        reviewRepository.deleteById(id);
    }
    
    public List<Review> findReviewsByCompanyId(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }
}