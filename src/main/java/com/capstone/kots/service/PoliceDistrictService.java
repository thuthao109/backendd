package com.capstone.kots.service;

import com.capstone.kots.entity.PoliceDistrict;
import com.capstone.kots.entity.PoliceWard;
import com.capstone.kots.repository.PoliceDistrictRepository;
import com.capstone.kots.repository.PoliceWardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PoliceDistrictService {
    private final PoliceDistrictRepository districtRepository;
    private final PoliceWardRepository wardRepository;
    private List<PoliceDistrict> districtList;
    private List<PoliceWard> wardList;

    @Autowired
    public PoliceDistrictService(PoliceDistrictRepository districtRepository, PoliceWardRepository wardRepository, List<PoliceDistrict> districtList, List<PoliceWard> wardList) {
        this.districtRepository = districtRepository;
        this.wardRepository = wardRepository;
        this.districtList = districtList;
        this.wardList = wardList;
    }

    public List<PoliceDistrict> getAllDistrict() {
        List<PoliceDistrict> result=new ArrayList<>();
        districtList = districtRepository.findAll();
        for (int i = 0; i < districtList.size(); i++) {
            PoliceDistrict d = districtList.get(i);
            Integer id = d.getId();
           // result = new ArrayList<>();
            int districtId=d.getId();
            String name = d.getDistrictName();
            String phone=d.getDistrictPhone();
            result.add(new PoliceDistrict(districtId,name,phone,getListWard(id)));
        }
        return result;
    }


//    public List<PoliceWard> getListWard(int districtId) {
//        return wardRepository.getWardNames(districtId);
//    }



    public List<PoliceWard> getListWard(Integer districtId) {
        return  (List)wardRepository.getWardNames(districtId);

    }
}
