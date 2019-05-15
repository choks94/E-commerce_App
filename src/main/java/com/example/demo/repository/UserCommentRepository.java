/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.repository;

import com.example.demo.domain.UserComment;
import com.example.demo.domain.Watch;
import java.util.Set;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author STEFAN94
 */

public interface UserCommentRepository extends CrudRepository<UserComment, Long>{
    
    Set<UserComment> findByWatch(Watch watch);
}
