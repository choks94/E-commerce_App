package com.example.demo.controller;

import com.example.demo.domain.CartItem;
import com.example.demo.domain.Order;
import com.example.demo.domain.ShippingOrder;
import com.example.demo.domain.User;
import com.example.demo.domain.UserBilling;
import com.example.demo.domain.UserComment;
import com.example.demo.domain.UserPayment;
import com.example.demo.domain.UserRating;
import com.example.demo.domain.UserShipping;
import com.example.demo.domain.Watch;
import com.example.demo.domain.security.PasswordResetToken;
import com.example.demo.domain.security.Role;
import com.example.demo.domain.security.UserRole;
import com.example.demo.service.CartItemService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserCommentService;
import com.example.demo.service.UserPaymentService;
import com.example.demo.service.UserRatingService;
import com.example.demo.service.UserService;
import com.example.demo.service.UserShippingService;
import com.example.demo.service.WatchService;
import com.example.demo.service.impl.UserSecurityService;
import com.example.demo.utility.ChechGUser;
import com.example.demo.utility.EmailConfig;
import com.example.demo.utility.MailConstructor;
import com.example.demo.utility.SecurityUtility;
import java.io.File;
import java.nio.file.Files;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class HomeController {

    private EmailConfig emailConfig = new EmailConfig();

    @Autowired
    private Environment env;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private UserService userService;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private WatchService watchService;

    @Autowired
    private UserPaymentService userPaymentService;

    @Autowired
    private UserShippingService userShippingService;

    @Autowired
    private UserCommentService userCommentService;

    @Autowired
    private UserRatingService userRatingService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ChechGUser check;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("classActiveLogin", true);
        return "myAccount";
    }

    @RequestMapping("/watchList")
    public String watchList(@RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            Model model,
            Authentication authentication) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(9);

        Page<Watch> watchPage = watchService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

//        if (authentication instanceof OAuth2Authentication) {
//            OAuth2Authentication auth = (OAuth2Authentication) authentication;
//            System.out.println("----------------------------" + auth.getDetails());
//            UsernamePasswordAuthenticationToken details = (UsernamePasswordAuthenticationToken) auth.getUserAuthentication();
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + details.getName());
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + details.getDetails());
//            String dets = details.getDetails().toString();
//            String email = dets.split("email=")[1].split(",")[0];
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + email);
//            String firstName = dets.split("given_name=")[1].split(",")[0];
//            String lastName = dets.split("family_name=")[1].split(",")[0];
//            System.out.println("First: "+firstName+" Last: "+lastName);
//            
//        } else {
//            System.out.println("++++++++++++++++++++++++++++" + authentication);
//            System.out.println("***************************" + authentication.getName());
//            User user = userService.findByUsername(authentication.getName());
//        }
////        List<Watch> watchList = watchService.findAll();
////        if (principal != null) {
////            String username = principal.getName();
////            User user = userService.findByUsername(username);
////            model.addAttribute("user", user);
////        }
        if (authentication != null) {
            User user = check.getUserInfo(authentication);
            if (user != null) {
                model.addAttribute("user", user);
            }
        }

        model.addAttribute("watchPage", watchPage);

        int totalPages = watchPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
