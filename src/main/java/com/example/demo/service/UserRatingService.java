/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;

import com.example.demo.domain.UserRating;
import com.example.demo.domain.Watch;
import java.util.Set;

/**
 *
 * @author STEFAN94
 */
public interface UserRatingService {
    
    Set<UserRating> findByWatch(Watch watch);

    public void save(UserRating userRating);
    
}
