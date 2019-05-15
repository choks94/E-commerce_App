/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service.impl;

import com.example.demo.domain.CartItem;
import com.example.demo.domain.ShoppingCart;
import com.example.demo.repository.ShoppingCartRepository;
import com.example.demo.service.CartItemService;
import com.example.demo.service.ShoppingCartService;
import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author STEFAN94
 */

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService{

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    
    @Autowired
    private CartItemService cartItemService;
    
    @Override
    public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {
        BigDecimal cartTotal = new BigDecimal(0);
        
        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
        
        for(CartItem cartItem : cartItemList){
            if(cartItem.getWatch().getInStockNumber() > 0){
                cartItemService.updateCartItem(cartItem);
                cartTotal = cartTotal.add(cartItem.getSubtotal());
            }
        }
        
        shoppingCart.setGrandTotal(cartTotal);
        
        shoppingCartRepository.save(shoppingCart);
        
        return shoppingCart;
    }

    @Override
    public void clearShoppingCart(ShoppingCart shoppingCart) {

     
        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
        
        for (CartItem cartItem : cartItemList) {
            cartItem.setShoppingCart(null);
            cartItemService.save(cartItem);
        }
        
        shoppingCart.setGrandTotal(new BigDecimal(0));
        shoppingCartRepository.save(shoppingCart);
    }
    
}