//        model.addAttribute("watchList", watchList);
        return "watchList";
    }

    @PostMapping("/addComment")
    public String addCommentPost(
            @ModelAttribute("watch") Watch watch,
            @ModelAttribute("userComment") UserComment userComment,
            Authentication authentication,
            Model model) {

        User user = null;
        try {
            user = check.getUserInfo(authentication);
        } catch (Exception e) {
            model.addAttribute("commentException", true);
            return "errorPageCustomer";
        }
//        User user = userService.findByUsername(principal.getName());
        Optional<Watch> watchOpt = watchService.findOne(watch.getId());
        watch = watchOpt.get();
        userComment.setWatch(watch);
        userComment.setUser(user);

        Date date = new Date();
        userComment.setDate(date);

        try {
            userCommentService.save(userComment);
        } catch (Exception e) {
            model.addAttribute("commentException", true);
            return "errorPageCustomer";
        }

        model.addAttribute("commentSuccess", true);
        return "forward:/watchDetails?id=" + watch.getId();
    }

    @PostMapping("/addRate")
    public String addRatePost(@ModelAttribute("watch") Watch watch,
            @ModelAttribute("rating") String userRate,
            Model model,
            Authentication authentication
    ) {
        User user = null;
        try {
            user = check.getUserInfo(authentication);
        } catch (Exception e) {
            model.addAttribute("rateException", true);
            return "errorPageCustomer";
        }
//        User user = userService.findByUsername(principal.getName());
        try {
            Optional<Watch> w = watchService.findOne(watch.getId());
            watch = w.get();
        } catch (Exception e) {
            model.addAttribute("rateException", true);
            return "errorPageCustomer";
        }

        try {
            int rate = Integer.parseInt(userRate);
            UserRating userRating = new UserRating();
            userRating.setDate(new Date());
            userRating.setUser(user);
            userRating.setWatch(watch);
            userRating.setRating(rate);

            try {
                userRatingService.save(userRating);
            } catch (Exception e) {
                model.addAttribute("rateException", true);
                return "errorPageCustomer";
            }

        } catch (Exception e) {
            System.out.println("======================================================" + userRate);
        }

        model.addAttribute("rateSuccess", true);
        return "forward:/watchDetails?id=" + watch.getId();

    }

    @RequestMapping("/watchDetails")
    public String watchDeatils(
            @PathParam("id") Long id, Model model,
            Authentication authentication
    ) {

//        if (principal != null) {
//            String username = principal.getName();
//            User user = userService.findByUsername(username);
//            model.addAttribute("user", user);
//        }
        if (authentication != null) {
            User user = check.getUserInfo(authentication);
            if (user != null) {
                model.addAttribute("user", user);
            }
        }
        File f1 = new File("src/main/resources/static/image/watch/" + id + "_a.png");
        File f2 = new File("src/main/resources/static/image/watch/" + id + "_b.png");
        File f3 = new File("src/main/resources/static/image/watch/" + id + "_c.png");
        File f4 = new File("src/main/resources/static/image/watch/" + id + "_d.png");
        File f5 = new File("src/main/resources/static/image/watch/" + id + "_e.png");

        List<String> album = new ArrayList<>();
        try {
            if (!Files.readAllBytes(f1.toPath()).equals(null)) {
                album.add("/image/watch/" + id + "_a.png");
            }
            if (!Files.readAllBytes(f2.toPath()).equals(null)) {
                album.add("/image/watch/" + id + "_b.png");
            }
            if (!Files.readAllBytes(f3.toPath()).equals(null)) {
                album.add("/image/watch/" + id + "_c.png");
            }
            if (!Files.readAllBytes(f4.toPath()).equals(null)) {
                album.add("/image/watch/" + id + "_d.png");
            }
            if (!Files.readAllBytes(f5.toPath()).equals(null)) {
                album.add("/image/watch/" + id + "_e.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("album", album);
//        System.out.println("******************************************************" + album);

        Watch watch = new Watch();
        try {
            Optional<Watch> w = watchService.findOne(id);
            watch = w.get();
        } catch (Exception e) {
            model.addAttribute("WatchDetailsException", true);
            return "errorPageCustomer";
        }
//        Set<UserComment> userComments = userCommentService.findByWatch(watch);

        UserComment userComment = new UserComment();
        model.addAttribute("userComment", userComment);
//        model.addAttribute("userComments", userComments);
        model.addAttribute("watch", watch);

        List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Set<UserComment> userCommentList = userCommentService.findByWatch(watch);

        System.out.println("User comments: " + userCommentList);
        model.addAttribute("userCommentList", userCommentList);
        model.addAttribute("qtyList", qtyList);
        model.addAttribute("qty", 1);

        Set<UserRating> userRatingList = userRatingService.findByWatch(watch);
        int fives = 0;
        int fours = 0;
        int threes = 0;
        int twoos = 0;
        int ones = 0;
        int noOfRaters = 0;
        double avgRate = 0;
        for (UserRating userRating : userRatingList) {
            noOfRaters++;
            switch (userRating.getRating()) {
                case 1:
                    ones++;
                    break;
                case 2:
                    twoos++;
                    break;
                case 3:
                    threes++;
                    break;
                case 4:
                    fours++;
                    break;
                case 5:
                    fives++;
                    break;
                default:
                    break;
            }
        }
        double onesPc = 0;
        double twoosPc = 0;
        double threesPc = 0;
        double foursPc = 0;
        double fivesPc = 0;
//        System.out.println(""+ones+""+twoos+""+threes+""+fours+""+fives);
        if (noOfRaters != 0) {
            avgRate = (ones * 1 + twoos * 2 + threes * 3 + fours * 4 + fives * 5) / (double) noOfRaters;

            onesPc = ones / (double) noOfRaters;
            twoosPc = twoos / (double) noOfRaters;
            threesPc = threes / (double) noOfRaters;
            foursPc = fours / (double) noOfRaters;
            fivesPc = fives / (double) noOfRaters;
        }

        model.addAttribute("avg", avgRate);
        model.addAttribute("noOfRaters", noOfRaters);
        model.addAttribute("ones", onesPc);
        model.addAttribute("twoos", twoosPc);
        model.addAttribute("threes", threesPc);
        model.addAttribute("fours", foursPc);
        model.addAttribute("fives", fivesPc);

        return "watchDetails";
    }

    @RequestMapping("/forgetPassword")
    public String forgetPassword(
            HttpServletRequest request,
            @ModelAttribute("email") String email,
            Model model
    ) {

        model.addAttribute("classActiveForgetPassword", true);

        User user = userService.findByEmail(email);

        if (user == null) {
            model.addAttribute("emailNotExist", true);
            return "myAccount";
        }

        String password = SecurityUtility.randomPassword();

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        userService.save(user);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//        String url = appUrl + "/newUser?token=" + token;
//        JavaMailSenderImpl sender = new JavaMailSenderImpl();
//        sender.setHost(emailConfig.getHost());
//        sender.setPort(emailConfig.getPort());
//        sender.setUsername(emailConfig.getUsername());
//        sender.setPassword(emailConfig.getPassword());
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom("scolic994@gmail.com");
//        mailMessage.setTo(email);
//        mailMessage.setSubject("Password reset token");
//        mailMessage.setText("Dear User, use this password: \n" + password + "\nand copy it to apropriate field on this link:\n" + url + "\n in order to reset password");
//
//        System.out.println(emailConfig.getHost() + " " + emailConfig.getPort() + " " + emailConfig.getPassword() + " " + emailConfig.getUsername());
//        sender.send(mailMessage);

        SimpleMailMessage emailToSend = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);
        mailSender.send(emailToSend);
//        SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);
//        mailSender.send(newEmail);
        model.addAttribute("forgetPasswordEmailSent", "true");

        return "myAccount";
    }

    @RequestMapping("/myProfile")
    public String myProfile(Model model,
            Authentication authentication
    //            Principal principal 
    ) {
//        User user = userService.findByUsername(principal.getName());

        User user = check.getUserInfo(authentication);

        model.addAttribute("user", user);
        System.out.println("###########################" + user);
        model.addAttribute("userPayment", user.getUserPayment());
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$" + user.getUserPayment());
        model.addAttribute("userShipping", user.getUserShipping());
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$" + user.getUserShipping().getId());
        model.addAttribute("userBilling", user.getUserPayment().getUserBilling());
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$" + user.getUserPayment().getUserBilling());
        RestTemplate restTemplate = new RestTemplate();
        List<Order> orderList = user.getOrderList();
        try {
            for (Order o : orderList) {
                if (!o.getOrderStatus().equals("arrived")) {
                    ShippingOrder shippingOrder = restTemplate.getForObject(env.getProperty("shipping.rest-service") + "/getStatus/" + o.getId(), ShippingOrder.class);
                    if (shippingOrder != null && !shippingOrder.getOrderStatus().equals(o.getOrderStatus())) {
                        o.setOrderStatus(shippingOrder.getOrderStatus());
                        orderService.save(o);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Shipping service is unvailable currently");
            e.printStackTrace();
        }

        model.addAttribute("orderList", orderList);

//        UserShipping userShipping = new UserShipping();
//        model.addAttribute("userShipping", userShipping);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("classActiveEdit", true);

        return "myProfile";
    }

//    @RequestMapping("/listOfCreditCards")
//    public String listOfCreditCards(
//            Model model,
//            //            Principal principal,
//            Authentication authentication,
//            HttpServletRequest request) {
//
////        User user = userService.findByUsername(principal.getName());
//        User user = check.getUserInfo(authentication);
//        model.addAttribute("user", user);
//        model.addAttribute("userPayment", user.getUserPayment());
//        model.addAttribute("userShipping", user.getUserShipping());
//        model.addAttribute("orderList", user.getOrderList());
//
//        model.addAttribute("listOfCreditCards", true);
//        model.addAttribute("classActiveBilling", true);
//        model.addAttribute("listOfShippingAddresses", true);
//        return "myProfile";
//    }
//
//    @RequestMapping("/listOfShippingAddresses")
//    public String listOfShippingAddresses(
//            Model model,
//            Authentication authentication,
//            //            Principal principal,
//            HttpServletRequest request) {
//
////        User user = userService.findByUsername(principal.getName());
//        User user = check.getUserInfo(authentication);
//        model.addAttribute("user", user);
//        model.addAttribute("userPayment", user.getUserPayment());
//        model.addAttribute("userShipping", user.getUserShipping());
//        model.addAttribute("orderList", user.getOrderList());
//
//        model.addAttribute("listOfCreditCards", true);
//        model.addAttribute("classActiveShipping", true);
//        model.addAttribute("listOfShippingAddresses", true);
//
//        return "myProfile";
//    }
    @RequestMapping("/addNewCreditCard")
    public String addNewCreditCard(
            Model model,
            Authentication authentication
    //            Principal principal
    ) {
        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);

        model.addAttribute("addNewCreditCard", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

//        UserBilling userBilling = new UserBilling();
//        UserPayment userPayment = new UserPayment();
//
//        model.addAttribute("userBilling", userBilling);
//        model.addAttribute("userPayment", userPayment);
        model.addAttribute("userPayment", user.getUserPayment());
        model.addAttribute("userBilling", user.getUserPayment().getUserBilling());
        model.addAttribute("userShipping", user.getUserShipping());
        model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    @PostMapping("/addNewCreditCard")
    public String addNewCreditCardPost(
            @ModelAttribute("userPayment") UserPayment userPayment,
            @ModelAttribute("userBilling") UserBilling userBilling,
            Model model,
            Authentication authentication
    ) {

        User user = new User();
        try {
            user = check.getUserInfo(authentication);
        } catch (Exception e) {
            model.addAttribute("CreditCardException", true);
            return "errorPageCustomer";
        }
//        User user = userService.findByUsername(principal.getName());

        try {
            userService.updateUserBilling(userBilling, userPayment, user);
        } catch (Exception e) {
            model.addAttribute("CreditCardException", true);
            return "errorPageCustomer";
        }

        model.addAttribute("user", user);
        model.addAttribute("userPayment", user.getUserPayment());
        model.addAttribute("userShipping", user.getUserShipping());
        model.addAttribute("userBilling", user.getUserPayment().getUserBilling());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("orderList", user.getOrderList());
        model.addAttribute("creditCardSuccess", true);

        return "myProfile";
    }

    @PostMapping("/addNewShippingAddress")
    public String addNewShippingAddressPost(
            @ModelAttribute("userShipping") UserShipping userShipping,
            Model model,
            Authentication authentication
    ) {

        User user = new User();
        try {
            user = check.getUserInfo(authentication);
        } catch (Exception e) {
            model.addAttribute("shippingAddressException", true);
            return "errorPageCustomer";
        }
//        User user = userService.findByUsername(principal.getName());

        try {
            userService.updateUserShipping(userShipping, user);
        } catch (Exception e) {
            model.addAttribute("shippingAddressException", true);
            return "errorPageCustomer";
        }

        model.addAttribute("user", user);
        model.addAttribute("userPayment", user.getUserPayment());
        model.addAttribute("userShipping", user.getUserShipping());
        model.addAttribute("userBilling", user.getUserPayment().getUserBilling());
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("orderList", user.getOrderList());
        model.addAttribute("shippingAddressSuccess", true);

        return "myProfile";
    }

    @RequestMapping("/updateCreditCard")
    public String updateCreditCard(
            @ModelAttribute("id") long creditCardId,
            Authentication authentication,
            //            Principal principal,
            Model model) {

        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());
        Optional<UserPayment> userPaymenOpt = userPaymentService.findById(creditCardId);
        UserPayment userPayment = null;
        if (!userPaymenOpt.equals(null)) {
            userPayment = userPaymenOpt.get();
        }

        if (user.getId() != userPayment.getUser().getId()) {
            return "badRequest";
        } else {
            model.addAttribute("user", user);
            UserBilling userBilling = userPayment.getUserBilling();
            model.addAttribute("userPayment", userPayment);
            model.addAttribute("userBilling", userBilling);

            model.addAttribute("userPayment", user.getUserPayment());
            model.addAttribute("userShipping", user.getUserShipping());
            model.addAttribute("orderList", user.getOrderList());

            model.addAttribute("addNewCreditCard", true);
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddresses", true);

            return "myProfile";
        }
    }

    @RequestMapping("/updateUserShipping")
    public String updateUserShippingPost(
            @ModelAttribute("id") long userShippingId,
            Authentication authentication,
            //            Principal principal,
            Model model) {

        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());
        Optional<UserShipping> userShippingOpt = userShippingService.findById(userShippingId);
        UserShipping userShipping = userShippingOpt.get();

        if (user.getId() != userShipping.getUser().getId()) {
            return "badRequest";
        } else {
            model.addAttribute("user", user);

            model.addAttribute("userShipping", userShipping);

            model.addAttribute("userPayment", user.getUserPayment());
            model.addAttribute("userShipping", user.getUserShipping());
            model.addAttribute("orderList", user.getOrderList());

            model.addAttribute("addNewShippingAddress", true);
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("listOfCreditCards", true);

            return "myProfile";
        }
    }

    @PostMapping("/setDefaultPayment")
    public String setDefaultPaymentPost(
            @ModelAttribute("defaultUserPaymentId") long defaultPayment,
            Authentication authentication,
            //            Principal principal,
            Model model) {

        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());
        userService.setUserDefaultPayment(defaultPayment, user);

        model.addAttribute("user", user);
        model.addAttribute("userPayment", user.getUserPayment());
        model.addAttribute("userShipping", user.getUserShipping());
        model.addAttribute("orderList", user.getOrderList());

        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveBilling", true);
        model.addAttribute("listOfShippingAddresses", true);

        return "myProfile";
    }

    @PostMapping("/setDefaultShippingAddress")
    public String setDefaultShippingAddressPost(
            @ModelAttribute("defaultShippingAddressId") long defaultShippingId,
            Authentication authentication,
            //            Principal principal,
            Model model) {

        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());
        userService.setUserDefaultShipping(defaultShippingId, user);

        model.addAttribute("user", user);
        model.addAttribute("userPayment", user.getUserPayment());
        model.addAttribute("userShipping", user.getUserShipping());
        model.addAttribute("orderList", user.getOrderList());

        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfShippingAddresses", true);

        return "myProfile";
    }

    @RequestMapping("/removeCreditCard")
    private String removeCreditCard(
            @ModelAttribute("id") long creditCardId,
            Authentication authentication,
            //            Principal principal,
            Model model) {

        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());
        Optional<UserPayment> userPaymenOpt = userPaymentService.findById(creditCardId);
        UserPayment userPayment = null;
        if (!userPaymenOpt.equals(null)) {
            userPayment = userPaymenOpt.get();
        }

        if (user.getId() != userPayment.getUser().getId()) {
            return "badRequest";
        } else {
            model.addAttribute("user", user);
            userPaymentService.removeById(creditCardId);

            model.addAttribute("userPayment", user.getUserPayment());
            model.addAttribute("userShipping", user.getUserShipping());

            model.addAttribute("listOfCreditCards", true);
            model.addAttribute("classActiveBilling", true);
            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("orderList", user.getOrderList());

            return "myProfile";
        }
    }

    @RequestMapping("/removeUserShipping")
    private String removeUserShipping(
            @ModelAttribute("id") long userShippingId,
            Authentication authentication,
            //            Principal principal,
            Model model) {

        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());
        Optional<UserShipping> userShippingOpt = userShippingService.findById(userShippingId);
        UserShipping userShipping = userShippingOpt.get();

        if (user.getId() != userShipping.getUser().getId()) {
            return "badRequest";
        } else {
            model.addAttribute("user", user);
            userShippingService.removeById(userShippingId);

            model.addAttribute("userPayment", user.getUserPayment());
            model.addAttribute("userShipping", user.getUserShipping());
            model.addAttribute("classActiveShipping", true);
            model.addAttribute("listOfShippingAddresses", true);
            model.addAttribute("orderList", user.getOrderList());

            return "myProfile";
        }
    }

    @RequestMapping("/addNewShippingAddress")
    public String addNewShippingAddress(
            Model model,
            Authentication authentication
    //            Principal principal
    ) {

        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);

        model.addAttribute("addNewShippingAddress", true);
        model.addAttribute("classActiveShipping", true);
        model.addAttribute("listOfCreditCards", true);

        UserShipping userShipping = new UserShipping();

        model.addAttribute("userShipping", userShipping);

        model.addAttribute("userPayment", user.getUserPayment());
        model.addAttribute("userShipping", user.getUserShipping());
        model.addAttribute("orderList", user.getOrderList());
