// Student Number: ATE/1291/14

package com.shopwave.service;

import com.shopwave.dto.CreateProductRequest;
import com.shopwave.dto.ProductDTO;
import com.shopwave.mapper.ProductMapper;
import com.shopwave.model.Category;
import com.shopwave.model.Product;
import com.shopwave.repository.CategoryRepository;
import com.shopwave.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(categoryRepository, productRepository, new ProductMapper());
    }

    @Test
    void createProductShouldReturnProductDtoWhenCategoryExists() {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("Laptop")
                .description("Lightweight laptop")
                .price(new BigDecimal("1200.00"))
                .stock(8)
                .categoryId(1L)
                .build();

        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic items")
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId(10L);
            product.setCreatedAt(LocalDateTime.of(2026, 3, 22, 12, 0));
            return product;
        });

        ProductDTO result = productService.createProduct(request);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getName()).isEqualTo("Laptop");
        assertThat(result.getCategoryId()).isEqualTo(1L);
        assertThat(result.getCategoryName()).isEqualTo("Electronics");

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        assertThat(productCaptor.getValue().getCategory()).isEqualTo(category);
        assertThat(productCaptor.getValue().getStock()).isEqualTo(8);
    }

    @Test
    void createProductShouldThrowExceptionWhenCategoryDoesNotExist() {
        CreateProductRequest request = CreateProductRequest.builder()
                .name("Laptop")
                .description("Lightweight laptop")
                .price(new BigDecimal("1200.00"))
                .stock(8)
                .categoryId(99L)
                .build();

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Category not found with id: 99");

        verify(productRepository, never()).save(any(Product.class));
    }
}
