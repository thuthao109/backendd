package com.capstone.kots.service;

import com.capstone.kots.entity.PoliceWard;
import com.capstone.kots.repository.PoliceDistrictRepository;
import com.capstone.kots.repository.PoliceWardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PoliceWardService {
    private final PoliceDistrictRepository districtRepository;
    private final PoliceWardRepository wardRepository;

    @Autowired
    public PoliceWardService(PoliceDistrictRepository districtRepository, PoliceWardRepository wardRepository) {
        this.districtRepository = districtRepository;
        this.wardRepository = wardRepository;
    }

    public List<PoliceWard> getAllWard(){
        List<PoliceWard> wardList=wardRepository.findAll();
        return wardList;
    }

    public List<String> findWardById(Integer districtId){
        return  wardRepository.getWardNames(districtId);
    }


}
