/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service.impl;

import com.example.demo.domain.CartItem;
import com.example.demo.domain.Order;
import com.example.demo.domain.ShoppingCart;
import com.example.demo.domain.User;
import com.example.demo.domain.Watch;
import com.example.demo.domain.WatchToCartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.WatchToCartRepository;
import com.example.demo.service.CartItemService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author STEFAN94
 */
@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private WatchToCartRepository watchToCartRepository;

    @Override
    public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {

        return cartItemRepository.findByShoppingCart(shoppingCart);
    }

    @Override
    public CartItem updateCartItem(CartItem cartItem) {
        BigDecimal bigDecimal = new BigDecimal(cartItem.getWatch().getOurPrice()).multiply(new BigDecimal(cartItem.getQty()));

        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        cartItem.setSubtotal(bigDecimal);
        cartItemRepository.save(cartItem);

        return cartItem;
    }

    @Override
    public CartItem addWatchToCartItem(Watch watch, User user, int qty) {

        List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());

        for (CartItem cartItem : cartItemList) {
            if (watch.getId() == cartItem.getWatch().getId()) {
                cartItem.setQty(cartItem.getQty() + qty);
                cartItem.setSubtotal(new BigDecimal(watch.getOurPrice()).multiply(new BigDecimal(qty)));
                cartItemRepository.save(cartItem);

                return cartItem;
            }
        }

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(user.getShoppingCart());
        cartItem.setWatch(watch);

        cartItem.setQty(qty);

        cartItem.setSubtotal(new BigDecimal(watch.getOurPrice()).multiply(new BigDecimal(qty)));

        cartItem = cartItemRepository.save(cartItem);

        WatchToCartItem watchToCartItem = new WatchToCartItem();
        watchToCartItem.setWatch(watch);
        watchToCartItem.setCartItem(cartItem);
        watchToCartRepository.save(watchToCartItem);

        return cartItem;
    }

    @Override
    public Optional<CartItem> findById(long cartItemId) {
        return cartItemRepository.findById(cartItemId);
    }

    @Override
    public void removeCartItem(CartItem cartItem) {
        watchToCartRepository.deleteByCartItem(cartItem);
        cartItemRepository.delete(cartItem);
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public List<CartItem> findByOrder(Order order) {
        return cartItemRepository.findByOrder(order);
    }

}
