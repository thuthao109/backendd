package com.capstone.kots.repository;


import com.capstone.kots.entity.CaseNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CaseNotificationRepository extends JpaRepository<CaseNotification, Integer> {

    Optional<List<CaseNotification>> findByUserId(int userId);
}
