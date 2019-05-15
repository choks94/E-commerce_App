package com.example.demo.controller;

import com.example.demo.config.paypal.PaypalPaymentIntent;
import com.example.demo.config.paypal.PaypalPaymentMethod;
import com.example.demo.domain.BillingAddress;
import com.example.demo.domain.CartItem;
import com.example.demo.domain.Order;
import com.example.demo.domain.ShippingAddress;
import com.example.demo.domain.ShippingOrder;
import com.example.demo.domain.ShoppingCart;
import com.example.demo.service.PaypalService;
import com.example.demo.service.UserService;
import com.example.demo.utility.URLUtility;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.domain.User;
import com.example.demo.service.CartItemService;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ShoppingCartService;
import com.example.demo.utility.ChechGUser;
import com.example.demo.utility.MailConstructor;
import com.paypal.api.payments.Address;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PotentialPayerInfo;
import com.paypal.base.rest.PayPalRESTException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/")
public class PaymentController {

    public static final String PAYPAL_SUCCESS_URL = "pay/success";
    public static final String PAYPAL_CANCEL_URL = "pay/cancel";
    private String shippingMet = "";
    private long shoppingCartId = 0;

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ChechGUser check;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }

//    @ModelAttribute("shippingMethod") String shippingMethod,
//            @ModelAttribute("payment") com.example.demo.domain.Payment usersPayment
    @RequestMapping(method = RequestMethod.POST, value = "pay")
    public String payPost(HttpServletRequest request,
            @RequestParam("total") String total,
            Authentication authentication,
            @ModelAttribute("shippingMethod") String shippingMethod,
            Model model
    ) {
        User user = null;
        try {
            user = check.getUserInfo(authentication);
        } catch (Exception e) {
            model.addAttribute("orderException", true);
            return "errorPageCustomer";
        }
        ShoppingCart sc = user.getShoppingCart();
        shoppingCartId = sc.getId();
        shippingMet = shippingMethod;
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + shippingMethod);
//        pym = usersPayment;
        String cancelUrl = URLUtility.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;
        String successUrl = URLUtility.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;
        Double tot = Double.parseDouble(total);

        try {
            Payment payment = paypalService.createPayment(
                    tot,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
//                    Payer payer = payment.getPayer();
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        model.addAttribute("error", true);
        return "forward:/checkOut?id=" + shoppingCartId;
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
    public String cancelPay(Model model) {
        model.addAttribute("error", true);
        return "forward:/checkOut?id=" + shoppingCartId;
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
    public String successPay(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            Authentication authentication,
            @ModelAttribute("shippingMethod") String shippingMethod,
            //            Principal principal, 
            Model model) {
        User user = null;
        try {
            user = check.getUserInfo(authentication);
        } catch (Exception e) {
            model.addAttribute("orderException", true);
            return "errorPageCustomer";
        }

//        User user = userService.findByUsername(principal.getName());
        ShoppingCart shoppingCart = user.getShoppingCart();
        List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
        model.addAttribute("cartItemList", cartItemList);

        try {
            System.out.println("========================================" + user);
            Payment payment = paypalService.executePayment(paymentId, payerId);
            System.out.println("##########################" + payment.getPayer());
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@" + payment);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!" + payment.getCart());
            Payer payer = payment.getPayer();
            PayerInfo payerInfo = payer.getPayerInfo();

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~" + payerInfo.getShippingAddress());
            BillingAddress billingAddress = new BillingAddress();
            ShippingAddress shippingAddress = new ShippingAddress();
            com.example.demo.domain.Payment pymnt = new com.example.demo.domain.Payment();
            try {

                billingAddress.setBillingAddressCity(payerInfo.getShippingAddress().getCity());
                billingAddress.setBillingAddressCountry(payerInfo.getShippingAddress().getState());
                billingAddress.setBillingAddressStreet(payerInfo.getShippingAddress().getLine1());
                billingAddress.setBillingAddressZipcode(payerInfo.getShippingAddress().getPostalCode());

                shippingAddress.setShippingAddressCity(payerInfo.getShippingAddress().getCity());
                shippingAddress.setShippingAddressCountry(payerInfo.getShippingAddress().getState());
                shippingAddress.setShippingAddressStreet(payerInfo.getShippingAddress().getLine1());
                shippingAddress.setShippingAddressZipcode(payerInfo.getShippingAddress().getPostalCode());
                pymnt.setHolderName(payerInfo.getFirstName() + " " + payerInfo.getLastName());

            } catch (Exception e) {
            }

            if (payment.getState().equals("approved")) {

//                paymentService.save(pym);
                RestTemplate restTemplate = new RestTemplate();
                double shippingPrice = 0;
                if (shippingMet.equals("groundShipping")) {
                    shippingPrice = restTemplate.getForObject(env.getProperty("shipping.rest-service") + "/getStandardPrice", Double.class);
                } else {
                    shippingPrice = restTemplate.getForObject(env.getProperty("shipping.rest-service") + "/getPremiumPrice", Double.class);
                }

                Order order = null;
                try {
                    order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, pymnt, shippingMethod, shippingPrice, user);
                } catch (Exception e) {
                    model.addAttribute("orderException", true);
                    return "errorPageCustomer";
                }

                ShippingOrder shippingOrder = new ShippingOrder();
                shippingOrder.setOrderId(order.getId());
                shippingOrder.setOrderStatus(order.getOrderStatus());
                shippingOrder.setOrderTotal(order.getOrderTotal());
                shippingOrder.setOrderDate(order.getOrderDate());
                shippingOrder.setShippingMethod(order.getShippingMethod());
                shippingOrder.setShippingPrice(order.getShippingPrice());
                shippingOrder.setId(0);

                shippingOrder = restTemplate.postForObject(env.getProperty("shipping.rest-service") + "/saveOrder", shippingOrder, ShippingOrder.class);

                order.setShippingOrderId((int) shippingOrder.getId());

                try {
                    orderService.save(order);
                    mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, shippingPrice, true, Locale.ENGLISH));
                    shoppingCartService.clearShoppingCart(shoppingCart);
                } catch (Exception e) {
                    model.addAttribute("orderException", true);
                    return "errorPageCustomer";
                }

//                LocalDate today = LocalDate.now();
//                LocalDate estimatedDeliveryDate;
//
//                if (shippingMet.equals("standardShipping")) {
//                    estimatedDeliveryDate = today.plusDays(5);
//                } else {
//                    estimatedDeliveryDate = today.plusDays(3);
//                }
                model.addAttribute("estimatedDeliveryDate", shippingOrder.getEstimatedDeliveryDate());
                return "orderSubmitted";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        model.addAttribute("error", true);
        return "forward:/checkOut?id=" + shoppingCartId;
    }

}
