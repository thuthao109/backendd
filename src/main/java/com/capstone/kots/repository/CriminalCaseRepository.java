package com.capstone.kots.repository;

import com.capstone.kots.entity.CriminalCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriminalCaseRepository extends JpaRepository<CriminalCase, Integer> {
}
