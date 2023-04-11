package com.test.learn.godbless.exceptions;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        System.out.println(e.getMessage());
        ModelAndView mav = new ModelAndView("/errors/generic");
        mav.addObject("error_text", e.getMessage());
        return mav;
    }

}
