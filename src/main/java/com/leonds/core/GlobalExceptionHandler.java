package com.leonds.core;

import com.leonds.core.resp.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Leon
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    Response handleException(ServiceException e) {
        return Response.err(e.getMessage()).build();
    }

}
