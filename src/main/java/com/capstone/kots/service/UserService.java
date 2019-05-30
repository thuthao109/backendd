package com.capstone.kots.service;

import com.capstone.kots.entity.FacebookResource;
import com.capstone.kots.entity.Role;
import com.capstone.kots.entity.User;
import com.capstone.kots.exception.RoleExceptions;
import com.capstone.kots.exception.UserExceptions;
import com.capstone.kots.repository.RoleRepository;
import com.capstone.kots.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.capstone.kots.constant.UserConstants.ROLE_DEFAULT;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    public User getUserByFacebookId(String facebookId) {
        log.info("Get User by Facebook ID " + facebookId);
        User user = userRepository.findByAccessToken(facebookId);
        return user;
    }

    public User getUserByUserName(String userName) {
        log.info("Get User by User Name " + userName);
        User user = userRepository.findByUsername(userName);
        return user;
    }

    public User setFBResourceForUser(User user, FacebookResource facebookResource) {
        log.info("Set Facebook Resource for User ID " + user.getId());
        user.setAccessToken(facebookResource.getId());
//        user.setFirstName(facebookResource.getFirstName());
//        user.setLastName(facebookResource.getLastName());
        user.setAvatarUrl(facebookResource.getAvatar());
//        user.setEmail(facebookResource.getEmail());
//        user.setDisplayName(facebookResource.getUserName());
//        user.setSex((byte) (facebookResource.getGender() ? 1 : 0));
//        user.setLinkDonate(facebookResource.getLinkDonate());
        return user;
    }

    public boolean isExistUsername(String userName) {
        log.info("Check Exist of Username " + userName);
        return userRepository.findByUsername(userName) == null ? false : true;
    }

    @Transactional(rollbackFor = Exception.class)
    public User createUser(User user, FacebookResource facebookResource) throws UnsupportedEncodingException, NoSuchAlgorithmException, RoleExceptions.RoleNotExistException, UserExceptions.UserDuplicateException, UserExceptions.UserLinkDonateExisted, UserExceptions.UsernameExistedException {
        Optional<Role> roleResult = roleRepository.findRolesByRoleName(ROLE_DEFAULT);
        Role role = roleResult.orElseThrow(RoleExceptions.RoleNotExistException::new);


//        if(isExistUsername(user.getUsername())) {
//            throw new UserExceptions.UsernameExistedException();
//        }
        if (facebookResource != null) {
            log.info("Create User by Facebook resource");
            user = new User();
            user = setFBResourceForUser(user, facebookResource);

            user.setPassword(bCryptPasswordEncoder.encode("123456"));
        } else {
            if (!user.getUsername().isEmpty() && isExistUsername(user.getUsername())) {
                throw new UserExceptions.UserDuplicateException();
            }
            log.info("Create New User with User Name" + user.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));


        }
        user.setRoleId(role.getId());

        return userRepository.saveAndFlush(user);
    }

}
