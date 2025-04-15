package com.ecommerce.demo.service;

import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.model.WishList;
import com.ecommerce.demo.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListService {

    @Autowired
    WishListRepository wishListRepository;
    
    public void createWishList(WishList wishList) {
        wishListRepository.save(wishList);
    }

    public WishList getWishListByUserAndProduct(User user, Product product) {
        return wishListRepository.findByUserAndProduct(user, product).orElse(null);
    }

    public List<WishList> readWishList(Integer userId) {
        return wishListRepository.findAllByUserIdOrderByCreatedDateDesc(userId);
    }

    public void deleteWishList(WishList wishList) {
        wishListRepository.delete(wishList);
    }
}
