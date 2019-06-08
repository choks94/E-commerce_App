/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.service.impl;

import com.example.demo.domain.Watch;
import com.example.demo.repository.WatchRepository;
import com.example.demo.service.WatchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author STEFAN94
 */
@Service
@Transactional
public class WatchServiceImpl implements WatchService {

    @Autowired
    private WatchRepository watchRepository;

    @Override
    public Watch save(Watch watch) {
        return watchRepository.save(watch);
    }

    public List<Watch> findAll() {
        return (List<Watch>) watchRepository.findAll();
    }

    @Override
    public Optional<Watch> findOne(Long id) {
        return watchRepository.findById(id);
    }

    @Override
    public Page<Watch> findPaginated(Pageable pageable) {

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Watch> list;
        List<Watch> watches = findAll();
        List<Watch> activeWatches = new ArrayList<>();
        for (Watch watch : watches) {
            if (watch.isActive()) {
                activeWatches.add(watch);
            }
        }
        if (activeWatches.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, activeWatches.size());
            list = activeWatches.subList(startItem, toIndex);
        }

        Page<Watch> watchPage
                = new PageImpl<Watch>(list, PageRequest.of(currentPage, pageSize), activeWatches.size());

        return watchPage;
    }

    @Override
    public void removeOne(long parseLong) {
        watchRepository.deleteById(parseLong);
    }

    @Override
    public Page<Watch> findByCategory(Pageable pageable, String category) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Watch> list;
        List<Watch> watches = watchRepository.findByCategory(category);
        List<Watch> activeWatches = new ArrayList<>();
        for (Watch watch : watches) {
            if (watch.isActive()) {
                activeWatches.add(watch);
            }
        }
        if (activeWatches.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, activeWatches.size());
            list = activeWatches.subList(startItem, toIndex);
        }

        Page<Watch> watchPage
                = new PageImpl<Watch>(list, PageRequest.of(currentPage, pageSize), activeWatches.size());

        return watchPage;
    }

    @Override
    public Page<Watch> searchWatch(Pageable pageable, String keyword) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Watch> list;
        List<Watch> watches = watchRepository.findByNameContaining(keyword);
        List<Watch> activeWatches = new ArrayList<>();
        for (Watch watch : watches) {
            if (watch.isActive()) {
                activeWatches.add(watch);
            }
        }
        if (activeWatches.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, activeWatches.size());
            list = activeWatches.subList(startItem, toIndex);
        }

        Page<Watch> watchPage
                = new PageImpl<Watch>(list, PageRequest.of(currentPage, pageSize), activeWatches.size());

        return watchPage;
    }

}
