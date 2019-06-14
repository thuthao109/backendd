package com.capstone.kots.repository;

import com.capstone.kots.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseRepository extends JpaRepository<Case, Integer> {
    @Override
    List<Case> findAll();
}
