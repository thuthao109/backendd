package com.capstone.kots.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CaseExceptions {

    @ResponseStatus(code = HttpStatus.OK, reason = "Vụ này đã có người xác nhận trước đó")
    public static class AlreadyJoinCase extends Exception{
        public AlreadyJoinCase(){
            super();
        }
        public AlreadyJoinCase(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.ACCEPTED, reason = "Vụ này đã có người xác nhận trước đó")
    public static class CaseAlreadyConfirmed extends Exception{
        public CaseAlreadyConfirmed(){
            super();
        }
        public CaseAlreadyConfirmed(String message){
            super(message);
        }
    }


    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Không có lý do từ chối")
    public static class RejectReasonRequired extends Exception{
        public RejectReasonRequired(){
            super();
        }
        public RejectReasonRequired(String message){
            super(message);
        }
    }


    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Chưa nhập giới hạn số người tham gia")
    public static class LimitPeopleRequired extends Exception{
        public LimitPeopleRequired(){
            super();
        }
        public LimitPeopleRequired(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Nhập loại vụ án")
    public static class CaseTagRequired extends Exception{
        public CaseTagRequired(){
            super();
        }
        public CaseTagRequired(String message){
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

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Dữ liệu tín hiệu không hợp lệ")
    public static class CaseTagTypeException extends Exception{
        public CaseTagTypeException(){
            super();
        }
        public CaseTagTypeException(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Không có dữ liệu người tạo")
    public static class NoCreatedUserDefineException extends Exception{
        public NoCreatedUserDefineException(){
            super();
        }
        public NoCreatedUserDefineException(String message){
            super(message);
        }
    }
}
