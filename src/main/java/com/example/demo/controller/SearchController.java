/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package com.example.demo.controller;

import com.example.demo.service.UserService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.domain.User;
import com.example.demo.domain.Watch;
import com.example.demo.service.WatchService;
import com.example.demo.utility.ChechGUser;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author STEFAN94
 */
@Controller
public class SearchController {

    @Autowired
    private UserService userService;

    @Autowired
    private WatchService watchService;

    @Autowired
    private ChechGUser check;

    @RequestMapping("/searchByCategory")
    public String searchByCategory(
            @RequestParam("category") String category,
            Model model,
            Authentication authentication,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size
    ) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        if (authentication != null) {
            User user = check.getUserInfo(authentication);
            if (user != null) {
                model.addAttribute("user", user);
            }
        }

        Page<Watch> watchPage = watchService.findByCategory(PageRequest.of(currentPage - 1, pageSize), category);
        String classActiveCategory = "active" + category;
        classActiveCategory = classActiveCategory.replaceAll("\\s+", "");
        model.addAttribute(classActiveCategory, true);


        model.addAttribute("watchPage", watchPage);

        int totalPages = watchPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        if (watchPage.isEmpty()) {
            model.addAttribute("emptyList", true);
            return "watchList";
        }

        model.addAttribute("watchPage", watchPage);

        return "watchList";
    }

    @RequestMapping("/searchWatch")
    public String searchWatch(
            @ModelAttribute("keyword") String keyword,
            Model model,
            Authentication authentication,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size
    ) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

         if (authentication != null) {
            User user = check.getUserInfo(authentication);
            if (user != null) {
                model.addAttribute("user", user);
            }
        }

        Page<Watch> watchPage = watchService.searchWatch(PageRequest.of(currentPage - 1, pageSize), keyword);

        model.addAttribute("watchPage", watchPage);

        int totalPages = watchPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        if (watchPage.isEmpty()) {
            model.addAttribute("emptyList", true);
            return "watchList";
        }

        model.addAttribute("watchPage", watchPage);

        return "watchList";
    }

}
