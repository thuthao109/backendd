package com.capstone.kots.repository;

import com.capstone.kots.entity.PoliceWard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

public interface PoliceWardRepository extends JpaRepository<PoliceWard, Integer>, JpaSpecificationExecutor<PoliceWard> {
    @Override
    List<PoliceWard> findAll();

    @Query(value="SELECT w.ward_name FROM police_wards w " +
            "LEFT JOIN police_districts d on w.district_id = d.id " +
            "WHERE d.id = :districtId",nativeQuery = true)
    List<String> getWardNames(@Param("districtId") Integer districtId);

}

