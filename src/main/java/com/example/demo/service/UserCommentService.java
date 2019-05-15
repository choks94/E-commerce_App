/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;

import com.example.demo.domain.UserComment;
import com.example.demo.domain.Watch;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author STEFAN94
 */
public interface UserCommentService {
    
    Set<UserComment> findByWatch(Watch watch);

    public void save(UserComment userComment);

    public Optional<UserComment> findOne(long id);

    public void removeById(long id);
    
}
