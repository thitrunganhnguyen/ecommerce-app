package com.ecommerce.demo.repository;

import com.ecommerce.demo.model.Cart;
import com.ecommerce.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Integer> {
    //newst cart firstly
    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);

    List<Cart> findAllByUserOrderByProduct_NameAsc(User user);

    Optional<Cart> findByIdAndUserId(Integer id, Integer userId);

    void deleteAllByUser(User user);


}
