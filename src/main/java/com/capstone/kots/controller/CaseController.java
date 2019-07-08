package com.capstone.kots.controller;

import com.capstone.kots.entity.Case;
import com.capstone.kots.entity.EmptyJsonResponse;
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
import java.util.Optional;
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


    @RequestMapping(value = "/cases/{caseId}", method = RequestMethod.GET)
    public ResponseEntity getCase(@PathVariable("caseId") Integer caseId){
        Case caseOne = caseService.getCaseById(caseId);
        if (caseOne == null){
            return ResponseEntity.status(HttpStatus.OK).body(new EmptyJsonResponse());
        }
        return ResponseEntity.status(HttpStatus.OK).body(caseOne);
    }

    @RequestMapping(value = "/cases/code/{caseCode}", method = RequestMethod.GET)
    public ResponseEntity getCase(@PathVariable("caseCode") String caseCode){
        Case caseOne = caseService.getCaseByCaseCode(caseCode);
        if (caseOne == null){
            return ResponseEntity.status(HttpStatus.OK).body(new EmptyJsonResponse());
        }
        return ResponseEntity.status(HttpStatus.OK).body(caseOne);
    }

    @RequestMapping(value = "/case/{caseId}/join", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity joinCase(@PathVariable("caseId") Integer caseId,
                                      @RequestParam("userId") Integer userId) throws CaseExceptions.CaseNotExisted, CaseExceptions.CaseIsFull, CaseExceptions.AlreadyJoinCase, ExecutionException, InterruptedException {
        Case joinCase = caseService.joinCase(caseId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(joinCase);
    }

    @RequestMapping(value = "/case/{caseId}/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity updateCase(@PathVariable("caseId") Integer caseId,
                                      @RequestParam("userId") Integer userId,
                                      @RequestParam("limitPeople") Integer limitPeople,
                                      @RequestParam("caseTag") String caseTag) throws CaseExceptions.CaseNotExisted, CaseExceptions.CaseAlreadyConfirmed, CaseExceptions.LimitPeopleRequired, CaseExceptions.CaseTagRequired {
        Case confirmCase = caseService.updateCase(caseId,userId,limitPeople,caseTag);
        return ResponseEntity.status(HttpStatus.OK).body(confirmCase);
    }

    @RequestMapping(value = "/case/code/{caseCode}/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity updateCaseByCaseCode(@PathVariable("caseCode") String caseCode,
                                      @RequestParam(value = "limitPeople",required = false) Integer limitPeople,
                                      @RequestParam(value = "caseTag",required = false) String caseTag) throws CaseExceptions.CaseNotExisted, CaseExceptions.CaseAlreadyConfirmed, CaseExceptions.LimitPeopleRequired, CaseExceptions.CaseTagRequired {
        Optional<Case> updatedCase = caseService.updateCaseByCaseCode(caseCode,limitPeople,caseTag);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCase);
    }


    @RequestMapping(value = "/case/{caseId}/delete", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity deleteCase(@PathVariable("caseId") Integer caseId,
                                     @RequestParam(value = "userId") Integer userId) throws CaseExceptions.CaseNotExisted, CaseExceptions.RejectReasonRequired {
        Case rejectCase = caseService.deleteCase(caseId,userId);
        return ResponseEntity.status(HttpStatus.OK).body(rejectCase);
    }

    @RequestMapping(value = "/case/code/{caseCode}/delete", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity deleteCaseByCaseCode(@PathVariable("caseCode") String caseCode,
                                     @RequestParam(value = "userId") Integer userId) throws CaseExceptions.CaseNotExisted, CaseExceptions.RejectReasonRequired {
        Case rejectCase = caseService.deleteCaseByCaseCode(caseCode,userId);
        return ResponseEntity.status(HttpStatus.OK).body(rejectCase);
    }

    @RequestMapping(value = "/cases", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity creatNewCase(@RequestParam("file") MultipartFile file, Case newCase) throws IOException, UserExceptions.UserNotFoundException, CaseExceptions.EvidenceNotExistedException, CaseExceptions.CoordinateNotExistedException, ExecutionException, InterruptedException, CaseExceptions.NoCreatedUserDefineException {
        log.info("Call case Service for creating new case");
        Case createdCase = caseService.createCaseWithEvidence(newCase, file);
        return ResponseEntity.status(HttpStatus.OK).body(createdCase);
    }

    @RequestMapping(value = "/cases/sos", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity creatNewCase(@RequestBody Case newCase) throws IOException, UserExceptions.UserNotFoundException, CaseExceptions.EvidenceNotExistedException, CaseExceptions.CoordinateNotExistedException, ExecutionException, InterruptedException, CaseExceptions.NoCreatedUserDefineException {
        log.info("Call case Service for creating new case");
        Case createdCase = caseService.createCaseSos(newCase);
        return ResponseEntity.status(HttpStatus.OK).body(createdCase);
    }


    @RequestMapping(value = "/chasing-cases", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity creatNewChasingCase(Case newCase) throws CaseExceptions.CoordinateNotExistedException, ExecutionException, InterruptedException, UserExceptions.UserNotFoundException {
        log.info("Call case Service for creating new case");
        Case createdCase = caseService.createChasingCase(newCase);
        return ResponseEntity.status(HttpStatus.OK).body(createdCase);
    }


}
