package com.capstone.kots.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.capstone.kots.constant.SecurityConstants.*;
import static java.util.Collections.emptyList;

@Slf4j
public class TokenAuthenticationService {

    public static void addAuthentication(HttpServletResponse res, String username) {
        log.info("Call method addAuthentication to add JWT token to header respone");
        String JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        try {
            String token = request.getHeader(HEADER_STRING);
            if (token != null) {
                // parse the token.
                String user = Jwts.parser()
                        .setSigningKey(JWT_SECRET)
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody()
                        .getSubject();

                return user != null ?
                        new UsernamePasswordAuthenticationToken(user, null, emptyList()) :
                        null;
            }
        }catch (ExpiredJwtException ex){
            request.setAttribute("error", ex.getMessage());
        }


        return null;
    }
}
