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
public class UserBillingDTO {

    private long id;
    private String userBillingName;
    private String userBillingStreet;
    private String userBillingCity;
    private String userBillingCountry;
    private String userBillingZipcode;
    private UserPaymentDTO userPayment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserBillingName() {
        return userBillingName;
    }

    public void setUserBillingName(String userBillingName) {
        this.userBillingName = userBillingName;
    }

    public String getUserBillingStreet() {
        return userBillingStreet;
    }

    public void setUserBillingStreet(String userBillingStreet) {
        this.userBillingStreet = userBillingStreet;
    }

    public String getUserBillingCity() {
        return userBillingCity;
    }

    public void setUserBillingCity(String userBillingCity) {
        this.userBillingCity = userBillingCity;
    }

    public String getUserBillingCountry() {
        return userBillingCountry;
    }

    public void setUserBillingCountry(String userBillingCountry) {
        this.userBillingCountry = userBillingCountry;
    }

    public String getUserBillingZipcode() {
        return userBillingZipcode;
    }

    public void setUserBillingZipcode(String userBillingZipcode) {
        this.userBillingZipcode = userBillingZipcode;
    }

    public UserPaymentDTO getUserPayment() {
        return userPayment;
    }

    public void setUserPayment(UserPaymentDTO userPayment) {
        this.userPayment = userPayment;
    }

}
