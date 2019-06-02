package com.capstone.kots.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CaseExceptions {


    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Không có dữ liệu chứng minh")
    public static class EvidenceNotExistedException extends Exception{
        public EvidenceNotExistedException(){
            super();
        }
        public EvidenceNotExistedException(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Không có dữ liệu vị trí")
    public static class CoordinateNotExistedException extends Exception{
        public CoordinateNotExistedException(){
            super();
        }
        public CoordinateNotExistedException(String message){
            super(message);
        }
    }
}
