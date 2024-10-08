package com.py.konecta.base.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.py.konecta.base.dto.CustomResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 * Class for the user  unauthenticated handling
 * */
@Component
@Slf4j
class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException
    ) throws IOException {

        final String expired = (String) request.getAttribute("expired");
        final String message = expired != null && !expired.isEmpty() ? expired : authException.getMessage();

        var customResponse = new CustomResponse<>(message, Boolean.TRUE, null);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonResponse = ow.writeValueAsString(customResponse);

        log.error("Authentication entry point: {}", jsonResponse);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(jsonResponse);
    }
}
