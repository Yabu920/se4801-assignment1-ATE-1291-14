// Student Number: ATE/1291/14

package com.shopwave.repository;

import com.shopwave.model.Category;
import com.shopwave.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByNameContainingIgnoreCaseShouldReturnCorrectResults() {
        Category category = Objects.requireNonNull(categoryRepository.save(Category.builder()
                .name("Electronics")
                .description("Electronic items")
                .build()));

        productRepository.save(Objects.requireNonNull(Product.builder()
                .name("Laptop")
                .description("Lightweight laptop")
                .price(new BigDecimal("1200.00"))
                .stock(8)
                .category(category)
                .build()));

        productRepository.save(Objects.requireNonNull(Product.builder()
                .name("Phone")
                .description("Smart phone")
                .price(new BigDecimal("800.00"))
                .stock(15)
                .category(category)
                .build()));

        productRepository.save(Objects.requireNonNull(Product.builder()
                .name("Desk")
                .description("Wooden desk")
                .price(new BigDecimal("300.00"))
                .stock(4)
                .category(category)
                .build()));

        List<Product> results = productRepository.findByNameContainingIgnoreCase("pho");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Phone");
    }
}
