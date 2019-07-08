package com.capstone.kots.service;

import com.capstone.kots.entity.Case;
import com.capstone.kots.service.interceptor.HeaderRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class RealtimeAPIService {

    private final String REALTIME_API_KNIGHTHALL = "https://firestore.googleapis.com/v1/projects/kots-18ef0/databases/(default)/documents/knight_hall";
    private final String REALTIME_API_PRIVATE_ROOM = "https://firestore.googleapis.com/v1/projects/kots-18ef0/databases/(default)/documents/case";

    private final String integerType = "integerValue";
    private final String stringType = "stringValue";
    private final String doubleType = "doubleValue";
    private static String FIREBASE_SERVER_KEY = "AAAA7XjQPXQ:APA91bF5mbhgPwOvQjXJL204wGYGAAKPqOkWr4oXjYdfeqf7WQ0P3cBKaynYnRXVYpjQpr5CUUktBGMtISHCdEi-wqjkozL23WexH_PNepuScFw6g_bmVLgfEuE9L-CNZ12Js-X9LMNK";


    @Async
    public CompletableFuture<String> updateCase(Case updatedCase) {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        JSONObject object = new JSONObject();
        JSONObject fields = new JSONObject();
        JSONObject peopleLimit = new JSONObject();
        JSONObject caseTag = new JSONObject();

        peopleLimit.put(integerType,updatedCase.getPeopleLimit());
        caseTag.put(stringType,updatedCase.getCaseTag());

        fields.put("peopleLimit", peopleLimit);
        fields.put("caseTag", caseTag);

        object.put("fields",fields);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<String>(object.toString(), headers);
        log.info("=====================");
//        log.info(object.toString());
//        log.info(REALTIME_API_KNIGHTHALL.concat("/").concat(caseCode).concat("?updateMask.fieldPaths=peopleJoined"));
        ResponseEntity<String> response = restTemplate.exchange(REALTIME_API_KNIGHTHALL.concat("/").concat(updatedCase.getCaseCode()).concat("?updateMask.fieldPaths=peopleLimit&updateMask.fieldPaths=caseTag"), HttpMethod.PATCH, request, String.class);

        return CompletableFuture.completedFuture(response.toString());
    }

    @Async
    public CompletableFuture<String> deleteCase(String caseCode) {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject object = new JSONObject();
        HttpEntity<String> request = new HttpEntity<String>(object.toString(), headers);
        log.info("=====================");
//        log.info(object.toString());
//        log.info(REALTIME_API_KNIGHTHALL.concat("/").concat(caseCode).concat("?updateMask.fieldPaths=peopleJoined"));
        ResponseEntity<String> response = restTemplate.exchange(REALTIME_API_KNIGHTHALL.concat("/").concat(caseCode), HttpMethod.DELETE, request, String.class);

        return CompletableFuture.completedFuture(response.toString());
    }

    @Async
    public CompletableFuture<String> joinCaseUpdatePeopleJoin(String caseCode, int newCount) {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        JSONObject object = new JSONObject();
        JSONObject fields = new JSONObject();
        JSONObject peopleJoined = new JSONObject();

        peopleJoined.put(integerType,newCount);

        fields.put("peopleJoined", peopleJoined);

        object.put("fields",fields);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(object.toString(), headers);
        log.info("=====================");
        log.info(object.toString());
//        log.info(REALTIME_API_KNIGHTHALL.concat("/").concat(caseCode).concat("?updateMask.fieldPaths=peopleJoined"));
        ResponseEntity<String> response = restTemplate.exchange(REALTIME_API_KNIGHTHALL.concat("/").concat(caseCode).concat("?updateMask.fieldPaths=peopleJoined"), HttpMethod.PATCH, request, String.class);

        return CompletableFuture.completedFuture(response.toString());
    }

    @Async
    public CompletableFuture<String> createChasingCase(Case newCase) {

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        JSONObject object = new JSONObject();
        JSONObject fields = new JSONObject();
        JSONObject latitude = new JSONObject();
        JSONObject longitude = new JSONObject();
        JSONObject caseTag = new JSONObject();
        JSONObject itemType = new JSONObject();
        JSONObject createdTime = new JSONObject();
        JSONObject caseTagType = new JSONObject();
        JSONObject displayType = new JSONObject();
        JSONObject caseCode = new JSONObject();
//        JSONObject caseSource = new JSONObject();
        JSONObject userNameCreated = new JSONObject();
        JSONObject userPhone = new JSONObject();
        JSONObject caseName = new JSONObject();
        JSONObject userId = new JSONObject();

        itemType.put(integerType,2);

        caseCode.put(stringType,newCase.getCaseCode());
        latitude.put(doubleType, newCase.getLatitude());
        longitude.put(doubleType, newCase.getLongitude());
        caseTagType.put(integerType,newCase.getCaseTagType());
        caseName.put(stringType,newCase.getCaseName());
        caseTag.put(stringType,newCase.getCaseTag());
        createdTime.put(stringType,newCase.getCreatedTime());

        userId.put(integerType,newCase.getCreatedUser().getId());
        userNameCreated.put(stringType,newCase.getCreatedUser().getFullname());
        userPhone.put(stringType,newCase.getCreatedUser().getPhoneNumber());

        fields.put("id", caseCode);
        fields.put("itemType",itemType);
        fields.put("latitude",latitude);
        fields.put("longitude",longitude);
        fields.put("caseTagType",caseTagType);
        fields.put("caseTag",caseTag);
        fields.put("createdTime",createdTime);
        fields.put("name", userNameCreated);
        fields.put("phone",userPhone);
        fields.put("caseName",caseName);
        fields.put("userId",userId);


        object.put("fields",fields);



        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(object.toString(), headers);
        log.info("=====================");
        log.info(object.toString());

        String dataResponse = restTemplate.postForObject(REALTIME_API_KNIGHTHALL.concat("/?documentId=").concat(newCase.getCaseCode()), request, String.class);

        return CompletableFuture.completedFuture(dataResponse);
    }

    @Async
    public CompletableFuture<String> createCaseWithEvidence(Case newCase) {

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        JSONObject object = new JSONObject();
        JSONObject fields = new JSONObject();
        JSONObject latitude = new JSONObject();
        JSONObject longitude = new JSONObject();
        JSONObject caseTag = new JSONObject();
        JSONObject itemType = new JSONObject();
        JSONObject peopleLimit = new JSONObject();
        JSONObject peopleJoined = new JSONObject();
        JSONObject createdTime = new JSONObject();
        JSONObject caseTagType = new JSONObject();
        JSONObject displayType = new JSONObject();
        JSONObject caseCode = new JSONObject();
        JSONObject caseSource = new JSONObject();
        JSONObject userNameCreated = new JSONObject();
        JSONObject userPhone = new JSONObject();
        JSONObject caseName = new JSONObject();
        JSONObject userId = new JSONObject();

        itemType.put(integerType,2);

        caseCode.put(stringType,newCase.getCaseCode());
        latitude.put(doubleType, newCase.getLatitude());
        longitude.put(doubleType, newCase.getLongitude());
        caseTagType.put(integerType,newCase.getCaseTagType());
        caseName.put(stringType,newCase.getCaseName());
        peopleLimit.put(integerType,newCase.getPeopleLimit());
        caseTag.put(stringType,newCase.getCaseTag());
        createdTime.put(stringType,newCase.getCreatedTime());
        peopleJoined.put(integerType,0);
        caseSource.put(stringType,newCase.getCaseSource());

        userNameCreated.put(stringType,newCase.getCreatedUser().getFullname());
        userPhone.put(stringType,newCase.getCreatedUser().getPhoneNumber());
        userId.put(integerType,newCase.getCreatedUser().getId());

        fields.put("id", caseCode);
        fields.put("itemType",itemType);
        fields.put("latitude",latitude);
        fields.put("longitude",longitude);
        fields.put("caseTagType",caseTagType);
        fields.put("caseTag",caseTag);
        fields.put("peopleLimit",peopleLimit);
        fields.put("peopleJoined",peopleJoined);
        fields.put("createdTime",createdTime);
        fields.put("name", userNameCreated);
        fields.put("phone",userPhone);
        fields.put("caseName",caseName);
        fields.put("caseSource",caseSource);
        fields.put("userId",userId);


        object.put("fields",fields);



        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(object.toString(), headers);
        log.info("=====================");
        log.info(object.toString());

        String dataResponse = restTemplate.postForObject(REALTIME_API_KNIGHTHALL.concat("/?documentId=").concat(newCase.getCaseCode()), request, String.class);

        return CompletableFuture.completedFuture(dataResponse);
    }

    //Item type in firebase : 1 is knight
    //                        2 is case
    //
    @Async
    public CompletableFuture<String> createCase(Case newCase) {

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        JSONObject object = new JSONObject();
        JSONObject fields = new JSONObject();
        JSONObject latitude = new JSONObject();
        JSONObject longitude = new JSONObject();
        JSONObject caseTag = new JSONObject();
        JSONObject itemType = new JSONObject();
        JSONObject peopleLimit = new JSONObject();
        JSONObject peopleJoined = new JSONObject();
        JSONObject createdTime = new JSONObject();
        JSONObject caseTagType = new JSONObject();
        JSONObject displayType = new JSONObject();
        JSONObject caseCode = new JSONObject();
//        JSONObject caseSource = new JSONObject();
        JSONObject userNameCreated = new JSONObject();
        JSONObject userPhone = new JSONObject();
        JSONObject caseName = new JSONObject();
        JSONObject userId = new JSONObject();

        itemType.put(integerType,2);

        caseCode.put(stringType,newCase.getCaseCode());
        latitude.put(doubleType, newCase.getLatitude());
        longitude.put(doubleType, newCase.getLongitude());
        caseTagType.put(integerType,newCase.getCaseTagType());
        caseName.put(stringType,newCase.getCaseName());
        peopleLimit.put(integerType,newCase.getPeopleLimit());
        caseTag.put(stringType,newCase.getCaseTag());
        createdTime.put(stringType,newCase.getCreatedTime());
        peopleJoined.put(integerType,0);

        userNameCreated.put(stringType,newCase.getCreatedUser().getFullname());
        userPhone.put(stringType,newCase.getCreatedUser().getPhoneNumber());
        userId.put(integerType,newCase.getCreatedUser().getId());

        fields.put("id", caseCode);
        fields.put("itemType",itemType);
        fields.put("latitude",latitude);
        fields.put("longitude",longitude);
        fields.put("caseTagType",caseTagType);
        fields.put("caseTag",caseTag);
        fields.put("peopleLimit",peopleLimit);
        fields.put("peopleJoined",peopleJoined);
        fields.put("createdTime",createdTime);
        fields.put("name", userNameCreated);
        fields.put("phone",userPhone);
        fields.put("caseName",caseName);
        fields.put("userId",userId);


        object.put("fields",fields);



        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(object.toString(), headers);
        log.info("=====================");
        log.info(object.toString());

        String dataResponse = restTemplate.postForObject(REALTIME_API_KNIGHTHALL.concat("/?documentId=").concat(newCase.getCaseCode()), request, String.class);

        return CompletableFuture.completedFuture(dataResponse);
    }

    @Async
    public CompletableFuture<String> send(String url,HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        String dataResponse = restTemplate.postForObject(url, entity, String.class);

        return CompletableFuture.completedFuture(dataResponse);
    }

}
