/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;

import com.example.demo.domain.UserShipping;
import java.util.Optional;

/**
 *
 * @author STEFAN94
 */
public interface UserShippingService {
    
    public Optional<UserShipping> findById(long id);

    public void removeById(long userShippingId);
    
    
}
