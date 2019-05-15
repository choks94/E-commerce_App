/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.data_transfer_objects;

import java.math.BigDecimal;
import java.util.List;


/**
 *
 * @author STEFAN94
 */
public class CartItemDTO {

    private Long id;
    private int qty;
    private BigDecimal subtotal;
    private WatchDTO watch;
    private List<WatchToCartItemDTO> watchToCartItemList;
    private ShoppingCartDTO shoppingCart;
    private OrderDTO order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public WatchDTO getWatch() {
        return watch;
    }

    public void setWatch(WatchDTO watch) {
        this.watch = watch;
    }

    public List<WatchToCartItemDTO> getWatchToCartItemList() {
        return watchToCartItemList;
    }

    public void setWatchToCartItemList(List<WatchToCartItemDTO> watchToCartItemList) {
        this.watchToCartItemList = watchToCartItemList;
    }

    public ShoppingCartDTO getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCartDTO shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

}
