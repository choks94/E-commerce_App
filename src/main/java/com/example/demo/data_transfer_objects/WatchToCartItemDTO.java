/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.data_transfer_objects;

/**
 *
 * @author STEFAN94
 */
public class WatchToCartItemDTO {

    private long id;
    private WatchDTO watch;
    private CartItemDTO cartItem;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WatchDTO getWatch() {
        return watch;
    }

    public void setWatch(WatchDTO watch) {
        this.watch = watch;
    }

    public CartItemDTO getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItemDTO cartItem) {
        this.cartItem = cartItem;
    }
}
