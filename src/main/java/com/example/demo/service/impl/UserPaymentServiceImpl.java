/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service.impl;

import com.example.demo.domain.UserPayment;
import com.example.demo.repository.UserPaymentRepository;
import com.example.demo.service.UserPaymentService;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author STEFAN94
 */
@Service
@Transactional
public class UserPaymentServiceImpl implements UserPaymentService{

    @Autowired
    private UserPaymentRepository userPaymentRepository;
    
    @Override
    public Optional<UserPayment> findById(long id) {
      return userPaymentRepository.findById(id);
    }

    @Override
    public void removeById(long creditCardId) {
        userPaymentRepository.deleteById(creditCardId);
    }
    
    
}
