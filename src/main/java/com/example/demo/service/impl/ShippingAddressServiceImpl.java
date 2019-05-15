/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service.impl;

import com.example.demo.domain.ShippingAddress;
import com.example.demo.domain.UserShipping;
import com.example.demo.service.ShippingAddressService;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author STEFAN94
 */
@Service
@Transactional
public class ShippingAddressServiceImpl implements ShippingAddressService {

    @Override
    public ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress) {
        shippingAddress.setShippingAddressName(userShipping.getUserShippingName());
        shippingAddress.setShippingAddressCity(userShipping.getUserShippingCity());
        shippingAddress.setShippingAddressStreet(userShipping.getUserShippingStreet());
        shippingAddress.setShippingAddressZipcode(userShipping.getUserShippingZipcode());
        shippingAddress.setShippingAddressCountry(userShipping.getUserShippingCountry());
        
        return shippingAddress;

    }

}
