package com.capstone.kots.repository;

import com.capstone.kots.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CaseRepository extends JpaRepository<Case, Integer> {
    @Override
    List<Case> findAll();

//    @Override
//    Optional<Case> findById(Integer integer);

    @Query(value = "SELECT * FROM cases c WHERE c.deleted_time is null", nativeQuery = true)
    List<Case> findActiveCases();

    @Query(value = "SELECT * FROM cases c WHERE c.deleted_time is null AND c.id = :case_id ", nativeQuery = true)
    Optional<Case> findActiveCaseById(@Param("case_id") Integer caseId);
}


