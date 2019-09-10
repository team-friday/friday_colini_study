package com.colini.study.api.controller;

import com.colini.study.api.exception.EntityNotFoundException;
import com.colini.study.api.constants.FailureResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = EntityNotFoundException.class)
    public FailureResponse handleNotExistEntity(EntityNotFoundException e){
        log.error("handleNotExistEntity : {}" , e);
        return new FailureResponse(e.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value()));
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(value = Exception.class)
//    public FailureResponse handleSuperException(EntityNotFoundException e){
//        log.error("handleSuperException : {}" , e);
//        return new FailureResponse(e.getMessage(), String.valueOf(HttpStatus.BAD_REQUEST.value()));
//    }
}
