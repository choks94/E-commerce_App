package com.example.demo;

import com.example.demo.domain.User;
import com.example.demo.domain.UserComment;
import com.example.demo.domain.security.Role;
import com.example.demo.domain.security.UserRole;
import com.example.demo.service.UserService;
import com.example.demo.utility.SecurityUtility;
import java.security.Principal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;

@SpringBootApplication
@EnableOAuth2Client
public class WatchStoreApplication {

    @Autowired
    private UserService userService;

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    public static void main(String[] args) {
        SpringApplication.run(WatchStoreApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        User user1 = new User();
//        user1.setFirstName("Stefan");
//        user1.setLastName("Colic");
//        user1.setUsername("coks");
//        user1.setPassword(SecurityUtility.passwordEncoder().encode("coks"));
//        user1.setEmail("stefan.colic94@gmail.com");
// 
//        Set<UserRole> userRoles = new HashSet<>();
//        Role role1 = new Role();
//        role1.setRoleId(1);
//        role1.setName("ROLE_USER");
//        userRoles.add(new UserRole(user1, role1));
//        userService.createUser(user1, userRoles);
        //        User user1 = new User();
        //        user1.setFirstName("S");
        //        user1.setLastName("C");
        //        user1.setUsername("c");
        //        user1.setPassword(SecurityUtility.passwordEncoder().encode("c"));
        //        user1.setEmail("scolic993@gmail.com");
        //
        //        Set<UserRole> userRoles = new HashSet<>();
        //        Role role1 = new Role();
        //        role1.setRoleId(1);
        //        role1.setName("ADMIN");
        //        UserComment userComment = new UserComment();
        //        userComment.setComment("blaaaa blaaa blaaa");
        //        userComment.setUser(user1);
        //        userRoles.add(new UserRole(user1, role1));
        //        userService.createUser(user1, userRoles);
//    }
}
