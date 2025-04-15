package com.ecommerce.demo.repository;

import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {
    Optional<WishList> findByUserAndProduct(User user, Product product);

    // get product added latest first
    List<WishList> findAllByUserOrderByCreatedDateDesc(User user);

    List<WishList> findAllByUserIdOrderByCreatedDateDesc(Integer userId);
}
