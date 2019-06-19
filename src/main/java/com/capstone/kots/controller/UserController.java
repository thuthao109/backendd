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
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

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


    //get all user
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity getAllUser(){
        List<User> users=userService.getAllUser();
        if (users == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    //get user
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity getUserById(@PathVariable("id") int id){
        Optional<User> users=userService.findById(id);
        if (users == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }



    //delete user
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable("id") int id){
        Optional<User> users=userService.findById(id);
        if (!users.isPresent()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        userService.deleteUser(users.get());
        return (ResponseEntity) ResponseEntity.status(HttpStatus.OK);

    }


    //register user
    @RequestMapping(value = "/users", method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE ,headers = "Accept=application/json")
    public ResponseEntity registerUser(@RequestParam( value="file", required = false) MultipartFile file, User newUser) throws UnsupportedEncodingException, NoSuchAlgorithmException, RoleExceptions.RoleNotExistException, UserExceptions.UserDuplicateException, UserExceptions.UserLinkDonateExisted, UserExceptions.UsernameExistedException {
//        log.info("Call User Service create a User not by Facebook");
        User result = userService.createUser(newUser,file,null);
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

    //update user
    @RequestMapping(value = "/users/{userId}",method = RequestMethod.PUT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateUserProfile(@PathVariable(name="userId") int userId,
                                            @RequestParam(value="file", required = false) MultipartFile file,
                                             User userReq) throws UserExceptions.UserNotFoundException {
        User result = userService.updateUserProfile(userReq,userId,file);
        if(result == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
