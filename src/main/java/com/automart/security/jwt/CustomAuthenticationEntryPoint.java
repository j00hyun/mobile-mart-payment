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

/**
 * 시큐리티 필터에서 발생하는 예외를 처리
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Gson gson = new GsonBuilder().create();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        if(request.getAttribute("exception") == null) {

            /*
            Default: 로그인이 필요한 요청일때, Login URL로 Redirect 된다.
            하지만 안드로이드 앱에서는 Redirect가 불필요하므로 401 에러와 에러 메세지를 return 한다.
            */
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

        } else if(request.getAttribute("exception").equals("EXPIRED_TOKEN")) {

            /*
            Access Token이 만료되었지만, Refresh Token이 살아있는 경우 새로운 Access Token을 보내준다.
            JwtCommonAuthorizationFilter에서 발생한 ExpiredJwtException 처리
            */
            String accessToken = (String) request.getAttribute("token");
            setResponse(response, accessToken);

        } else if(request.getAttribute("exception").equals("BLOCKED_TOKEN")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그아웃된 유저입니다.");
        }
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
