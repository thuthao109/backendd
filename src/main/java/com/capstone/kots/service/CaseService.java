package com.capstone.kots.service;

import com.capstone.kots.entity.Case;
import com.capstone.kots.entity.Notification;
import com.capstone.kots.exception.CaseExceptions;
import com.capstone.kots.repository.CaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class CaseService {
    public final CaseRepository caseRepository;
    public final AmazonClient  amazonClient;
    public ObjectMapper mapper;
    public final FirebasePushNotificationService pushNotificationService;

    @Autowired
    public CaseService(CaseRepository caseRepository, AmazonClient amazonClient,FirebasePushNotificationService pushNotificationService) {
        this.caseRepository = caseRepository;
        this.amazonClient = amazonClient;
        this.pushNotificationService = pushNotificationService;
        this.mapper = new ObjectMapper();
    }

    @Transactional(rollbackFor = Exception.class)
    public Case createCaseWithEvidence(Case newCase, MultipartFile file) throws IOException,CaseExceptions.EvidenceNotExistedException,CaseExceptions.CoordinateNotExistedException {

        if(newCase.getLatitude() == 0 || newCase.getLongitude() == 0){
            throw new CaseExceptions.CoordinateNotExistedException();
        }

        if(file == null){
            throw new CaseExceptions.EvidenceNotExistedException();
        }

        String url = amazonClient.uploadFile(file);
        newCase.setCaseSource(url);

        caseRepository.saveAndFlush(newCase);

//        HashMap<String,Object> body = new HashMap<>();
//        Notification headerNotif = new Notification();
//        headerNotif.setTitle("testing");
//        headerNotif.setBody("body");
//        body.put("notification",headerNotif);
//        body.put("data",newCase);
//
//
//        String requestJson = mapper.writeValueAsString(body);

//        JSONObject body = new JSONObject();
//        body.put("to", "d0ftlJFgfVM:APA91bGJerLz3JAe7kBX-BAC-l0w3AMlfbXSLORIVUgJCxJS1yrd_QaP7uePGMZJamzzKoKewiRCxVq6fx1Xuue94Gx69d53TR5S_LBsA6QKKtTQGoWmhjN884R6fj1yesS8BdRjm_1i");
//
//        JSONObject notification = new JSONObject();
//        notification.put("title", "JSA Notification");
//        notification.put("body", "Happy Message!");
//
//        JSONObject data = new JSONObject();
//        data.put("Key-1", "JSA Data 1");
//        data.put("Key-2", "JSA Data 2");
//
//        body.put("notification", notification);
//        body.put("data", data);

//        HttpEntity<String> request = new HttpEntity<>(body.toString());
//
//        CompletableFuture<String> pushNotification = pushNotificationService.send(request);
//        CompletableFuture.allOf(pushNotification).join();
//
//        try {
//            String firebaseResponse = pushNotification.get();
//
//            log.info("==============");
//            log.info(firebaseResponse);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        return newCase;

    }
}
