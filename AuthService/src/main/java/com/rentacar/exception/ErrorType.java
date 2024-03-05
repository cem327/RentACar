package com.rentacar.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorType {

    INTERNAL_ERROR(1000, "Unexpected error on the server. Please try again later.",HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR_DUPLICATE_USERNAME(2000,"Username exists",HttpStatus.BAD_REQUEST),
    ERROR_INVALID_LOGIN_PARAMETER(2001,"Invalid username/password.",HttpStatus.BAD_REQUEST),
    ERROR_CREATE_TOKEN(1003,"Token creation error.",HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST_ERROR(1001, "Invalid parameters.", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatus httpStatus;
}
