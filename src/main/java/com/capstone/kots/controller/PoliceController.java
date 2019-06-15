package com.capstone.kots.controller;


import com.capstone.kots.entity.PoliceDistrict;
import com.capstone.kots.entity.PoliceWard;
import com.capstone.kots.service.PoliceDistrictService;
import com.capstone.kots.service.PoliceWardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class PoliceController {
    private final PoliceDistrictService districtService;
    private final PoliceWardService wardService;

    @Autowired
    public PoliceController(PoliceDistrictService districtService, PoliceWardService wardService) {
        this.districtService = districtService;
        this.wardService = wardService;
    }


    //get all district
    @RequestMapping(value = "/districts", method = RequestMethod.GET)
    public ResponseEntity getAllDistrict(){
        List<PoliceDistrict> districts=districtService.getAllDistrict();
        if (districts == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(districts);
    }

    @RequestMapping(value = "/wards", method = RequestMethod.GET)
    public ResponseEntity getAll(){
        List<PoliceWard> districts=wardService.getAllWard();
        if (districts == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(districts);
    }

    @RequestMapping(value = "/wards/{districtId}", method = RequestMethod.GET)
    public ResponseEntity getWardByDistrictId(@PathVariable("districtId") Integer districtId){
        List<String> wards=wardService.findWardById(districtId);
        if (wards == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(wards);
    }
}
