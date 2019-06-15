package com.capstone.kots.service;

import com.capstone.kots.entity.PoliceDistrict;
import com.capstone.kots.repository.PoliceDistrictRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class PoliceDistrictService {
    private final PoliceDistrictRepository districtRepository;

    @Autowired
    public PoliceDistrictService(PoliceDistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public List<PoliceDistrict> getAllDistrict(){
        List<PoliceDistrict> districtList=districtRepository.findAll();
        return districtList;
    }

//    public List<String> getDistrictName(Integer wardId){
////        log.info("Call getDistrictName method with wardId is "+wardId);
//        return districtRepository.getDistrictNames(wardId);
//    }
}
