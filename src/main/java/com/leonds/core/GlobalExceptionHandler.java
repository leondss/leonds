package com.leonds.core;

import com.leonds.core.resp.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Leon
 */
@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    Response handleException(ServiceException e) {
        log.error(e);
        return Response.err(e.getMessage()).build();
    }

}
