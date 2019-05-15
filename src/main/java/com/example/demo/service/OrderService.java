/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;

import com.example.demo.domain.BillingAddress;
import com.example.demo.domain.Order;
import com.example.demo.domain.Payment;
import com.example.demo.domain.ShippingAddress;
import com.example.demo.domain.ShoppingCart;
import com.example.demo.domain.User;
import java.util.Optional;

/**
 *
 * @author STEFAN94
 */
public interface OrderService {

    public Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, BillingAddress billingAddress, Payment payment, String shippingMethod,double shippingPrice, User user);
    
    public Optional<Order> findOne(long id);

    public void save(Order o);
    
}
