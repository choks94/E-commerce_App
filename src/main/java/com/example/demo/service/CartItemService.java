/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;

import com.example.demo.domain.CartItem;
import com.example.demo.domain.Order;
import com.example.demo.domain.ShoppingCart;
import com.example.demo.domain.User;
import com.example.demo.domain.Watch;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author STEFAN94
 */
public interface CartItemService {
    
    public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
    
    public CartItem updateCartItem(CartItem cartItem);

    public CartItem addWatchToCartItem(Watch watch, User user, int parseInt);

    public Optional<CartItem> findById(long cartItemId);

    public void removeCartItem(CartItem cartItem);

    public CartItem save(CartItem cartItem);

    public List<CartItem> findByOrder(Order order);
    
    
}
