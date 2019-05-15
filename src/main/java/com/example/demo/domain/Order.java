/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author STEFAN94
 */
@Entity
@Table(name = "user_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date orderDate;
    private Date ShippinDate;
    private String shippingMethod;
    private String orderStatus;
    private double shippingPrice;
    private BigDecimal orderTotal;
    private int shippingOrderId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<CartItem> cartItemList;
    
    @OneToOne(cascade = CascadeType.ALL)
    private ShippingAddress shippingAddress;
    
    @OneToOne(cascade = CascadeType.ALL)
    private BillingAddress billingAddress;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Payment payment;
    
    @ManyToOne
    private User user;

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

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
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
