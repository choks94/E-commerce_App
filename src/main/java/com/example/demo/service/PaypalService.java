/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;

import com.example.demo.config.paypal.PaypalPaymentIntent;
import com.example.demo.config.paypal.PaypalPaymentMethod;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

/**
 *
 * @author STEFAN94
 */
public interface PaypalService {

    public Payment createPayment(Double total, 
			String currency, 
			PaypalPaymentMethod method, 
			PaypalPaymentIntent intent, 
			String description, 
			String cancelUrl, 
			String successUrl) throws PayPalRESTException;

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
    
}
