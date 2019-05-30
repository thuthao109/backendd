package com.capstone.kots.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class RoleExceptions {
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Dữ liệu role không tồn tại trong hệ thống")
    public static class RoleNotExistException extends Exception{
        public RoleNotExistException(){
            super();
        }
        public RoleNotExistException(String message){
            super(message);
        }
    }
}
