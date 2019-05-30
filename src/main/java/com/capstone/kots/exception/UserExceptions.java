package com.capstone.kots.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserExceptions {

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Username đã tồn tại")
    public static class UsernameExistedException extends Exception{
        public UsernameExistedException(){
            super();
        }
        public UsernameExistedException(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Mật khẩu không đúng")
    public static class UserWrongPasswordException extends Exception{
        public UserWrongPasswordException(){
            super();
        }
        public UserWrongPasswordException(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Không tìm thấy user trong hệ thống")
    public static class UserNotFoundException extends Exception{
        public UserNotFoundException(){
            super();
        }
        public UserNotFoundException(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Mã giới thiệu không đúng")
    public static class UserReferenceCodeNotExistException extends Exception{
        public UserReferenceCodeNotExistException(){
            super();
        }
        public UserReferenceCodeNotExistException(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Tài khoản này đã là player")
    public static class UserUpgradeException extends Exception{
        public UserUpgradeException(){
            super();
        }
        public UserUpgradeException(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Tài khoản đã tồn tại")
    public static class UserDuplicateException extends Exception{
        public UserDuplicateException(){
            super();
        }
        public UserDuplicateException(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Mã PIN không đúng")
    public static class UserWrongPinException extends Exception{
        public UserWrongPinException(){
            super();
        }
        public UserWrongPinException(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User này không phải là player!")
    public static class UserNotAPlayerException extends Exception{
        public UserNotAPlayerException(){
            super();
        }
        public UserNotAPlayerException(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Link donate đã có người đặt")
    public static class UserLinkDonateExisted extends Exception{
        public UserLinkDonateExisted(){
            super();
        }
        public UserLinkDonateExisted(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Giá thuê nhỏ nhất là 10")
    public static class UserRentPriceTooLow extends Exception{
        public UserRentPriceTooLow(){
            super();
        }
        public UserRentPriceTooLow(String message){
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Không thể thay đổi giá lúc đang thuê")
    public static class UserChangePriceRenting extends Exception{
        public UserChangePriceRenting(){
            super();
        }
        public UserChangePriceRenting(String message){
            super(message);
        }
    }
}
