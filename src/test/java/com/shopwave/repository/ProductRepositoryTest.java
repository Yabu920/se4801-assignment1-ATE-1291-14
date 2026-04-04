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

@DataJpaTest(properties = "spring.sql.init.mode=never")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByNameContainingIgnoreCaseShouldReturnCorrectResults() {
        Category category = Objects.requireNonNull(categoryRepository.save(Category.builder()
                .name("Perfumes")
                .description("Perfume and fragrance products")
                .build()));

        productRepository.save(Objects.requireNonNull(Product.builder()
                .name("Rose Bloom")
                .description("Floral perfume with rose notes")
                .price(new BigDecimal("59.99"))
                .stock(15)
                .category(category)
                .build()));

        productRepository.save(Objects.requireNonNull(Product.builder()
                .name("Ocean Mist")
                .description("Fresh aquatic perfume")
                .price(new BigDecimal("64.99"))
                .stock(12)
                .category(category)
                .build()));

        productRepository.save(Objects.requireNonNull(Product.builder()
                .name("Velvet Oud")
                .description("Warm woody oud fragrance")
                .price(new BigDecimal("89.99"))
                .stock(8)
                .category(category)
                .build()));

        List<Product> results = productRepository.findByNameContainingIgnoreCase("mist");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Ocean Mist");
    }
}
