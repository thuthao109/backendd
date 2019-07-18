package com.capstone.kots.service;

import com.capstone.kots.entity.Case;
import com.capstone.kots.entity.CaseDetailInfo;
import com.capstone.kots.entity.User;
import com.capstone.kots.entity.UserJoinCase;
import com.capstone.kots.exception.CaseExceptions;
import com.capstone.kots.exception.UserExceptions;
import com.capstone.kots.repository.CaseDetailInfoRepository;
import com.capstone.kots.repository.CaseRepository;
import com.capstone.kots.repository.UserJoinCaseRepository;
import com.capstone.kots.repository.UserRepository;
import com.capstone.kots.util.ServiceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import redis.embedded.Redis;

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
    public final CaseDetailInfoRepository caseDetailInfoRepository;
    public final AmazonClient amazonClient;
    public ObjectMapper mapper;
    public final FirebasePushNotificationService pushNotificationService;
    private final String CHASING_TAG_TYPE = "Rượt bắt cuớp";
    private final String EMERGENCY_CASE_NAME = "Tín hiệu khẩn cấp";
    private final String SIGNAL_RECEIVE = "Tín hiệu hỗ trợ";
    private final String SUPPORT_CASE_NAME = "Hỗ trợ bắt cướp";
    private final String HAPPEN_CASE_NAME = "Vụ việc xảy ra";
    private final String UNDEFINED_CASE = "Chưa xác định";
    private final RealtimeAPIService realtimeAPIService;
    private final String REALTIME_API = "http://localhost:3001";
    private UserJoinCaseRepository userJoinCaseRepository;
    private RedisMessagePublisher publisher;

    private final String ROBBER_NAME = "Cướp giật";
    private final String BIENTHAI_NAME = "Biến thái";
    private final String BATCOC_NAME = "Bắt cóc";
    private final String HOAHOAN_NAME = "Hoả hoạn";
    private final String BAOLUC_NAME = "Bạo lực";
    private final String TAINAN_NAME = "Tai nạn";
    private final String CAPCUU_NAME = "Cấp cứu";
    private final String KHAC_NAME = "Khác";




    @Autowired
    public CaseService(CaseRepository caseRepository,
                       AmazonClient amazonClient,
                       FirebasePushNotificationService pushNotificationService,
                       RealtimeAPIService realtimeAPIService,
                       UserRepository userRepository,
                       UserJoinCaseRepository userJoinCaseRepository,
                       CaseDetailInfoRepository caseDetailInfoRepository,
                       RedisMessagePublisher publisher) {
        this.caseRepository = caseRepository;
        this.amazonClient = amazonClient;
        this.pushNotificationService = pushNotificationService;
        this.realtimeAPIService = realtimeAPIService;
        this.userRepository = userRepository;
        this.userJoinCaseRepository = userJoinCaseRepository;
        this.caseDetailInfoRepository = caseDetailInfoRepository;
        this.publisher = publisher;
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

    public Optional<Case> getUserJoined(Optional<Case> caseOne){
        Optional<List<UserJoinCase>> userJoined = userJoinCaseRepository.findByCaseId(caseOne.get().getId());

        if (userJoined.isPresent()) {
            userJoined.get().forEach(userjoined -> {
                Optional<User> user = userRepository.findById(userjoined.getUserId());
                if (user.isPresent()) {
                    userjoined.setUser(user.get());
                }
            });
            caseOne.get().setUserJoinCases(userJoined.get());
        }

        return caseOne;
    }

    @Transactional(readOnly = true)
    public Case getCaseByCaseCode(String caseCode){
        Optional<Case> caseOne = caseRepository.findActiveCaseByCaseCode(caseCode);
        caseOne.get().setCreatedUser(userRepository.findById(caseOne.get().getCreatedId()).get());
        if(caseOne.isPresent()){
            caseOne = getUserJoined(caseOne);
        }else {
            return null;
        }

        return caseOne.get();
    }


    @Transactional(readOnly = true)
    public Case getCaseById(Integer caseId){
        Optional<Case> caseOne = caseRepository.findActiveCaseById(caseId);
        caseOne.get().setCreatedUser(userRepository.findById(caseOne.get().getCreatedId()).get());
        if(caseOne.isPresent()){
            caseOne = getUserJoined(caseOne);
        }else {
            return null;
        }

        return caseOne.get();
    }

    @Transactional(rollbackFor = Exception.class)
    public Optional<Case> updateCaseByCaseCode(String caseCode,
                                     Integer limitPeople,
                                     String caseTag) throws CaseExceptions.CaseNotExisted, CaseExceptions.CaseAlreadyConfirmed, CaseExceptions.LimitPeopleRequired, CaseExceptions.CaseTagRequired {
        if (caseCode.equals("")) {
            throw new CaseExceptions.CaseNotExisted();
        }

        Optional<Case> caseOne = caseRepository.findActiveCaseByCaseCode(caseCode);
        if (!caseOne.isPresent()) {
            throw new CaseExceptions.CaseNotExisted();
        }

        if(caseTag != null && !caseTag.trim().equals("")){
            caseOne.get().setCaseTag(caseTag);
        }

        if(limitPeople != null && limitPeople != 0){
            caseOne.get().setPeopleLimit(limitPeople);
        }

        caseRepository.save(caseOne.get());
        caseOne.get().setCreatedUser(userRepository.findById(caseOne.get().getCreatedId()).get());
        caseOne = getUserJoined(caseOne);

        realtimeAPIService.updateCase(caseOne.get());

        return caseOne;
    }

    @Transactional(rollbackFor = Exception.class)
    public Case updateCase(Integer caseId, Integer userId, Integer limitPeople, String caseTag) throws CaseExceptions.CaseNotExisted, CaseExceptions.CaseAlreadyConfirmed, CaseExceptions.LimitPeopleRequired, CaseExceptions.CaseTagRequired {
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
            return caseOne.get();
        }

        return caseRepository.save(caseOne.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public Case deleteCase(Integer caseId, Integer rejectUserId) throws CaseExceptions.CaseNotExisted, CaseExceptions.RejectReasonRequired {
        if (caseId == 0 || caseId == null) {
            throw new CaseExceptions.CaseNotExisted();
        }

        Optional<Case> caseOne = caseRepository.findActiveCaseById(caseId);
        if (!caseOne.isPresent()) {
            throw new CaseExceptions.CaseNotExisted();
        }
        Timestamp ts = new Timestamp(new Date().getTime());
        caseOne.get().setDeletedTime(ts);
        caseOne.get().setDeletedReason("Không xảy ra");
        caseOne.get().setDeletedUserId(rejectUserId);

        return caseRepository.save(caseOne.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public Case deleteCaseByCaseCode(String caseCode, Integer rejectUserId) throws CaseExceptions.CaseNotExisted, CaseExceptions.RejectReasonRequired {
        if (caseCode == null || caseCode.equals("")) {
            throw new CaseExceptions.CaseNotExisted();
        }

        Optional<Case> caseOne = caseRepository.findActiveCaseByCaseCode(caseCode);
        if (!caseOne.isPresent()) {
            throw new CaseExceptions.CaseNotExisted();
        }
        Timestamp ts = new Timestamp(new Date().getTime());
        caseOne.get().setDeletedTime(ts);
        caseOne.get().setDeletedReason("Không xảy ra");
        caseOne.get().setDeletedUserId(rejectUserId);

        caseRepository.save(caseOne.get());

        realtimeAPIService.deleteCase(caseOne.get().getCaseCode());
        return caseOne.get();
    }

    @Transactional(rollbackFor = Exception.class)
    public Case cancelCaseByCaseId(Integer caseId, Integer cancelUserId) throws CaseExceptions.CaseNotExisted, CaseExceptions.RejectReasonRequired {
        if (caseId == 0 || caseId == null) {
            throw new CaseExceptions.CaseNotExisted();
        }

        Optional<Case> caseOne = caseRepository.findActiveCaseById(caseId);
        if (!caseOne.isPresent()) {
            throw new CaseExceptions.CaseNotExisted();
        }
        Timestamp ts = new Timestamp(new Date().getTime());
        caseOne.get().setCaseStatus(3);


        caseRepository.save(caseOne.get());

//        realtimeAPIService.deleteCase(caseOne.get().getCaseCode());
        return caseOne.get();
    }


    @Transactional(rollbackFor = Exception.class)
    public Case joinCase(Integer joinedCaseId, Integer userId) throws CaseExceptions.CaseNotExisted, CaseExceptions.CaseIsFull, CaseExceptions.AlreadyJoinCase, ExecutionException, InterruptedException {
        if (joinedCaseId == 0 || joinedCaseId == null) {
            throw new CaseExceptions.CaseNotExisted();
        }

        Optional<Case> caseOne = caseRepository.findActiveCaseById(joinedCaseId);
        if (!caseOne.isPresent()) {
            throw new CaseExceptions.CaseNotExisted();
        }

        Optional<UserJoinCase> alreadyJoined = userJoinCaseRepository.findByCaseIdAndUserId(joinedCaseId, userId);
        if (alreadyJoined.isPresent()) {
            caseOne.get().setCreatedUser(userRepository.findById(caseOne.get().getCreatedId()).get());
            if(caseOne.isPresent()){
                caseOne = getUserJoined(caseOne);
            }
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

        caseOne.get().setCreatedUser(userRepository.findById(caseOne.get().getCreatedId()).get());
        if(caseOne.isPresent()){
            caseOne = getUserJoined(caseOne);
        }



        //Update join case
        String responseString = realtimeAPIService.joinCaseUpdatePeopleJoin(caseOne.get().getCaseCode(),users.size()).get();
        log.info("==============");
        log.info(responseString);

        Optional<User> userJoining = userRepository.findById(userId);
        Date now = new Date();
        Timestamp ts = new Timestamp(now.getTime());

        CaseDetailInfo detailInfo = new CaseDetailInfo();
        detailInfo.setCaseId(caseOne.get().getId());
        detailInfo.setDescription(String.format(Locale.US,"Hiệp sĩ %s đã tham gia xử lý",userJoining.get().getFullname()));

        detailInfo.setCreatedTime(ts);

        caseDetailInfoRepository.save(detailInfo);

        String responseSubCollect = realtimeAPIService.createSubCollectionInCase(caseOne.get().getCaseCode(),detailInfo).get();
        log.info(responseSubCollect);

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
        newCase.setCaseName(SUPPORT_CASE_NAME);
        newCase.setCaseCode(ServiceUtil.getAlphaNumericString(12));
        newCase.setCaseTag(CHASING_TAG_TYPE);
        newCase.setCreatedUser(updatedUser.get());
        Date date = new Date();

        long time = date.getTime();
        System.out.println("Time in Milliseconds: " + time);

        Timestamp ts = new Timestamp(time);

        newCase.setCreatedTime(ts);

        caseRepository.saveAndFlush(newCase);

        String responseString = realtimeAPIService.createChasingCase(newCase).get();
        log.info("==============");
        log.info(responseString);

        publisher.publish(newCase);

        return newCase;
    }

    @Transactional(rollbackFor = Exception.class)
    public Case createCaseSos(Case newCase) throws IOException, CaseExceptions.EvidenceNotExistedException, CaseExceptions.CoordinateNotExistedException, ExecutionException, InterruptedException, CaseExceptions.NoCreatedUserDefineException {

        if (newCase.getLatitude() == 0 || newCase.getLongitude() == 0) {
            throw new CaseExceptions.CoordinateNotExistedException();
        }

        if (newCase.getCreatedId() == null || newCase.getCreatedId() == 0){
            throw new CaseExceptions.NoCreatedUserDefineException();

        }

        Optional<User> isExistedUser = userRepository.findById(newCase.getCreatedId());

        if(!isExistedUser.isPresent()){
            throw new CaseExceptions.NoCreatedUserDefineException();
        }

        newCase.setCreatedUser(isExistedUser.get());
        newCase.setCaseTagType(3);
        newCase.setCaseName(EMERGENCY_CASE_NAME);
        newCase.setCaseCode(ServiceUtil.getAlphaNumericString(12));
        newCase.setPeopleLimit(3);
        newCase.setCaseTag(UNDEFINED_CASE);
        Date now = new Date();
        Timestamp ts = new Timestamp(now.getTime());
        newCase.setCreatedTime(ts);

        caseRepository.saveAndFlush(newCase);

        publisher.publish(newCase);

        String responseString = realtimeAPIService.createCase(newCase).get();
        log.info(responseString);

        //Notify to online knight -- end

        return newCase;

    }

    @Transactional(rollbackFor = Exception.class)
    public Case createCaseWithEvidence(Case newCase, MultipartFile file) throws IOException, CaseExceptions.EvidenceNotExistedException, CaseExceptions.CoordinateNotExistedException, ExecutionException, InterruptedException, CaseExceptions.NoCreatedUserDefineException, CaseExceptions.CaseTagTypeException {

        if (newCase.getLatitude() == 0 || newCase.getLongitude() == 0) {
            throw new CaseExceptions.CoordinateNotExistedException();
        }

        if(newCase.getCaseTagType() == 0){
            throw new CaseExceptions.CaseTagTypeException();
        }

//        if (file == null) {
//            throw new CaseExceptions.EvidenceNotExistedException();
//        }

        if (newCase.getCreatedId() == null || newCase.getCreatedId() == 0){
            throw new CaseExceptions.NoCreatedUserDefineException();

        }

        Optional<User> isExistedUser = userRepository.findById(newCase.getCreatedId());

        if(!isExistedUser.isPresent()){
            throw new CaseExceptions.NoCreatedUserDefineException();
        }

        if(file != null){
            if (file.getOriginalFilename().contains(".mp4")) {
                newCase.setDisplayType(1);
            } else {
                newCase.setDisplayType(2);
            }
            String url = amazonClient.uploadFile(file);
            newCase.setCaseSource(url);
        }

        if(newCase.getCaseTagType() == 4){
            newCase.setCaseTagType(4);
            newCase.setCaseName(ROBBER_NAME);
        }else if(newCase.getCaseTagType() == 5){
            newCase.setCaseTagType(5);
            newCase.setCaseName(BIENTHAI_NAME);
        }else if(newCase.getCaseTagType() == 6){
            newCase.setCaseTagType(6);
            newCase.setCaseName(BATCOC_NAME);
        }else if(newCase.getCaseTagType() == 7){
            newCase.setCaseTagType(7);
            newCase.setCaseName(HOAHOAN_NAME);
        }else if(newCase.getCaseTagType() == 8){
            newCase.setCaseTagType(8);
            newCase.setCaseName(BAOLUC_NAME);
        }else if(newCase.getCaseTagType() == 9){
            newCase.setCaseTagType(9);
            newCase.setCaseName(TAINAN_NAME);
        }else if(newCase.getCaseTagType() == 10){
            newCase.setCaseTagType(10);
            newCase.setCaseName(CAPCUU_NAME);
        }else if(newCase.getCaseTagType() == 11){
            newCase.setCaseTagType(11);
            newCase.setCaseName(KHAC_NAME);
        }
        newCase.setCaseTag(SIGNAL_RECEIVE);


        newCase.setCaseStatus(1);
        newCase.setCaseCode(ServiceUtil.getAlphaNumericString(12));
        newCase.setCreatedUser(isExistedUser.get());
        newCase.setPeopleLimit(3);

        Date now = new Date();
        Timestamp ts = new Timestamp(now.getTime());
        newCase.setCreatedTime(ts);

        newCase = caseRepository.saveAndFlush(newCase);

        String responseString = realtimeAPIService.createCaseWithEvidence(newCase).get();
        log.info(responseString);

        CaseDetailInfo detailInfo = new CaseDetailInfo();
        detailInfo.setCaseId(newCase.getId());
        detailInfo.setDescription("Tín hiệu đã tạo");

        detailInfo.setCreatedTime(ts);

        caseDetailInfoRepository.save(detailInfo);

        String responseSubCollect = realtimeAPIService.createSubCollectionInCase(newCase.getCaseCode(),detailInfo).get();
        log.info(responseSubCollect);


        publisher.publish(newCase);


        return newCase;

    }

    public boolean isExistUser(Integer id) {
        log.info("Check Exist of User id " + id);
        return userRepository.findById(id).isPresent();
    }

}
