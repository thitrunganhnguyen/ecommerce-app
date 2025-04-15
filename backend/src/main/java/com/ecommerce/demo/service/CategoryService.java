package com.ecommerce.demo.service;

import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    public List<Category> listCategories() {
        return categoryRepo.findAll();
    }

    public void createCategory(Category category) {
        categoryRepo.save(category);
    }

    public void editCategory(int categoryId, Category updateCategory) {
//        Category category = categoryRepo.getById(categoryId);
//        category.setCategoryName(updateCategory.getCategoryName());
//        category.setDescription(updateCategory.getDescription());
//        category.setImageUrl(updateCategory.getImageUrl());
//        categoryRepo.save(category);
          updateCategory.setId(categoryId);
          categoryRepo.save(updateCategory);
    }

    public boolean findById(int categoryId) {
        return categoryRepo.findById(categoryId).isPresent();
    }

    public Object readCategory(String categoryName) {
        return categoryRepo.findByCategoryName(categoryName);
    }

    public Optional<Category> readCategory(Integer categoryId) {
        return categoryRepo.findById(categoryId);
    }

    public void deleteCategory(Integer categoryId) {
        categoryRepo.deleteById(categoryId);
    }
}
