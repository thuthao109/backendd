package com.capstone.kots.repository;

import com.capstone.kots.entity.PoliceDistrict;
import com.capstone.kots.entity.PoliceWard;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PoliceDistrictRepository extends JpaRepository<PoliceDistrict, Integer>, JpaSpecificationExecutor<PoliceDistrict> {
    @Override
    List<PoliceDistrict> findAll();

    Optional<PoliceDistrict> findById(int id);

   // Optional<PoliceWard> findWardById(int districtId);

//    @Query(value="SELECT p.district_name FROM police_districts d " +
//            "LEFT JOIN police_wards w on w.district_id = d.id " +
//            "WHERE w.id = :wardId",nativeQuery = true)
//    List<String> getDistrictNames(@Param("wardId") Integer wardId);

  //  Optional<PoliceDistrict> findDistrictByDistrictName(String districtName);

    @Query(value="SELECT * FROM police_wards " +
            "WHERE district_id = :districtId",nativeQuery = true)
    List<String> getWardNames(@Param("districtId") Integer districtId);

}
