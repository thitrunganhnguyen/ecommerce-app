package com.ecommerce.demo.service;

import com.ecommerce.demo.exceptions.category.CategoryNotEmptyException;
import com.ecommerce.demo.exceptions.category.CategoryNotFoundException;
import com.ecommerce.demo.exceptions.category.DuplicateCategoryException;
import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.repository.CategoryRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    CategoryRepo categoryRepo;

    @InjectMocks
    CategoryService categoryService;

    @Test
    void shouldThrowDuplicateCategoryException_whenCategoryNameAlreadyExists() {
        // Arrange
        Category category = new Category();
        category.setCategoryName("books");

        when(categoryRepo.findByCategoryName("books")).thenReturn(Optional.of(category));

        assertThrows(DuplicateCategoryException.class,
                () -> categoryService.createCategory(category));

    }

    @Test
    void shouldCreateCategory_whenNameIsUnique() {
        // Arrange
        Category category = new Category();
        category.setCategoryName("toys");

        when(categoryRepo.findByCategoryName("toys")).thenReturn(Optional.empty());

        // Act
        categoryService.createCategory(category);

        // Assert
        verify(categoryRepo).save(category);
    }

    @Test
    void shouldThrowDuplicateCategoryException_whenRenamingToExistingCategoryName() {
        // Arrange
        Category existing = new Category();
        existing.setId(1);
        existing.setCategoryName("books");
        Category update = new Category();
        update.setCategoryName("games");

        Category alreadyTaken = new Category();
        alreadyTaken.setCategoryName("games");


        when(categoryRepo.findById(1)).thenReturn(Optional.of(existing));
        when(categoryRepo.findByCategoryName("games")).thenReturn(Optional.of(alreadyTaken));

        assertThrows(DuplicateCategoryException.class,
                () -> categoryService.editCategory(1, update));
    }

    @Test
    void shouldUpdateCategory_whenNameIsUniqueOrUnchanged() {
        // Arrange
        Category existing = new Category();
        existing.setId(1);
        existing.setCategoryName("books");
        existing.setDescription("Old desc");

        Category update = new Category();
        update.setCategoryName("toys");  // or "books" to test unchanged name
        update.setDescription("New desc");

        when(categoryRepo.findById(1)).thenReturn(Optional.of(existing));
        when(categoryRepo.findByCategoryName("toys")).thenReturn(Optional.empty());

        // Act
        categoryService.editCategory(1, update);

        // Assert
        // verify(categoryRepo).save(any(Category.class));
        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepo).save(captor.capture());

        Category saved = captor.getValue();
        assertEquals("toys", saved.getCategoryName());
        assertEquals("New desc", saved.getDescription());

    }

    @Test
    void shouldThrowCategoryNotFoundException_whenDeletingNonExistingCategory() {

        // Arrange
        int categoryId = 99;
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(categoryId));
    }


    @Test
    void shouldThrowCategoryNotEmptyException_whenCategoryHasProducts() {
        // Arrange
        Category category = new Category();
        category.setId(1);
        Product product = new Product(); // or use a mock Product
        Set<Product> products = new HashSet<>();
        products.add(product);

        category.setProducts(products);

        when(categoryRepo.findById(1)).thenReturn(Optional.of(category));

        // Act & Assert
        assertThrows(CategoryNotEmptyException.class,
                () -> categoryService.deleteCategory(1));

    }

    @Test
    void shouldDeleteCategory_whenNoProductsExist() {
        // Arrange
        Category category = new Category();
        category.setId(1);
        category.setProducts(new HashSet<>()); // empty set = no products

        when(categoryRepo.findById(1)).thenReturn(Optional.of(category));

        // Act
        categoryService.deleteCategory(1);

        // Assert
        verify(categoryRepo).deleteById(1);
    }

}
