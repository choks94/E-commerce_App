/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.controller;

import com.example.demo.domain.UserComment;
import com.example.demo.domain.Watch;
import com.example.demo.service.UserCommentService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author STEFAN94
 */
@Controller
@RequestMapping("/adminportal")
public class AdminHomeController {
    
    @Autowired
    public UserCommentService userCommentService;

    @RequestMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
    
    @RequestMapping("/logout")
    public String logout(Model model){
        model.addAttribute("classActiveLogin", false);
        return "myAccount";
    }
    
    @RequestMapping("/errorPageAdmin")
    public String errorPageAdmin(){
        return "errorPageAdmin";
    }

}
