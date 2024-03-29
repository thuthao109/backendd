package com.capstone.kots.service;

import com.capstone.kots.entity.*;
import com.capstone.kots.exception.CaseExceptions;
import com.capstone.kots.exception.RoleExceptions;
import com.capstone.kots.exception.UserExceptions;
import com.capstone.kots.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.capstone.kots.constant.UserConstants.ROLE_DEFAULT;


@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserJoinCaseRepository userJoinCaseRepository;
    private final CaseRepository caseRepository;
    private final CaseNotificationRepository caseNotificationRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public final AmazonClient amazonClient;

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       RoleRepository roleRepository,
                       AmazonClient amazonClient,
                       UserJoinCaseRepository userJoinCaseRepository,
                       CaseRepository caseRepository,
                       CaseNotificationRepository caseNotificationRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.amazonClient = amazonClient;
        this.userJoinCaseRepository = userJoinCaseRepository;
        this.caseRepository = caseRepository;
        this.caseNotificationRepository = caseNotificationRepository;
    }

    //update user
    @Transactional(rollbackFor = Exception.class)
    public User updateUserProfile(User newUser, int userId, MultipartFile file) throws UserExceptions.UserNotFoundException {
        Optional<User> updatedUser = userRepository.findById(userId);
        if (updatedUser.get() == null) {
            throw new UserExceptions.UserNotFoundException();
        }
        updatedUser.get().setFullname(newUser.getFullname());
        //  updatedUser.get().setPassword(newUser.getPassword());
        updatedUser.get().setEmail(newUser.getEmail());
        updatedUser.get().setAddress(newUser.getAddress());
        updatedUser.get().setPhoneNumber(newUser.getPhoneNumber());
        if (file != null) {
            String url = amazonClient.uploadFile(file);
            updatedUser.get().setAvatarUrl(url);
        }
        return userRepository.save(updatedUser.get());
    }


//    @Transactional(rollbackFor = Exception.class)
//    public User updateUserProfile(User newUser, int userId) throws UserExceptions.UserNotFoundException {
//        Optional<User> updatedUser = userRepository.findById(userId);
//        if (updatedUser.get() == null) {
//            throw new UserExceptions.UserNotFoundException();
//        }
//        updatedUser.get().setUsername(newUser.getUsername());
//        updatedUser.get().setPassword(newUser.getPassword());
//        updatedUser.get().setEmail(newUser.getEmail());
//        updatedUser.get().setAddress(newUser.getAddress());
//        updatedUser.get().setPhoneNumber(newUser.getPhoneNumber());
//        updatedUser.get().setAvatarUrl(newUser.getAvatarUrl());
//        updatedUser.get().setRoleId(newUser.getRoleId());
//        updatedUser.get().setDeviceToken(newUser.getDeviceToken());
//
//        return userRepository.save(updatedUser.get());
//    }
    //get all user
    public List<User> getAllUser() {
        List<User> userList = userRepository.findAll();
        //userRepository.delete(new User().setId());
        return userList;
    }

    //get user
    public Optional<User> findById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user;
    }

    public NotificationResponse findNotificationByUserId(int id){
        NotificationResponse response = new NotificationResponse();
        Optional<List<CaseNotification>> caseNotif = caseNotificationRepository.findByUserId(id);
        if(caseNotif.isPresent()){
            int count = 0;
            for(CaseNotification c : caseNotif.get()){
                if(c.getRead() == false){
                    count++;
                }
                Optional<Case> caseById = caseRepository.findById(c.getNotificationCaseSourceId());
                c.setCaseOne(caseById.get());
            }
            response.setBadge(count);
            response.setListNotif(caseNotif.get());
        }else{
            response.setBadge(0);
            response.setListNotif(new ArrayList<>());
        }

        return response;
    }

    public Optional<List<UserJoinCase>> findUserJoinCaseByUserId(int id){
        Optional<List<UserJoinCase>> caseJoined = userJoinCaseRepository.findByUserId(id);
        Iterator<UserJoinCase> iterator;
        if(caseJoined.isPresent()){
            caseJoined.get().forEach(userJoinCase -> {
                Optional<Case> caseById = caseRepository.findById(userJoinCase.getCaseId());
                if (caseById.isPresent()) {
//                    if(caseById.get().getDeletedUserId() != null){
//
//                    }
                    Optional<User> user = userRepository.findById(caseById.get().getCreatedId());
                    if(user.isPresent()){
                        caseById.get().setCreatedUser(user.get());
                    }
                    userJoinCase.setOneCase(caseById.get());
                }
            });
            iterator = caseJoined.get().iterator();
            while (iterator.hasNext()) {
                UserJoinCase s = iterator.next(); // must be called before you can call i.remove()
                // Do something
                if(s.getOneCase().getDeletedUserId() != null){
                    iterator.remove();
                }
            }
        }
        return caseJoined;
    }
    //delete user
    public void deleteUser(User user) {
        userRepository.delete(user);
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
    public User setOnline(User user) throws UserExceptions.UserNotFoundException {

        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    public User updateToken(String fcmToken, int userId) throws UserExceptions.UserNotFoundException {
        Optional<User> updatedUser = userRepository.findById(userId);
        if (updatedUser.get() == null) {
            throw new UserExceptions.UserNotFoundException();
        }
        updatedUser.get().setDeviceToken(fcmToken);
        return userRepository.save(updatedUser.get());
    }

    //create user
    @Transactional(rollbackFor = Exception.class)
    public User createUser(User user, MultipartFile file, FacebookResource facebookResource) throws UnsupportedEncodingException, NoSuchAlgorithmException, RoleExceptions.RoleNotExistException, UserExceptions.UserDuplicateException, UserExceptions.UserLinkDonateExisted, UserExceptions.UsernameExistedException {
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
        if (file != null) {
            String url = amazonClient.uploadFile(file);
            user.setAvatarUrl(url);
        }

        return userRepository.saveAndFlush(user);
    }

}
