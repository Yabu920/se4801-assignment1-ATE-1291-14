// Student Number: ATE/1291/14

package com.shopwave.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createProductShouldReturnCreatedWhenCategoryExists() throws Exception {
        String requestBody = """
                {
                  "name": "Vanilla Silk",
                  "description": "Soft vanilla perfume",
                  "price": 69.99,
                  "stock": 18,
                  "categoryId": 1
                }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Vanilla Silk"))
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.categoryName").value("Perfumes"));
    }

    @Test
    void createProductShouldReturnBadRequestWhenCategoryDoesNotExist() throws Exception {
        String requestBody = """
                {
                  "name": "Vanilla Silk",
                  "description": "Soft vanilla perfume",
                  "price": 69.99,
                  "stock": 18,
                  "categoryId": 999
                }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Category not found with id: 999"))
                .andExpect(jsonPath("$.path").value("/api/products"));
    }

    @Test
    void getAllProductsShouldReturnSeededProducts() throws Exception {
        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Rose Bloom"))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void getProductByIdShouldReturnNotFoundErrorJson() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Product not found with id: 999"))
                .andExpect(jsonPath("$.path").value("/api/products/999"));
    }

    @Test
    void searchProductsShouldReturnMatchingSeededProduct() throws Exception {
        mockMvc.perform(get("/api/products/search")
                        .param("keyword", "mist")
                        .param("maxPrice", "70"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Ocean Mist"));
    }

    @Test
    void updateStockShouldReturnUpdatedProduct() throws Exception {
        String requestBody = """
                {
                  "delta": 5
                }
                """;

        mockMvc.perform(patch("/api/products/1/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.stock").value(20))
                .andExpect(jsonPath("$.name").value("Rose Bloom"));
    }
}
