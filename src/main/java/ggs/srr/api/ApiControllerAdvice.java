package ggs.srr.api;

import ggs.srr.api.controller.jwt.exception.JwtControllerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JwtControllerException.class)
    public ApiResponse<Object> jwtControllerException(JwtControllerException e) {
        return ApiResponse.badRequest(e);
    }

}
