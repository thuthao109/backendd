package com.capstone.kots.controller;

import com.capstone.kots.entity.User;
import com.capstone.kots.exception.RoleExceptions;
import com.capstone.kots.exception.UserExceptions;
import com.capstone.kots.repository.UserRepository;
import com.capstone.kots.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    public ResponseEntity onlineUser(@RequestBody User user) throws UserExceptions.UserNotFoundException {
//        User result = userService.setOnline(user);
//        if (result == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }

    //register user
    @RequestMapping(value = "/users", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE ,headers = "Accept=application/json")
    public ResponseEntity registerUser(@RequestBody User newUser) throws UnsupportedEncodingException, NoSuchAlgorithmException, RoleExceptions.RoleNotExistException, UserExceptions.UserDuplicateException, UserExceptions.UserLinkDonateExisted, UserExceptions.UsernameExistedException {
//        log.info("Call User Service create a User not by Facebook");
        User result = userService.createUser(newUser,null);
        if (result == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(value = "/users/{userId}/refresh-token",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE ,headers = "Accept=application/json")
    public ResponseEntity updateToken(@PathVariable(name="userId") int userId, @RequestBody User userReq) throws UserExceptions.UserNotFoundException {
        log.info(userReq.getDeviceToken());
        User result = userService.updateToken(userReq.getDeviceToken(),userId);
        if(result == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
