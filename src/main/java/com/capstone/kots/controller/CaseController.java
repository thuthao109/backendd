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
import java.util.List;
import java.util.concurrent.ExecutionException;

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


    @RequestMapping(value = "/cases", method = RequestMethod.GET)
    public ResponseEntity getAllCase(){
        List<Case> cases=caseService.getAllCase();
        if (cases == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(cases);
    }

    @RequestMapping(value = "/case/{caseId}/join", method = RequestMethod.PUT)
    public ResponseEntity joinCase(@PathVariable("caseId") Integer caseId,
                                      @RequestParam("userId") Integer userId) throws CaseExceptions.CaseNotExisted, CaseExceptions.CaseIsFull {
        Case joinCase = caseService.joinCase(caseId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(joinCase);
    }

    @RequestMapping(value = "/case/{caseId}/confirm", method = RequestMethod.PUT)
    public ResponseEntity confirmCase(@PathVariable("caseId") Integer caseId,
                                      @RequestParam("userId") Integer userId) throws CaseExceptions.CaseNotExisted {
        Case confirmCase = caseService.confirmCase(caseId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(confirmCase);
    }

    @RequestMapping(value = "/case/{caseId}/reject", method = RequestMethod.PUT)
    public ResponseEntity rejectCase(@PathVariable("caseId") Integer caseId,
                                     @RequestParam("userId") Integer userId,
                                     @RequestParam("delete_reason") String deleteReason) throws CaseExceptions.CaseNotExisted, CaseExceptions.RejectReasonRequired {
        Case rejectCase = caseService.rejectCase(caseId,userId,deleteReason);
        return ResponseEntity.status(HttpStatus.OK).body(rejectCase);
    }

    @RequestMapping(value = "/cases", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity creatNewCase(@RequestParam("file") MultipartFile file, Case newCase) throws IOException, UserExceptions.UserNotFoundException, CaseExceptions.EvidenceNotExistedException, CaseExceptions.CoordinateNotExistedException, ExecutionException, InterruptedException {
        log.info("Call case Service for creating new case");
        Case createdCase = caseService.createCaseWithEvidence(newCase, file);
        return ResponseEntity.status(HttpStatus.OK).body(createdCase);
    }


    @RequestMapping(value = "/chasing-cases", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity creatNewChasingCase(Case newCase) throws CaseExceptions.CoordinateNotExistedException, ExecutionException, InterruptedException, UserExceptions.UserNotFoundException {
        log.info("Call case Service for creating new case");
        Case createdCase = caseService.createChasingCase(newCase);
        return ResponseEntity.status(HttpStatus.OK).body(createdCase);
    }


}
