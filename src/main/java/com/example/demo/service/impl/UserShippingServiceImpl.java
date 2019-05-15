/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service.impl;

import com.example.demo.domain.UserShipping;
import com.example.demo.repository.UserShippingRepository;
import com.example.demo.service.UserShippingService;
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
public class UserShippingServiceImpl implements UserShippingService {

    @Autowired
    private UserShippingRepository userShippingRepository;

    @Override
    public Optional<UserShipping> findById(long id) {

        return userShippingRepository.findById(id);
    }

    @Override
    public void removeById(long userShippingId) {
        
        userShippingRepository.deleteById(userShippingId);
    }

}
