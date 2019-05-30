package com.capstone.kots.provider;


import com.capstone.kots.entity.FacebookResource;
import com.capstone.kots.entity.User;
import com.capstone.kots.service.RoleService;
import com.capstone.kots.service.UserService;
import com.capstone.kots.token.SocialAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SocialAuthenticationProvider implements AuthenticationProvider {


    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    private RoleService roleService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Insert account facebook if don't have");
        String id = authentication.getName();
        Object credentials = authentication.getCredentials();
        System.out.println("credentials class: " + credentials.getClass());
        if (!(credentials instanceof FacebookResource)) {
            throw new BadCredentialsException("Authentication failed for id facebook "+id);
        }
        FacebookResource facebookResource = (FacebookResource) credentials;
        User user = userService.getUserByFacebookId(id);
        if (user == null){
//            try {
//                user = userService.createUser(user,facebookResource);
//            } catch (NoSuchAlgorithmException |
//                    UnsupportedEncodingException |
//                    RoleExceptions.RoleNotExistException |
//                    UserExceptions.UserDuplicateException |
//                    UserExceptions.UserReferenceCodeNotExistException | UserExceptions.UserLinkDonateExisted e) {
//                e.printStackTrace();
//                return null;
//            }
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(roleService.getRoleName(user.getId()).stream().findFirst().get()));
        Authentication auth = new SocialAuthenticationToken(id,facebookResource,grantedAuthorities);
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(SocialAuthenticationToken.class);
    }
}
