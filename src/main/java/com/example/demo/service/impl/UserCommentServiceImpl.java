/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service.impl;

import com.example.demo.domain.UserComment;
import com.example.demo.domain.Watch;
import com.example.demo.repository.UserCommentRepository;
import com.example.demo.service.UserCommentService;
import java.util.Optional;
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
public class UserCommentServiceImpl implements UserCommentService {
    
    @Autowired
    UserCommentRepository userCommentRepository;
    
    @Override
    public Set<UserComment> findByWatch(Watch watch) {
        return userCommentRepository.findByWatch(watch);
    }
    
    @Override
    public void save(UserComment userComment) {
        userCommentRepository.save(userComment);
    }
    
    @Override
    public Optional<UserComment> findOne(long id) {
        return userCommentRepository.findById(id);
    }
    
    @Override
    public void removeById(long id) {
        userCommentRepository.deleteById(id);
    }
    
}
