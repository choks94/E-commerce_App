/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 *
 * @author STEFAN94
 */

@Configuration
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
    
  

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        
        String targetUrl =determinateTargetUrl(authentication);
        
        if(response.isCommitted()){
            return;
        }
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
    
    protected String determinateTargetUrl(Authentication authentication){
        
        String url = "login?/error=true";
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = new ArrayList<String>();
        for(GrantedAuthority grantedAuthority : authorities){
            roles.add(grantedAuthority.getAuthority());
            System.out.println(""+grantedAuthority.getAuthority());
        }
        
        if(roles.contains("ROLE_ADMIN")){
            url = "/adminportal/home";
        }else if(roles.contains("ROLE_USER")){
            url = "/";
        }
        return url;
    }
    
    
    
}
