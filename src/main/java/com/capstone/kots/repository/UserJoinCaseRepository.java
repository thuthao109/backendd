package com.capstone.kots.repository;

import com.capstone.kots.entity.Case;
import com.capstone.kots.entity.UserJoinCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserJoinCaseRepository extends JpaRepository<UserJoinCase, Integer> {


//    @Query(value = "SELECT ujc.* FROM user_join_cases ujc LEFT JOIN cases c ON ujc.case_id = c.id",nativeQuery = true)
//    List<Case> findAvailableCase();
    List<UserJoinCase> findAll();


    Optional<List<UserJoinCase>> findByCaseId(int caseId);

    Optional<UserJoinCase> findByCaseIdAndUserId(Integer caseId,Integer userId);
}
