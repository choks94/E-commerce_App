/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.utility;

import org.springframework.stereotype.Service;
import com.example.demo.domain.User;
import com.example.demo.domain.security.Role;
import com.example.demo.domain.security.UserRole;
import com.example.demo.service.UserService;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 *
 * @author STEFAN94
 */
@Service
@Transactional
public class ChechGUser {

    @Autowired
    private UserService userService;

    public User getUserInfo(Authentication authentication) {

        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication auth = (OAuth2Authentication) authentication;
            System.out.println("----------------------------" + auth.getDetails());
            UsernamePasswordAuthenticationToken details = (UsernamePasswordAuthenticationToken) auth.getUserAuthentication();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + details.getName());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + details.getDetails());
            String dets = details.getDetails().toString();
            String email = dets.split("email=")[1].split(",")[0];
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + email);
            String firstName = dets.split("given_name=")[1].split(",")[0];
            String lastName = dets.split("family_name=")[1].split(",")[0];
            System.out.println("First: " + firstName + " Last: " + lastName);

            User user = userService.findByEmail(email);
            if (user != null) {
                return user;
            } else {
                user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setUsername(email);
                user.setUserType("GoogleUser");
//            user.setPassword(SecurityUtility.passwordEncoder().encode("coks"));
                user.setEmail(email);

                Set<UserRole> userRoles = new HashSet<>();
                Role role1 = new Role();
                role1.setRoleId(1);
                role1.setName("ROLE_USER");
                userRoles.add(new UserRole(user, role1));
                try {
                    userService.createUser(user, userRoles);
                } catch (Exception ex) {
                    Logger.getLogger(ChechGUser.class.getName()).log(Level.SEVERE, null, ex);
                }

                return user;
            }
        } else {
            System.out.println("++++++++++++++++++++++++++++" + authentication);
            System.out.println("***************************" + authentication.getName());
            User user = userService.findByUsername(authentication.getName());
            return user;
        }
    }
}
