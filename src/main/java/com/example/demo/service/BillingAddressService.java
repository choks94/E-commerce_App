/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;

import com.example.demo.domain.BillingAddress;
import com.example.demo.domain.UserBilling;

/**
 *
 * @author STEFAN94
 */
public interface BillingAddressService {
    
    BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
}
