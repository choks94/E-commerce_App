/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;

import com.example.demo.domain.ShippingAddress;
import com.example.demo.domain.UserShipping;

/**
 *
 * @author STEFAN94
 */
public interface ShippingAddressService {
    
    ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
}
