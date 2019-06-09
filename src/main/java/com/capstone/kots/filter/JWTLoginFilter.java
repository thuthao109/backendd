package com.capstone.kots.filter;

import com.capstone.kots.constant.SecurityConstants;
import com.capstone.kots.entity.User;
import com.capstone.kots.repository.UserRepository;
import com.capstone.kots.service.TokenAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private BanRepository banRepository;

    public JWTLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("Call attemptAuthentication to authorize username and password");
        User user = new ObjectMapper().readValue(request.getReader(),User.class);
        String username = user.getUsername();
        String password = user.getPassword();
        log.info("JWTLoginFilter.attemptAuthentication: username/password= " + username +"/"+ password);
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password,
                        Collections.emptyList()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        TokenAuthenticationService.addAuthentication(response, authResult.getName());
        User user = userRepository.findByUsername(authResult.getName());
        user.setPassword(null);
        String userJson = new ObjectMapper().writeValueAsString(user).replace("{","").replace("}","");
        String authorizationString = response.getHeader("Authorization");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"");
        response.getWriter().write( SecurityConstants.HEADER_STRING + "\":\"" + authorizationString + "\",");
        response.getWriter().write( userJson);
//        response.getWriter().write(userJson);
        response.getWriter().write("}");
//        if(!banRepository.existsByUserIdAndExpireGreaterThanAndIsActive(
//                userRepository.findByUsername(authResult.getName()).getId(),
//                new Timestamp(System.currentTimeMillis()),
//                (byte) 1
//        )) {
//
//
//        }
    }
}
