/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.domain.Order;
import com.example.demo.domain.BillingAddress;
import com.example.demo.domain.CartItem;
import com.example.demo.domain.Payment;
import com.example.demo.domain.ShippingAddress;
import com.example.demo.domain.ShippingOrder;
import com.example.demo.domain.ShoppingCart;
import com.example.demo.domain.UserBilling;
import com.example.demo.domain.UserPayment;
import com.example.demo.domain.UserShipping;
import com.example.demo.service.BillingAddressService;
import com.example.demo.service.CartItemService;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ShippingAddressService;
import com.example.demo.service.ShoppingCartService;
import com.example.demo.service.UserPaymentService;
import com.example.demo.service.UserService;
import com.example.demo.service.UserShippingService;
import com.example.demo.utility.ChechGUser;
import com.example.demo.utility.MailConstructor;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author STEFAN94
 */
@Controller
public class CheckOutController {

    @Autowired
    Environment env;

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ShippingAddressService shippingAddressService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BillingAddressService billingAddressService;

    @Autowired
    private UserShippingService userShippingService;

    @Autowired
    private UserPaymentService userPaymentService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private ChechGUser check;

    private ShippingAddress shippingAddress = new ShippingAddress();
    private BillingAddress billingAddress = new BillingAddress();
    private Payment payment = new Payment();

    @RequestMapping("/checkOut")
    public String checkOut(
            @RequestParam("id") long cartId,
            @RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredFied,
            Model model,
            Authentication authentication
    //            Principal principal
    ) {

        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);

        if (cartId != user.getShoppingCart().getId()) {
            return "badRequest";
        }

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

        if (cartItemList.size() == 0) {
            model.addAttribute("emptyCart", true);
            return "forward:/shoppingCart/cart";
        }

        for (CartItem cartItem : cartItemList) {
            if (cartItem.getWatch().getInStockNumber() < cartItem.getQty()) {
                model.addAttribute("notEnoghtInStock", true);
                return "forward:/shoppingCart/cart";
            }
        }

        UserShipping userShipping = user.getUserShipping();
        UserPayment userPayment = user.getUserPayment();

        model.addAttribute("userShipping", userShipping);
        model.addAttribute("userPayment", userPayment);
//
//        if (userPaymentList.size() == 0) {
//            model.addAttribute("emptyPaymentList", true);
//        } else {
//            model.addAttribute("emptyPaymentList", false);
//        }

//        if (userShippingList.size() == 0) {
//            model.addAttribute("emptyShippingList", true);
//        } else {
//            model.addAttribute("emptyShippingList", false);
//        }
        ShoppingCart shoppingCart = user.getShoppingCart();

        if (userShipping.isUserShippingDefault()) {
            shippingAddressService.setByUserShipping(userShipping, shippingAddress);
        }

        if (userPayment.isDefaultPayment()) {
            paymentService.setByUserPayment(userPayment, payment);
            billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
        }

        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("payment", payment);
        model.addAttribute("billingAddress", billingAddress);
        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("shoppingCart", user.getShoppingCart());

        model.addAttribute("classActiveShipping", true);

        if (missingRequiredFied) {
            model.addAttribute("missingRequiredField", true);
        }

        RestTemplate restTemplate = new RestTemplate();
        double shippingStandardCost = 0d;
        double shippingPremiumCost = 0d;

        try {
            shippingStandardCost = restTemplate.getForObject(env.getProperty("shipping.rest-service") + "/getStandardPrice", Double.class);
            System.out.println("===================================" + shippingStandardCost);
            model.addAttribute("shippingStandardCost", shippingStandardCost);
            model.addAttribute("shippingServiceAvailable", true);
        } catch (Exception e) {
            model.addAttribute("shippingStandardCost", shippingStandardCost);
            model.addAttribute("shippingServiceAvailable", false);
        }

