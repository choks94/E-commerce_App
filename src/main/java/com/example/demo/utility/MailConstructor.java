/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.utility;

import com.example.demo.domain.Order;
import com.example.demo.domain.User;
import java.util.Locale;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author STEFAN94
 */
@Service
public class MailConstructor {

    @Autowired
    private Environment env;

    @Autowired
    private TemplateEngine templateEngine;

    public SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user, String password) {
        String url = contextPath + "/newUser?token=" + token;
        String message = "\n Please click on this link to verify your email and edit your personal informations. Your password is: \n" + password;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Watches Store - New User");
        email.setText(url + message);
        email.setFrom("scolic994@gmail.com");
        return email;

    }

    public MimeMessagePreparator constructOrderConfirmationEmail(User user, Order order,double shippingPrice,boolean paypal, Locale ENGLISH) {

        Context context = new Context();
        context.setVariable("order", order);
        context.setVariable("user", user);
        context.setVariable("shippingPrice", shippingPrice);
        context.setVariable("cardItemList", order.getCartItemList());
        context.setVariable("paypal", paypal);
        String text = templateEngine.process("orderConfirmation", context);

        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {

                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setTo(user.getEmail());
                email.setSubject("OrderConfirmation -  " + order.getId());
                email.setText(text, true);
                email.setFrom(new InternetAddress("njtdiplomski@gmail.com"));
            }
        };

        return messagePreparator;
    }

}
