package com.capstone.kots.filter;

import com.capstone.kots.entity.FacebookResource;
import com.capstone.kots.repository.UserRepository;
import com.capstone.kots.token.SocialAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


@Slf4j
public class SocialLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private BanRepository banRepository;


    public SocialLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        log.info("Call attemptAuthentication method to check exist Facebook account in DB");
        FacebookResource facebookResource = new ObjectMapper().readValue(request.getReader(), FacebookResource.class);
        System.out.printf("SocialLoginFilter.attemptAuthentication: id= %s", facebookResource.getId());
        return getAuthenticationManager().authenticate(
                new SocialAuthenticationToken(
                        facebookResource.getId(),
                        facebookResource,
                        Collections.emptyList()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("SocialLoginFilter.successfulAuthentication:");
//        if(!banRepository.existsByUserIdAndExpireGreaterThanAndIsActive(
//                userRepository.findByFacebookId(authResult.getName()).getId(),
//                new Timestamp(System.currentTimeMillis()),
//                (byte) 1
//        )) {
//            TokenAuthenticationService.addAuthentication(response, authResult.getName());
//            String authorizationString = response.getHeader("Authorization");
//            System.out.println("Authorization String=" + authorizationString);
//        }
    }
}