//        model.addAttribute("orderList", user.getOrderList());

        return "myProfile";
    }

    @PostMapping("/newUser")
    public String newUserPost(
            HttpServletRequest request,
            @ModelAttribute("email") String userEmail,
            @ModelAttribute("username") String username,
            Model model
    ) throws Exception {
        model.addAttribute("classActiveNewAccount", true);
        model.addAttribute("email", userEmail);
        model.addAttribute("username", username);

        if (userService.findByUsername(username) != null) {
            model.addAttribute("usernameExists", true);

            return "myAccount";
        }

        if (userService.findByEmail(userEmail) != null) {
            model.addAttribute("emailExists", true);

            return "myAccount";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(userEmail);

        String password = SecurityUtility.randomPassword();

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        Role role = new Role();
        role.setRoleId(1);
        role.setName("ROLE_USER");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, role));
        userService.createUser(user, userRoles);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//        String url = appUrl + "/newUser?token=" + token;
//        JavaMailSenderImpl sender = new JavaMailSenderImpl();
//        sender.setHost("smtp.mailtrap.io");
//        sender.setPort(2525);
//        sender.setUsername("e6ecbc5819b818");
//        sender.setPassword("d6f171a4ce5f3b");
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom("scolic994@gmail.com");
//        mailMessage.setTo(userEmail);
//        mailMessage.setSubject("Password token");
//        mailMessage.setText("Dear User, use this password: \n" + password + "\nand copy it to apropriate field on this link:\n" + url + "\nin order to set your personal informations");
//
//        System.out.println(emailConfig.getHost() + " " + emailConfig.getPort() + " " + emailConfig.getPassword() + " " + emailConfig.getUsername());
//        sender.send(mailMessage);

        SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);

        mailSender.send(email);
        model.addAttribute("emailSent", "true");
        model.addAttribute("orderList", user.getOrderList());

        return "myAccount";
    }

    @RequestMapping("/newUser")
    public String newUser(Locale locale,
            @RequestParam("token") String token, Model model
    ) {
        PasswordResetToken passToken = userService.getPasswordResetToken(token);

        if (passToken == null) {
            String message = "Invalid Token.";
            model.addAttribute("message", message);
            return "redirect:/badRequest";
        }

        User user = passToken.getUser();
        String username = user.getUsername();

        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        model.addAttribute("user", user);
//        model.addAttribute("orderList", user.getOrderList());

        model.addAttribute("classActiveEdit", true);
        return "myProfile";
    }

    @PostMapping("/updateUserInfo")
    public String updateUserInformationPost(
            @ModelAttribute("user") User user,
            @ModelAttribute("newPassword") String newPassword,
            Model model) throws Exception {

        User currentUser = null;
        try {
            Optional<User> currentUserOpt = userService.findById(user.getId());
            currentUser = currentUserOpt.get();
        } catch (Exception e) {
            model.addAttribute("UpdateUserInfoException", true);
            return "errorPageCustomer";
        }

        if (currentUser == null) {
            throw new Exception("User not found");
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            if (userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
                model.addAttribute("emailExists", true);
                return "myProfile";
            }
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            if (userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
                model.addAttribute("usernameExists", true);
                return "myProfile";
            }
        }
        if (user.getPassword() != null) {
            if (newPassword != null && !newPassword.isEmpty() && !newPassword.trim().equals("")) {
                BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
                String dbPassword = currentUser.getPassword();
                if (passwordEncoder.matches(user.getPassword(), dbPassword)) {
                    currentUser.setPassword(passwordEncoder.encode(newPassword));
                } else {
                    model.addAttribute("incorrectPassword", true);
                    return "myProfile";
                }
            }
        } else {
            BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
            String dbPassword = currentUser.getPassword();
            if (dbPassword == null || dbPassword.equals("")) {
                currentUser.setPassword(passwordEncoder.encode(newPassword));
            }
        }

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setUsername(user.getUsername());
        currentUser.setEmail(user.getEmail());

        try {
            userService.save(currentUser);
        } catch (Exception e) {
            model.addAttribute("UpdateUserInfoException", true);
            return "errorPageCustomer";
        }
        model.addAttribute("user", currentUser);
        model.addAttribute("classActiveEdit", true);
        model.addAttribute("listOfShippingAddresses", true);
        model.addAttribute("listOfCreditCards", true);
        model.addAttribute("orderList", user.getOrderList());
//        model.addAttribute("userPayment", user.getUserPayment());
//        model.addAttribute("userShipping", user.getUserShipping());
        UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        model.addAttribute("updateSuccess", true);
        
        return "forward:/myProfile";
    }

    @RequestMapping("/orderDetail")
    public String orderDetail(
            @RequestParam("id") long orderId,
            Authentication authentication,
            //            Principal principal,
            Model model) {

        User user = check.getUserInfo(authentication);
//        User user = userService.findByUsername(principal.getName());
        Optional<Order> orderOpt = orderService.findOne(orderId);
        Order order = orderOpt.get();

        if (order.getUser().getId() != user.getId()) {
            return "badRequest";
        } else {
            List<CartItem> cartItemList = cartItemService.findByOrder(order);

            model.addAttribute("user", user);
            model.addAttribute("cartItemList", cartItemList);
            model.addAttribute("order", order);

            model.addAttribute("userPayment", user.getUserPayment());
            model.addAttribute("userShipping", user.getUserShipping());
            RestTemplate restTemplate = new RestTemplate();
            List<Order> orderList = user.getOrderList();
            try {
                for (Order o : orderList) {
                    if (!o.getOrderStatus().equals("arrived")) {
                        ShippingOrder shippingOrder = restTemplate.getForObject(env.getProperty("shipping.rest-service") + "/getStatus/" + o.getShippingOrderId(), ShippingOrder.class);
                        if (shippingOrder != null && !shippingOrder.getOrderStatus().equals(o.getOrderStatus())) {
                            o.setOrderStatus(shippingOrder.getOrderStatus());
                            orderService.save(o);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Shipping service is unvailable currently");
                e.printStackTrace();
            }

            model.addAttribute("orderList", orderList);

            UserShipping userShipping = new UserShipping();
            model.addAttribute("userShipping", user.getUserShipping());
            model.addAttribute("userBilling", user.getUserPayment().getUserBilling());
            model.addAttribute("addNewShippingAddress", true);
            model.addAttribute("classActiveOrders", true);
            model.addAttribute("listOfCreditCards", true);
            model.addAttribute("displayOrderDetail", true);

        }
        return "myProfile";
    }

    @RequestMapping("/hoursAndLocation")
    public String hoursAndLocation() {
        return "hoursAndLocation";
    }

    @RequestMapping("/faq")
    public String faq() {
        return "faq.html";
    }

    @RequestMapping("/errorPageCustomer")
    public String errorPageCustomer() {
        return "errorPageCustomer";
    }

}
