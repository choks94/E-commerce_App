/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;

import com.example.demo.domain.ShoppingCart;

/**
 *
 * @author STEFAN94
 */
public interface ShoppingCartService {

    public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart);

    public void clearShoppingCart(ShoppingCart shoppingCart);
    
}
