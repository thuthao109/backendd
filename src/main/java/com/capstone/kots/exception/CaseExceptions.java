package com.capstone.kots.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CaseExceptions {


    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Không có lý do từ chối")
    public static class RejectReasonRequired extends Exception{
        public RejectReasonRequired(){
            super();
        }
        public RejectReasonRequired(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Vụ án không hợp lệ")
    public static class CaseNotExisted extends Exception{
        public CaseNotExisted(){
            super();
        }
        public CaseNotExisted(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Vụ án đã đủ số người tham gia")
    public static class CaseIsFull extends Exception{
        public CaseIsFull(){
            super();
        }
        public CaseIsFull(String message){
            super(message);
        }
    }

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
