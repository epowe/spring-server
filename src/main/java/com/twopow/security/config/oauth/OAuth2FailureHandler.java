package com.twopow.security.config.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${server.react.scheme}")
    private String reactScheme;
    @Value("${server.react.host}")
    private String reactHost;
    @Value("${server.react.port}")
    private String reactPort;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("[Authentication Failure] 오류가 생겨 back-end 에서 프론트의 메인 페이지로 redirect 합니다.");
        if (Objects.equals(reactPort, "80")){
            reactPort=null;
        }
        String targetUrl = UriComponentsBuilder
                .newInstance()
                .scheme(reactScheme)
                .host(reactHost)
                .port(reactPort)
                .path("/")
                .encode()
                .build()
                .toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}