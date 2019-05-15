/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.controller;

import com.example.demo.service.WatchService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author STEFAN94
 */

@RestController("/adminportal/watch")
public class ResourceController {
    
    @Autowired
    private WatchService watchService;
    
    @PostMapping("/adminportal/watch/removeList")
    public String removeList(
    @RequestBody ArrayList<String> watchIdList,
            Model model){
        
        for (String id : watchIdList) {
            String watchId = id.substring(8);
            watchService.removeOne(Long.parseLong(watchId));
        }
        
        return "delete success";
    }
}
