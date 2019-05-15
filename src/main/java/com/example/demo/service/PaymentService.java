/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;

import com.example.demo.domain.Payment;
import com.example.demo.domain.UserPayment;

/**
 *
 * @author STEFAN94
 */
public interface PaymentService {
    
    Payment setByUserPayment(UserPayment userPayment, Payment payment);

    public void save(Payment pym);
}
