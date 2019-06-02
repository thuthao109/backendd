package com.capstone.kots.controller;

import com.capstone.kots.entity.Case;
import com.capstone.kots.exception.CaseExceptions;
import com.capstone.kots.exception.UserExceptions;
import com.capstone.kots.service.AmazonClient;
import com.capstone.kots.service.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api")
public class CaseController {
    private CaseService caseService;

    @Autowired
    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    //    public ResponseEntity registerUser(@RequestBody Case newUser)  {
////        log.info("Call User Service create a User not by Facebook");
//        Case result = caseService.createUser(newUser,null);
//        if (result == null){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }
    @RequestMapping(value = "/cases", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity creatNewCase(@RequestParam("file") MultipartFile file, Case newCase) throws IOException, UserExceptions.UserNotFoundException, CaseExceptions.EvidenceNotExistedException, CaseExceptions.CoordinateNotExistedException {
        log.info("Call case Service for creating new case");
        Case createdCase = caseService.createCaseWithEvidence(newCase, file);
        return ResponseEntity.status(HttpStatus.OK).body(createdCase);
    }




}
