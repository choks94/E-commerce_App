/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.controller;

import com.example.demo.domain.CartItem;
import com.example.demo.domain.ShoppingCart;
import com.example.demo.domain.User;
import com.example.demo.domain.Watch;
import com.example.demo.service.CartItemService;
import com.example.demo.service.ShoppingCartService;
import com.example.demo.service.UserService;
import com.example.demo.service.WatchService;
import com.example.demo.utility.ChechGUser;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author STEFAN94
 */
@Component
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private WatchService watchService;

    @Autowired
    private ChechGUser check;

    @RequestMapping("/cart")
    public String shoppingCart(
            Model model,
            Authentication authentication
    //            Principal principal
    ) {

        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());
        ShoppingCart shoppingCart = user.getShoppingCart();

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

        shoppingCartService.updateShoppingCart(shoppingCart);

        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("shoppingCart", shoppingCart);
        model.addAttribute("user", user);

        return "shoppingCart";
    }

    @RequestMapping("/addItem")
    public String addItem(
            @ModelAttribute("watch") Watch watch,
            @ModelAttribute("qty") String qty,
            Model model,
            Authentication authentication
    ) {
        User user = null;
        try {
            user = check.getUserInfo(authentication);
        } catch (Exception e) {
            model.addAttribute("addWatchException", true);
            return "errorPageCustomer";
        }
//        User user = userService.findByUsername(principal.getName());
        try {
            Optional<Watch> watchOpt = watchService.findOne(watch.getId());
            watch = watchOpt.get();
        } catch (Exception e) {
            model.addAttribute("addWatchException", true);
            return "errorPageCustomer";
        }

        int quantity = 0;

        try {
            quantity = Integer.parseInt(qty);
        } catch (Exception e) {
            quantity = 1;
        }

        model.addAttribute("user", user);
        if (quantity > watch.getInStockNumber()) {
            System.out.println("QTY: " + quantity);
            System.out.println("In stock number: " + watch.getInStockNumber());

            model.addAttribute("notEnoughtInStock", true);
            return "forward:/watchDetails?id=" + watch.getId(); 
        }

        try {
            cartItemService.addWatchToCartItem(watch, user, quantity);
        } catch (Exception e) {
            model.addAttribute("addWatchException", true);
            return "errorPageCustomer";
        }
        model.addAttribute("addWatchSuccess", true);

        return "forward:/watchDetails?id=" + watch.getId();

    }

    @RequestMapping("/updateCartItem")
    public String updateCartItem(
            @ModelAttribute("id") long cartItemId,
            @ModelAttribute("qty") int qty,
            Authentication authentication,
            //            Principal principal,
            Model model) {

        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());
        Optional<CartItem> cartItemOpt = cartItemService.findById(cartItemId);
        CartItem cartItem = cartItemOpt.get();
        cartItem.setQty(qty);
        cartItemService.updateCartItem(cartItem);
        model.addAttribute("user", user);

        return "forward:/shoppingCart/cart";

    }

    @RequestMapping("/removeItem")
    public String removeItem(@RequestParam("id") long id,
            Model model,
            Authentication authentication
    //            Principal principal
    ) {
        Optional<CartItem> cartItemOpt = cartItemService.findById(id);
        CartItem cartItem = cartItemOpt.get();
        cartItemService.removeCartItem(cartItem);

        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "forward:/shoppingCart/cart";
    }
}
