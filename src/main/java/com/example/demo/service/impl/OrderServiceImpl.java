/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service.impl;

import com.example.demo.domain.BillingAddress;
import com.example.demo.domain.Order;
import com.example.demo.domain.CartItem;
import com.example.demo.domain.Payment;
import com.example.demo.domain.ShippingAddress;
import com.example.demo.domain.ShoppingCart;
import com.example.demo.domain.User;
import com.example.demo.domain.Watch;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.CartItemService;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import java.util.Calendar;
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
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private PaymentService paymentService;

    @Override
    public synchronized Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, BillingAddress billingAddress, Payment payment, String shippingMethod,double shippingPrice, User user) {
        Order order = new Order();
        order.setOrderStatus("created");
        order.setPayment(payment);
        order.setShippingAddress(shippingAddress);
        order.setShippingMethod(shippingMethod);
        order.setBillingAddress(billingAddress);
        order.setShippingPrice(shippingPrice);

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

        for (CartItem cartItem : cartItemList) {
            Watch watch = cartItem.getWatch();
            cartItem.setOrder(order);
            watch.setInStockNumber(watch.getInStockNumber() - cartItem.getQty());
        }

        order.setCartItemList(cartItemList);
        order.setOrderDate(Calendar.getInstance().getTime());
        order.setOrderTotal(shoppingCart.getGrandTotal());
        shippingAddress.setOrder(order);
        billingAddress.setOrder(order);
        payment.setOrder(order);
        paymentService.save(payment);
        order.setBillingAddress(billingAddress);
        order.setUser(user);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + order);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + user);

        order = orderRepository.save(order);
        return order;
    }

    @Override
    public Optional<Order> findOne(long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void save(Order o) {
        orderRepository.save(o);
    }

}
