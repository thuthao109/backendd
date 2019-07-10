package com.capstone.kots.controller;

import com.capstone.kots.entity.CaseNotification;
import com.capstone.kots.entity.UserJoinCase;
import com.capstone.kots.service.CaseNotificationService;
import com.capstone.kots.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class NotificationController {
    private final UserService userService;
    private final CaseNotificationService caseNotificationService;

    @Autowired
    public NotificationController(UserService userService,
                                   CaseNotificationService caseNotificationService) {
        this.userService = userService;
        this.caseNotificationService = caseNotificationService;
    }

    @RequestMapping(value = "/notifications/{id}/read", method = RequestMethod.PUT)
    public ResponseEntity getCaseOfUserById(@PathVariable("id") int id){
        Optional<CaseNotification> notification = caseNotificationService.readNotification(id);
        if (notification == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(notification);
    }

}