        try {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + env.getProperty("shipping.rest-service"));
            shippingPremiumCost = restTemplate.getForObject(env.getProperty("shipping.rest-service") + "/getPremiumPrice", Double.class);
            System.out.println("===================================" + shippingPremiumCost);
            model.addAttribute("shippingPremiumCost", shippingPremiumCost);
            model.addAttribute("shippingServiceAvailable", true);
        } catch (Exception e) {
            model.addAttribute("shippingPremiumCost", shippingPremiumCost);
            model.addAttribute("shippingServiceAvailable", false);
        }
//        model.addAttribute("shippingServiceAvailable", true);
        return "checkOut";
    }

    @PostMapping("/checkOut")
    public String checkOutPost(
            @ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
            @ModelAttribute("billingAddress") BillingAddress billingAddress,
            @ModelAttribute("payment") Payment payment,
            @ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
            @ModelAttribute("shippingMethod") String shippingMethod,
            Authentication authentication,
            Model model) {

        User user = new User();
        try {
            user = check.getUserInfo(authentication);
        } catch (Exception e) {
            model.addAttribute("orderException", true);
            System.out.println("1");
            return "errorPageCustomer";
            
        }
//        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        ShoppingCart shoppingCart = user.getShoppingCart();

        List<CartItem> cartItemList = new ArrayList<>();
        try {
            cartItemList = cartItemService.findByShoppingCart(shoppingCart);
        } catch (Exception e) {
            model.addAttribute("orderException", true);
            System.out.println("2");
            return "errorPageCustomer";
        }
        model.addAttribute("cartItemList", cartItemList);

        if (billingSameAsShipping.equals("true")) {
            billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
            billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
            billingAddress.setBillingAddressStreet(shippingAddress.getShippingAddressStreet());
            billingAddress.setBillingAddressZipcode(shippingAddress.getShippingAddressZipcode());
            billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
        }

        if (shippingAddress.getShippingAddressStreet().isEmpty()
                || shippingAddress.getShippingAddressCity().isEmpty()
                || shippingAddress.getShippingAddressName().isEmpty()
                || shippingAddress.getShippingAddressZipcode().isEmpty()
                //                || payment.getCardName().isEmpty()
                || payment.getCardNumber().isEmpty()
                || payment.getCvc() == 0
                || billingAddress.getBillingAddressCity().isEmpty()
                || billingAddress.getBillingAddressName().isEmpty()
                || billingAddress.getBillingAddressZipcode().isEmpty()
                || billingAddress.getBillingAddressStreet().isEmpty()
                || billingAddress.getBillingAddressCountry().isEmpty()) {
//            model.addAttribute("missingRequiredField", true);
//            return "checkOut?id=" + shoppingCart.getId();
            return "redirect:/checkOutid?=" + shoppingCart.getId() + "&missingRequiredField=true";
        }

        double shippingPrice = 0;
        RestTemplate restTemplate = new RestTemplate();
        try {
            if (shippingMethod.equals("standardShipping")) {
                shippingPrice = restTemplate.getForObject(env.getProperty("shipping.rest-service") + "/getStandardPrice", Double.class);
            } else {
                shippingPrice = restTemplate.getForObject(env.getProperty("shipping.rest-service") + "/getPremiumPrice", Double.class);
            }
        } catch (Exception e) {

        }

        Order order = null;
        try {
            order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, payment, shippingMethod, shippingPrice, user);
        } catch (Exception e) {
            model.addAttribute("orderException", true);
            System.out.println("3");
            return "errorPageCustomer";
        }
//        order.setShippingPrice(shippingPrice);

        //        LocalDate today = LocalDate.now();
        LocalDate estimatedDeliveryDate;
