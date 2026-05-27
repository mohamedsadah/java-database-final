package com.project.code.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.code.Model.Customer;
import com.project.code.Model.Review;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.ReviewRepository;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ReviewRepository reviewRepository;


    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(@PathVariable Long storeId, @PathVariable Long productId){
        Map<String, Object> reviewsMap = new HashMap<>();
        List<Map<String, String>> filteredReviews = new ArrayList<>();

        List<Review> reviews = reviewRepository
            .findByStoreIdAndProductId(
                storeId.longValue(), 
                productId.longValue()
            );

        for(Review review: reviews){
            Map<String, String> rev = new HashMap<>();
            Customer customer = customerRepository.findById(review.getCustomerId());

            rev.put("comment", review.getComment());
            rev.put("rating", Integer.toString(review.getRating()));
            rev.put("customer", 
                 customer != null ? 
                 customer.getName() 
                : "unknown"
            );
            filteredReviews.add(rev);
        }

        reviewsMap.put("reviews", filteredReviews);

        return reviewsMap;

    }

    
   
}
