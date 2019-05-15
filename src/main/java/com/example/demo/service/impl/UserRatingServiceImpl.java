/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service.impl;

import com.example.demo.domain.UserRating;
import com.example.demo.domain.Watch;
import com.example.demo.repository.UserRatingRepository;
import com.example.demo.service.UserRatingService;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author STEFAN94
 */
@Service
@Transactional
public class UserRatingServiceImpl implements UserRatingService {

    @Autowired
    private UserRatingRepository userRatingRepository;

    @Override
    public Set<UserRating> findByWatch(Watch watch) {
        return userRatingRepository.findByWatch(watch);
    }

    @Override
    public void save(UserRating userRating) {
        Set<UserRating> userRatings = findByWatch(userRating.getWatch());
        for (UserRating ur : userRatings) {
            if (userRating.getUser().getId() == ur.getUser().getId()) {
                if (ur.getRating() != userRating.getRating()) {
                    ur.setRating(userRating.getRating());
                    userRatingRepository.save(ur);
                }
                return;
            }
        }
        userRatingRepository.save(userRating);
    }

}