//        if (shippingMethod.equals("groundShipping")) {
//            estimatedDeliveryDate = today.plusDays(5);
//        } else {
//            estimatedDeliveryDate = today.plusDays(3);
//        }

        ShippingOrder shippingOrder = new ShippingOrder();
        shippingOrder.setOrderId(order.getId());
        shippingOrder.setOrderStatus(order.getOrderStatus());
        shippingOrder.setOrderTotal(order.getOrderTotal());
        shippingOrder.setOrderDate(order.getOrderDate());
        shippingOrder.setShippingMethod(order.getShippingMethod());
        shippingOrder.setShippingPrice(order.getShippingPrice());
        shippingOrder.setId(0);

        try {
            shippingOrder = restTemplate.postForObject(env.getProperty("shipping.rest-service") + "/saveOrder", shippingOrder, ShippingOrder.class);
        } catch (Exception e) {
        }

        order.setShippingOrderId((int) shippingOrder.getId());
        try {
            orderService.save(order);
            //mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, shippingPrice, false, Locale.ENGLISH));
            shoppingCartService.clearShoppingCart(shoppingCart);
        } catch (Exception e) {
            model.addAttribute("orderException", true);
            System.out.println("4");
            return "errorPageCustomer";
        }

        model.addAttribute("estimatedDeliveryDate", shippingOrder.getEstimatedDeliveryDate());

        return "orderSubmitted";
    }

    @RequestMapping("/setShippingAddress")
    public String setShippingAddress(
            @RequestParam("userShippingId") long userShippingId,
            Authentication authentication,
            //            Principal principal,
            Model model
    ) {
        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());
        Optional<UserShipping> userShippingOpt = userShippingService.findById(userShippingId);
        UserShipping userShipping = userShippingOpt.get();
        if (userShipping.getUser().getId() != user.getId()) {
            return "badRequest";
        } else {
            shippingAddressService.setByUserShipping(userShipping, shippingAddress);

            List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

//            BillingAddress billingAddress = new BillingAddress();
            model.addAttribute("shippingAddress", shippingAddress);
            model.addAttribute("payment", payment);
            model.addAttribute("billingAddress", billingAddress);
            model.addAttribute("cartItemList", cartItemList);
            model.addAttribute("shoppingCart", user.getShoppingCart());

            userShipping = user.getUserShipping();
            UserPayment userPayment = user.getUserPayment();

            model.addAttribute("userShippingList", userShipping);
            model.addAttribute("userPaymentList", userPayment);

            model.addAttribute("shippingAddress", shippingAddress);
            model.addAttribute("classActiveShipping", true);
//
//            if (userPaymentList.size() == 0) {
//                model.addAttribute("emptyPaymentList", true);
//            } else {
//                model.addAttribute("emptyPaymentList", false);
//            }
//
//            model.addAttribute("emptyShippingList", false);

            return "checkOut";
        }

    }

    @RequestMapping("/setPaymentMethod")
    public String setPaymentMethod(
            @RequestParam("userPaymentId") long userPaymentId,
            Authentication authentication,
            //            Principal principal,
            Model model) {

        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());
        Optional<UserPayment> userPaymentOpt = userPaymentService.findById(userPaymentId);
        UserPayment userPayment = userPaymentOpt.get();
        UserBilling userBilling = userPayment.getUserBilling();

        if (userPayment.getUser().getId() != user.getId()) {
            return "badRequest";
        } else {
            paymentService.setByUserPayment(userPayment, payment);
        }

        List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

        billingAddressService.setByUserBilling(userBilling, billingAddress);
        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("payment", payment);
        model.addAttribute("billingAddress", billingAddress);
        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("shoppingCart", user.getShoppingCart());

        UserShipping userShipping = user.getUserShipping();
        UserPayment userPaymentList = user.getUserPayment();

        model.addAttribute("userShippingList", userShipping);
        model.addAttribute("userPaymentList", userPayment);

        model.addAttribute("shippingAddress", shippingAddress);
        model.addAttribute("classActivePayment", true);

        model.addAttribute("emptyPaymentList", false);

//        if (userShippingList.size() == 0) {
//            model.addAttribute("emptyShippingList", true);
//        } else {
//            model.addAttribute("emptyShippingList", false);
//        }
        return "checkOut";
    }
}
