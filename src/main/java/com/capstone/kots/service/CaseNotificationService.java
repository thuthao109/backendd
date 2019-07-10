package com.capstone.kots.service;

import com.capstone.kots.entity.Case;
import com.capstone.kots.entity.CaseNotification;
import com.capstone.kots.repository.CaseNotificationRepository;
import com.capstone.kots.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CaseNotificationService {

    private final CaseNotificationRepository caseNotificationRepository;
    private final CaseRepository caseRepository;

    @Autowired
    public CaseNotificationService(CaseNotificationRepository caseNotificationRepository,
                                   CaseRepository caseRepository) {
        this.caseNotificationRepository = caseNotificationRepository;
        this.caseRepository = caseRepository;
    }


    public Optional<CaseNotification> readNotification(Integer notifId){
        Optional<CaseNotification> notif = caseNotificationRepository.findById(notifId);
        if(notif.isPresent()){
            notif.get().setRead(true);
            Optional<Case> caseOne = caseRepository.findById(notif.get().getNotificationCaseSourceId());
            notif.get().setCaseOne(caseOne.get());
            caseNotificationRepository.save(notif.get());
        }else {
            return null;
        }

        return notif;
    }
}
