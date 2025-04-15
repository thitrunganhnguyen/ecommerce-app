package com.ecommerce.demo.service;

import com.ecommerce.demo.exceptions.category.CategoryNotEmptyException;
import com.ecommerce.demo.exceptions.category.CategoryNotFoundException;
import com.ecommerce.demo.exceptions.category.DuplicateCategoryException;
import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.repository.CategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> listCategories() {
        return categoryRepo.findAll();
    }

    public void createCategory(Category category) {
        // Normalize the input: trim whitespace and convert to lowercase (optional)
        String normalizedName = normalizeName(category.getCategoryName());

        // Check if a category with the normalized name already exists
        if (getCategoryByName(normalizedName).isPresent()) {
            throw new DuplicateCategoryException("Category already exists");
        }

        // Set the normalized name back to the entity before saving
        category.setCategoryName(normalizedName);

        // Save the category
        categoryRepo.save(category);
    }

    public void editCategory(int categoryId, Category updateCategory) {
        Category existingCategory = categoryRepo.findById(categoryId)
                        .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        String newName = normalizeName(updateCategory.getCategoryName());

        // Only check for duplicates if the name is actually being changed
        if (!existingCategory.getCategoryName().equals(newName) &&
                getCategoryByName(newName).isPresent()) {
            throw new DuplicateCategoryException("Category name already exists");
        }

        existingCategory.setCategoryName(newName);
        existingCategory.setDescription(updateCategory.getDescription());
        existingCategory.setImageUrl(updateCategory.getImageUrl());

        categoryRepo.save(existingCategory);
    }

    public void deleteCategory(Integer categoryId) {
        Category existingCategory = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        if (existingCategory.getProducts() != null && !existingCategory.getProducts().isEmpty()) {
            throw new CategoryNotEmptyException("Cannot delete category because it still contains products.");
        }
        categoryRepo.deleteById(categoryId);
    }

    public Optional<Category> getCategoryByName(String categoryName) {
        return categoryRepo.findByCategoryName(categoryName);
    }

    public Optional<Category> getCategoryById(Integer categoryId) {
        return categoryRepo.findById(categoryId);
    }

    private String normalizeName(String name) {
        return name.trim().toLowerCase();
    }

}
