package com.capstone.kots.service;

import com.capstone.kots.entity.Case;
import com.capstone.kots.entity.Notification;
import com.capstone.kots.entity.User;
import com.capstone.kots.exception.CaseExceptions;
import com.capstone.kots.exception.UserExceptions;
import com.capstone.kots.repository.CaseRepository;
import com.capstone.kots.repository.UserRepository;
import com.capstone.kots.util.ServiceUtil;
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
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class CaseService {
    public final CaseRepository caseRepository;
    public final UserRepository userRepository;
    public final AmazonClient  amazonClient;
    public ObjectMapper mapper;
    public final FirebasePushNotificationService pushNotificationService;
    private final String CHASING_TAG_TYPE = "Rượt bắt cuớp";
    private final RealtimeAPIService realtimeAPIService;
    private final String REALTIME_API = "http://localhost:3001";

    @Autowired
    public CaseService(CaseRepository caseRepository,
                        AmazonClient amazonClient,
                         FirebasePushNotificationService pushNotificationService,
                          RealtimeAPIService realtimeAPIService,
                           UserRepository userRepository) {
        this.caseRepository = caseRepository;
        this.amazonClient = amazonClient;
        this.pushNotificationService = pushNotificationService;
        this.realtimeAPIService = realtimeAPIService;
        this.userRepository = userRepository;
        this.mapper = new ObjectMapper();
    }

    @Transactional(rollbackFor = Exception.class)
    public Case createChasingCase(Case newCase) throws CaseExceptions.CoordinateNotExistedException, InterruptedException, ExecutionException, UserExceptions.UserNotFoundException {
        if(newCase.getLatitude() == 0 || newCase.getLongitude() == 0){
            throw new CaseExceptions.CoordinateNotExistedException();
        }

        if(newCase.getCreatedId() == null){
            throw new UserExceptions.UserNotFoundException();
        }

        Optional<User> updatedUser = userRepository.findById(newCase.getCreatedId());
        if(!updatedUser.isPresent()) {
            throw new UserExceptions.UserNotFoundException();
        }

        newCase.setCaseTagType(1);
        newCase.setCaseCode(ServiceUtil.getAlphaNumericString(12));
        newCase.setCaseTag(CHASING_TAG_TYPE);
        caseRepository.saveAndFlush(newCase);

        Date date= new Date();

        long time = date.getTime();
        System.out.println("Time in Milliseconds: " + time);

        Timestamp ts = new Timestamp(time);

        JSONObject data = new JSONObject();
        data.put("userId", newCase.getCreatedId());
        data.put("timeCreated", ts.getTime());

        String url = String.format("%s/%s/%d",REALTIME_API,"chase",newCase.getId());

        HttpEntity<String> request = new HttpEntity<>(data.toString());

        CompletableFuture<String> createCaseRoom = realtimeAPIService.send(url,request);

        String createCaseRoomResponse = createCaseRoom.get();

        log.info("==============");
        log.info(createCaseRoomResponse);

        return newCase;
    }

    @Transactional(rollbackFor = Exception.class)
    public Case createCaseWithEvidence(Case newCase, MultipartFile file) throws IOException, CaseExceptions.EvidenceNotExistedException, CaseExceptions.CoordinateNotExistedException, ExecutionException, InterruptedException {

        if(newCase.getLatitude() == 0 || newCase.getLongitude() == 0){
            throw new CaseExceptions.CoordinateNotExistedException();
        }

        if(file == null){
            throw new CaseExceptions.EvidenceNotExistedException();
        }

        String url = amazonClient.uploadFile(file);
        newCase.setCaseSource(url);
        newCase.setCaseTagType(2);
        newCase.setCaseCode(ServiceUtil.getAlphaNumericString(12));

        caseRepository.saveAndFlush(newCase);

        JSONObject data = new JSONObject();
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr ="";
        try {

            // get Oraganisation object as a json string
            jsonStr = Obj.writeValueAsString(newCase);

            // Displaying JSON String
            System.out.println(jsonStr);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        data.put("case", jsonStr);

        String urlNotify = String.format("%s/case/notify",REALTIME_API);

        HttpEntity<String> request = new HttpEntity<>(data.toString());

        realtimeAPIService.send(urlNotify,request);

//        String createCaseRoomResponse = createCaseRoom.get();

//        log.info("=====================");
//        log.info(createCaseRoomResponse);

        return newCase;

    }

    public boolean isExistUser(Integer id) {
        log.info("Check Exist of User id " + id);
        return userRepository.findById(id).isPresent();
    }

}
