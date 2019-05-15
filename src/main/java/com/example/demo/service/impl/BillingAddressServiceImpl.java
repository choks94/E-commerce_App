/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service.impl;

import com.example.demo.domain.BillingAddress;
import com.example.demo.domain.UserBilling;
import com.example.demo.service.BillingAddressService;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 *
 * @author STEFAN94
 */
@Service
@Transactional
public class BillingAddressServiceImpl implements BillingAddressService{

    @Override
    public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress) {
        billingAddress.setBillingAddressName(userBilling.getUserBillingName());
        billingAddress.setBillingAddressCity(userBilling.getUserBillingCity());
        billingAddress.setBillingAddressStreet(userBilling.getUserBillingStreet());
        billingAddress.setBillingAddressZipcode(userBilling.getUserBillingZipcode());
        billingAddress.setBillingAddressCountry(userBilling.getUserBillingCountry());
        
        return billingAddress;
    }
    
}
