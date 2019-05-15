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
public class ShoppingCartDTO {

    private long id;
    private BigDecimal grandTotal;
    private List<CartItemDTO> cartItemList;
    private UserDTO user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandDecimal) {
        this.grandTotal = grandDecimal;
    }

    public List<CartItemDTO> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItemDTO> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
