package com.capstone.kots.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {
    @RequestMapping("/health")
    public ResponseEntity checkHealth(){
        return ResponseEntity.status(HttpStatus.OK).body("Ok");
    }
}
