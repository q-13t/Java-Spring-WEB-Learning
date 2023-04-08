package com.test.learn.godbless.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        HttpSession session = request.getSession();
        session.setAttribute("redirectURL", request.getRequestURL());
        session.setAttribute("redirectParameters", request.getParameterMap());
        response.sendRedirect("/hello");
    }

}
