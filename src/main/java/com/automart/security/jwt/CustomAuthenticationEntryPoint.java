package com.automart.security.jwt;

import com.automart.user.dto.AuthResponseDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Gson gson = new GsonBuilder().create();

    /**
     * JwtCommonAuthorizationFilter에서 발생한 ExpiredJwtException 처리
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String accessToken = (String) request.getAttribute("token");
        setResponse(response, accessToken);
    }

    /**
     * 새롭게 발급된 access token을 json 형식으로 body에 넣음
     */
    private void setResponse(HttpServletResponse response, String accessToken) throws IOException {
        response.setStatus(401);
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(gson.toJson(new AuthResponseDto(accessToken)));
        writer.flush();
        writer.close();
    }
}
