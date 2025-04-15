package com.ecommerce.demo.controller;

import com.ecommerce.demo.common.ApiResponse;
import com.ecommerce.demo.dto.product.ProductDto;
import com.ecommerce.demo.exceptions.ProductNotExistException;
import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.service.CategoryService;
import com.ecommerce.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> prducts = productService.listProducts();
        return new ResponseEntity<>(prducts, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductDto productDto) {
        Optional<Category> optionalCategory = categoryService.getCategoryById(productDto.getCategoryId());
        if(!optionalCategory.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "Category does not exists"), HttpStatus.BAD_REQUEST);
        }
        Category category = optionalCategory.get();
        productService.addProduct(productDto, category);
        return new ResponseEntity<>(new ApiResponse(true, "Product has been added"), HttpStatus.CREATED);

    }

    @PostMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Integer productId, @RequestBody ProductDto productDto) throws Exception {
        Optional<Category> optionalCategory = categoryService.getCategoryById(productDto.getCategoryId());
        if(!optionalCategory.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "category does not exists"), HttpStatus.BAD_REQUEST);
        }
        Category category = optionalCategory.get();
        productService.updateProduct(productId, productDto, category);
        return new ResponseEntity<>(new ApiResponse(true, "product has been updated"), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Integer productId) {
        try {
            Product product = productService.getProductById(productId);
            productService.deleteProduct(productId);
            return new ResponseEntity<>(new ApiResponse(true, "Product has been deleted"), HttpStatus.OK);
        }
        catch(ProductNotExistException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }


    }
}
