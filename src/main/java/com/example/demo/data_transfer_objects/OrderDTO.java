/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.data_transfer_objects;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author STEFAN94
 */
public class OrderDTO {

    private long id;
    private Date orderDate;
    private Date ShippinDate;
    private String shippingMethod;
    private String orderStatus;
    private double shippingPrice;
    private BigDecimal orderTotal;
    private int shippingOrderId;
    private List<CartItemDTO> cartItemList;
    private ShippingAddressDTO shippingAddress;
    private BillingAddressDTO billingAddress;
    private PaymentDTO payment;
    private UserDTO user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getShippinDate() {
        return ShippinDate;
    }

    public void setShippinDate(Date ShippinDate) {
        this.ShippinDate = ShippinDate;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public List<CartItemDTO> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItemDTO> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public ShippingAddressDTO getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddressDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public BillingAddressDTO getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddressDTO billingAddress) {
        this.billingAddress = billingAddress;
    }

    public double getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(double shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", orderDate=" + orderDate + ", ShippinDate=" + ShippinDate + ", shippingMethod=" + shippingMethod + ", orderStatus=" + orderStatus + ", shippingPrice=" + shippingPrice + ", orderTotal=" + orderTotal + ", cartItemList=" + cartItemList + ", shippingAddress=" + shippingAddress + ", billingAddress=" + billingAddress + ", payment=" + payment + ", user=" + user + '}';
    }

    public int getShippingOrderId() {
        return shippingOrderId;
    }

    public void setShippingOrderId(int shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
    }

    
}
