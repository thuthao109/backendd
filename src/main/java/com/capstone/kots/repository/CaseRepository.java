package com.capstone.kots.repository;

import com.capstone.kots.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CaseRepository extends JpaRepository<Case, Integer> {
    @Override
    List<Case> findAll();

}
