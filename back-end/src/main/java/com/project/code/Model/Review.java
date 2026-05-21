package com.project.code.Model;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    @NotNull(message = "customerID cannot be null")
    private long customerId;

    @NotNull(message = "productID cannot be null")
    private long productId;

    @NotNull(message = "storeID cannot be null")
    private long storeId;

    @NotNull(message = "please add a rating")
    private int rating;

    private String comment;

    public Review() {
    }

    public Review(long customerId, long productId, long storeId, int rating, String comment) {
        this.customerId = customerId;
        this.productId = productId;
        this.storeId = storeId;
        this.rating = rating;
        this.comment = comment != null ? comment : "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
