// Student Number: ATE/1291/14

package com.shopwave.service;

import com.shopwave.dto.CreateProductRequest;
import com.shopwave.dto.ProductDTO;
import com.shopwave.exception.ProductNotFoundException;
import com.shopwave.mapper.ProductMapper;
import com.shopwave.model.Product;
import com.shopwave.repository.CategoryRepository;
import com.shopwave.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductDTO createProduct(CreateProductRequest request) {
        Long categoryId = Objects.requireNonNull(request.getCategoryId(), "Category ID must not be null");
        Product product = Objects.requireNonNull(productMapper.toEntity(
                request,
                categoryRepository.findById(categoryId)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Category not found with id: " + categoryId))
        ), "Mapped product must not be null");
        Product savedProduct = Objects.requireNonNull(productRepository.save(Objects.requireNonNull(product)),
                "Saved product must not be null");
        return productMapper.toDTO(savedProduct);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Pageable safePageable = Objects.requireNonNull(pageable, "Pageable must not be null");
        return productRepository.findAll(safePageable)
                .map(productMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Long productId = Objects.requireNonNull(id, "Product ID must not be null");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return productMapper.toDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String keyword, BigDecimal maxPrice) {
        List<Product> products;

        if (keyword != null && !keyword.trim().isEmpty() && maxPrice != null) {
            products = productRepository.findByNameContainingIgnoreCase(keyword);
            products = products.stream()
                    .filter(p -> p.getPrice().compareTo(maxPrice) <= 0)
                    .collect(Collectors.toList());
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(keyword);
        } else if (maxPrice != null) {
            products = productRepository.findByPriceLessThanEqual(maxPrice);
        } else {
            products = productRepository.findAll();
        }

        return products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO updateStock(Long id, int delta) {
        Long productId = Objects.requireNonNull(id, "Product ID must not be null");
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        int newStock = product.getStock() + delta;
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        product.setStock(newStock);
        Product savedProduct = Objects.requireNonNull(productRepository.save(Objects.requireNonNull(product)),
                "Saved product must not be null");
        return productMapper.toDTO(savedProduct);
    }
}
