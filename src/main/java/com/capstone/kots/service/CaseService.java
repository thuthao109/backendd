package com.capstone.kots.service;

import com.capstone.kots.entity.Case;
import com.capstone.kots.entity.Notification;
import com.capstone.kots.entity.User;
import com.capstone.kots.entity.UserJoinCase;
import com.capstone.kots.exception.CaseExceptions;
import com.capstone.kots.exception.UserExceptions;
import com.capstone.kots.repository.CaseRepository;
import com.capstone.kots.repository.UserJoinCaseRepository;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class CaseService {
    public final CaseRepository caseRepository;
    public final UserRepository userRepository;
    public final AmazonClient amazonClient;
    public ObjectMapper mapper;
    public final FirebasePushNotificationService pushNotificationService;
    private final String CHASING_TAG_TYPE = "Rượt bắt cuớp";
    private final RealtimeAPIService realtimeAPIService;
    private final String REALTIME_API = "http://localhost:3001";
    private UserJoinCaseRepository userJoinCaseRepository;

    @Autowired
    public CaseService(CaseRepository caseRepository,
                       AmazonClient amazonClient,
                       FirebasePushNotificationService pushNotificationService,
                       RealtimeAPIService realtimeAPIService,
                       UserRepository userRepository,
                       UserJoinCaseRepository userJoinCaseRepository) {
        this.caseRepository = caseRepository;
        this.amazonClient = amazonClient;
        this.pushNotificationService = pushNotificationService;
        this.realtimeAPIService = realtimeAPIService;
        this.userRepository = userRepository;
        this.userJoinCaseRepository = userJoinCaseRepository;
        this.mapper = new ObjectMapper();
    }


    @Transactional(readOnly = true)
    public List<Case> getAllCase() {

        List<Case> caseList = caseRepository.findActiveCases();

        caseList.forEach(oneCase -> {
            Optional<List<UserJoinCase>> userJoined = userJoinCaseRepository.findByCaseId(oneCase.getId());

            if (userJoined.isPresent()) {
                userJoined.get().forEach(userjoined -> {
                    Optional<User> user = userRepository.findById(userjoined.getUserId());
                    if (user.isPresent()) {
                        userjoined.setUser(user.get());
                    }
                });
                oneCase.setUserJoinCases(userJoined.get());
            }

        });
        return caseList;
    }

    @Transactional(rollbackFor = Exception.class)
    public Case confirmCase(Integer caseId, Integer userId, Integer limitPeople, String caseTag) throws CaseExceptions.CaseNotExisted, CaseExceptions.CaseAlreadyConfirmed, CaseExceptions.LimitPeopleRequired, CaseExceptions.CaseTagRequired {
        if (caseId == 0) {
            throw new CaseExceptions.CaseNotExisted();
        }

        if (caseTag.trim().equals("")) {
            throw new CaseExceptions.CaseTagRequired();
        }


        Optional<Case> caseOne = caseRepository.findActiveCaseById(caseId);
        if (!caseOne.isPresent()) {
            throw new CaseExceptions.CaseNotExisted();
        }

        if (caseOne.get().getConfirmedId() == null || caseOne.get().getConfirmedId() == 0) {
            caseOne.get().setConfirmedId(userId);
            caseOne.get().setCaseTag(caseTag);
            caseOne.get().setPeopleLimit(limitPeople);

            UserJoinCase newJoinedCase = new UserJoinCase();
            newJoinedCase.setUserId(userId);
            newJoinedCase.setCaseId(caseId);

            userJoinCaseRepository.save(newJoinedCase);
        } else {
            throw new CaseExceptions.CaseAlreadyConfirmed();
        }

        return caseRepository.save(caseOne.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public Case rejectCase(Integer caseId, Integer rejectUserId, String deletedReason) throws CaseExceptions.CaseNotExisted, CaseExceptions.RejectReasonRequired {
        if (caseId == 0 || caseId == null) {
            throw new CaseExceptions.CaseNotExisted();
        }

        if (deletedReason == "" || deletedReason == null) {
            throw new CaseExceptions.RejectReasonRequired();
        }

        Optional<Case> caseOne = caseRepository.findActiveCaseById(caseId);
        if (!caseOne.isPresent()) {
            throw new CaseExceptions.CaseNotExisted();
        }
        Timestamp ts = new Timestamp(new Date().getTime());
        caseOne.get().setDeletedTime(ts);
        caseOne.get().setDeletedReason(deletedReason);
        caseOne.get().setDeletedUserId(rejectUserId);

        return caseRepository.save(caseOne.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public Case joinCase(Integer joinedCaseId, Integer userId) throws CaseExceptions.CaseNotExisted, CaseExceptions.CaseIsFull, CaseExceptions.AlreadyJoinCase {
        if (joinedCaseId == 0 || joinedCaseId == null) {
            throw new CaseExceptions.CaseNotExisted();
        }

        Optional<Case> caseOne = caseRepository.findActiveCaseById(joinedCaseId);
        if (!caseOne.isPresent()) {
            throw new CaseExceptions.CaseNotExisted();
        }

        Optional<UserJoinCase> alreadyJoined = userJoinCaseRepository.findByCaseIdAndUserId(joinedCaseId, userId);
        if (alreadyJoined.isPresent()) {
            return caseOne.get();
        }

        UserJoinCase newJoinedCase = new UserJoinCase();
        newJoinedCase.setUserId(userId);
        newJoinedCase.setCaseId(joinedCaseId);

        Optional<List<UserJoinCase>> userJoined = userJoinCaseRepository.findByCaseId(caseOne.get().getId());

        if (userJoined.isPresent()) {
            userJoined.get().forEach(userjoined -> {
                Optional<User> user = userRepository.findById(userjoined.getUserId());
                if (user.isPresent()) {
                    userjoined.setUser(user.get());
                }
            });
            caseOne.get().setUserJoinCases(userJoined.get());
        } else {
            caseOne.get().setUserJoinCases(new ArrayList<>());
        }

        List users = caseOne.get().getUserJoinCases();
        if (caseOne.get().getPeopleLimit() != 0 && users.size() >= caseOne.get().getPeopleLimit()) {
            throw new CaseExceptions.CaseIsFull();
        }
        users.add(newJoinedCase);
        caseOne.get().setUserJoinCases(users);

        userJoinCaseRepository.save(newJoinedCase);

        return caseOne.get();
    }


    @Transactional(rollbackFor = Exception.class)
    public Case createChasingCase(Case newCase) throws CaseExceptions.CoordinateNotExistedException, InterruptedException, ExecutionException, UserExceptions.UserNotFoundException {
        if (newCase.getLatitude() == 0 || newCase.getLongitude() == 0) {
            throw new CaseExceptions.CoordinateNotExistedException();
        }

        if (newCase.getCreatedId() == null) {
            throw new UserExceptions.UserNotFoundException();
        }

        Optional<User> updatedUser = userRepository.findById(newCase.getCreatedId());
        if (!updatedUser.isPresent()) {
            throw new UserExceptions.UserNotFoundException();
        }

        newCase.setCaseTagType(1);
        newCase.setCaseCode(ServiceUtil.getAlphaNumericString(12));
        newCase.setCaseTag(CHASING_TAG_TYPE);
        caseRepository.saveAndFlush(newCase);

        Date date = new Date();

        long time = date.getTime();
        System.out.println("Time in Milliseconds: " + time);

        Timestamp ts = new Timestamp(time);

        JSONObject data = new JSONObject();
        data.put("userId", newCase.getCreatedId());
        data.put("timeCreated", ts.getTime());

        String url = String.format("%s/%s/%d", REALTIME_API, "chase", newCase.getId());

        HttpEntity<String> request = new HttpEntity<>(data.toString());

        CompletableFuture<String> createCaseRoom = realtimeAPIService.send(url, request);

        String createCaseRoomResponse = createCaseRoom.get();

        log.info("==============");
        log.info(createCaseRoomResponse);

        return newCase;
    }

    @Transactional(rollbackFor = Exception.class)
    public Case createCaseWithEvidence(Case newCase, MultipartFile file) throws IOException, CaseExceptions.EvidenceNotExistedException, CaseExceptions.CoordinateNotExistedException, ExecutionException, InterruptedException {

        if (newCase.getLatitude() == 0 || newCase.getLongitude() == 0) {
            throw new CaseExceptions.CoordinateNotExistedException();
        }

        if (file == null) {
            throw new CaseExceptions.EvidenceNotExistedException();
        }

        if (file.getOriginalFilename().contains(".mp4")) {
            newCase.setDisplayType(1);
        } else {
            newCase.setDisplayType(2);
        }
        String url = amazonClient.uploadFile(file);
        newCase.setCaseSource(url);
        newCase.setCaseTagType(2);
        newCase.setCaseCode(ServiceUtil.getAlphaNumericString(12));

        caseRepository.saveAndFlush(newCase);


        //Notify to online knight -- start
        JSONObject data = new JSONObject();
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = "";
        try {

            // get Oraganisation object as a json string
            jsonStr = Obj.writeValueAsString(newCase);
        } catch (IOException e) {
            e.printStackTrace();
        }
        data.put("case", jsonStr);

        String urlNotify = String.format("%s/case/notify", REALTIME_API);

        HttpEntity<String> request = new HttpEntity<>(data.toString());

        realtimeAPIService.send(urlNotify, request);
        //Notify to online knight -- end

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
