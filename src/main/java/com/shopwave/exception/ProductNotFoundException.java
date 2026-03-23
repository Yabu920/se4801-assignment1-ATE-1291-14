// Student Number: ATE/1291/14

package com.shopwave.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(Long productId) {
        super("Product not found with id: " + productId);
    }
}