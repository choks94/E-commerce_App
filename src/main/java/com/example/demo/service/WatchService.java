/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service;

import com.example.demo.domain.Watch;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author STEFAN94
 */
public interface WatchService {

    Watch save(Watch watch);

    List<Watch> findAll();

    Optional<Watch> findOne(Long id);

    public Page<Watch> findPaginated(Pageable pageable);

    public void removeOne(long parseLong);

    public Page<Watch> findByCategory(Pageable pageable,String category);

    public Page<Watch> searchWatch(Pageable pageable, String keyword);
}
