package com.test.learn.godbless.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.boot.web.servlet.error.ErrorController;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Component
@ControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView handleEHttpStatusBadRequestSException(Exception e) {
        System.out.println(e.getMessage());
        ModelAndView mav = new ModelAndView("/errors/generic");
        String errorMessage = "Error: " + e.getMessage() + " Occurred!";
        mav.addObject("error_text", errorMessage);
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        System.out.println(e.getMessage());
        ModelAndView mav = new ModelAndView("/errors/generic");
        String errorMessage = "Error: " + e.getMessage() + " Occurred!";
        mav.addObject("error_text", errorMessage);
        return mav;
    }
}

@Controller
class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleException(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/errors/generic");
        Object httpstatus = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        System.out.println(httpstatus);
        String errorMessage = "Error: " + HttpStatus.valueOf((Integer) httpstatus).name() + " Occurred!";
        mav.addObject("error_text", errorMessage);
        return mav;
    }
}