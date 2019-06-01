package com.capstone.kots.repository;

import com.capstone.kots.entity.TracePoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TracePointRepository extends JpaRepository<TracePoint, Integer> {
}
