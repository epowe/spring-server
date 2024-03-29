package com.twopow.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Slf4j
@Configuration
public class CorsConfig {
    @Value("${server.react.scheme}")
    private String reactScheme;
    @Value("${server.react.host}")
    private String reactHost;
    @Value("${server.react.port}")
    private String reactPort;

    private String reactPath = "/";

    @Bean
    public CorsFilter corsFilter() {
        //기본 포트인 80 포트라면 내용 없애기
        if (Objects.equals(reactPort, "80")) {
            reactPort = null;
            reactPath = "";
        }
        String originUrl = UriComponentsBuilder
                .newInstance()
                .scheme(reactScheme)
                .host(reactHost)
                .port(reactPort)
                .path(reactPath)
                .encode()
                .build()
                .toUriString();
        log.trace(originUrl);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);//내 서버가 응답을 할때 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        config.addAllowedOrigin(originUrl);//모든 ip에 응답을 허용
        config.addAllowedHeader("*");//모든 header에 응답을 허용
        config.addAllowedMethod("*");//모든 post,get,put,delete,patch에 응답을 허용
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


}
