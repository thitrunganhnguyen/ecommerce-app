package com.ecommerce.demo.controller;

import com.ecommerce.demo.exceptions.category.CategoryNotEmptyException;
import com.ecommerce.demo.exceptions.category.CategoryNotFoundException;
import com.ecommerce.demo.exceptions.category.DuplicateCategoryException;
import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CategoryService categoryService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnListOfCategories() throws Exception {
        // Arrange
        Category cat1 = new Category();
        cat1.setId(1);
        cat1.setCategoryName("books");

        Category cat2 = new Category();
        cat2.setId(2);
        cat2.setCategoryName("electronics");

        List<Category> categories = List.of(cat1, cat2);

        when(categoryService.listCategories()).thenReturn(categories);

        // Act & Assert
        mockMvc.perform(get("/category/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].categoryName").value("books"))
                .andExpect(jsonPath("$[1].categoryName").value("electronics"));
    }

    @Test
    void shouldCreateCategoryAndReturnSuccessMessage() throws Exception {
        // Arrange
        Category category = new Category();
        category.setCategoryName("toys");
        category.setDescription("Fun stuff");

        String categoryJson = objectMapper.writeValueAsString(category);

        // Act and Assert
        mockMvc.perform(post("/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("A new category created"));
    }

    @Test
    void shouldReturnConflict_whenCategoryNameAlreadyExists() throws Exception {
        // Arrange
        Category category = new Category();
        category.setCategoryName("books");

        String categoryJson = objectMapper.writeValueAsString(category);

        // Mock service to throw DuplicateCategoryException
        doThrow(new DuplicateCategoryException("Category already exists"))
                .when(categoryService).createCategory(any(Category.class));

        // ACt and Assert
        mockMvc.perform(post("/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isConflict())
                .andExpect(content().string("Category already exists"));

    }

    @Test
    void shouldUpdateCategoryAndReturnSucessMessage() throws Exception {
        // Arrange
        Category category = new Category();
        category.setCategoryName("Updated books");
        category.setDescription("Update description");

        String categoryJson = objectMapper.writeValueAsString(category);

        // Act & Assert
        mockMvc.perform(put("/category/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Category has been updated"));

        // Optional: verify that service was called
        verify(categoryService).editCategory(eq(1), any(Category.class));
    }

    @Test
    void shouldReturnNotFound_whenCategoryIdDoesNotExist() throws Exception {
        // Arrange
        Category category = new Category();
        category.setCategoryName("New Name");
        category.setDescription("Updated description");

        String categoryJson = objectMapper.writeValueAsString(category);


        // Mock service to throw CategoryNotFoundException
        doThrow(new CategoryNotFoundException("Category not found"))
                .when(categoryService).editCategory(eq(1), any(Category.class));

        // ACt and Assert
        mockMvc.perform(put("/category/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Category not found"));

    }

    @Test
    void shouldDeleteCategoryAndReturnSuccessMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/category/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Category has been deleted"));

        // Optional: verify that service was called
        verify(categoryService).deleteCategory(1);
    }

    @Test
    void shouldReturnNotFound_whenDeletingNotExistingCategory() throws Exception {
        // Arrange
        doThrow(new CategoryNotFoundException("Category not found"))
                .when(categoryService).deleteCategory(1);
        // Act & Assert
        mockMvc.perform(delete("/category/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Category not found"));

    }

    @Test
    void shouldReturnConflict_whenDeletingCategoryWithProducts() throws Exception {
        // Arrange
        doThrow(new CategoryNotEmptyException("Category has products"))
                .when(categoryService).deleteCategory(1);
        // Act & Assert
        mockMvc.perform(delete("/category/delete/1"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Category has products"));
    }

}