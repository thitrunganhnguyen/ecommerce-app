package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.product.ProductDto;
import com.ecommerce.demo.exceptions.ProductNotExistException;
import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.repository.CategoryRepo;
import com.ecommerce.demo.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepo productRepo;

    public static Product getProductFromDto(ProductDto productDto, Category category) {
        Product product = new Product(productDto, category);
        return product;
    }

    public static ProductDto getDtoFromProduct(Product product) {
        ProductDto productDto = new ProductDto(product);
        return productDto;
    }

    public List<ProductDto> listProducts(){
        List<Product> allProducts = productRepo.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product product: allProducts) {
            ProductDto productDto = getDtoFromProduct(product);
            productDtos.add(productDto);
        }
        return productDtos;
    }

    public void addProduct(ProductDto productDto, Category category) {
        Product product = getProductFromDto(productDto, category);
        productRepo.save(product);
    }



    public void updateProduct(Integer productID, ProductDto productDto, Category category) {

        // Product product = getProductById(productID);

        // maps productDto (from frontend) into Product (database entity)
        Product product = getProductFromDto(productDto, category);

        // getProductFromDto() creates a new Product object, so it has no id yet.
        // By setting the id manually, you are telling JPA:
        // “This is not a new product; this is an update to an existing product with this ID.”
        // This is how save() knows to update instead of insert.
        product.setId(productID);

        productRepo.save(product);
    }

    public Product getProductById(Integer productId) {
        return productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotExistException("Product does not exists: " + productId));
    }

    public void deleteProduct(Integer productId) {
        productRepo.deleteById(productId);
    }
}
