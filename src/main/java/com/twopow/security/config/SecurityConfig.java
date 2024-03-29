package com.twopow.security.config;
//구글 로그인이 완료된 뒤의 후처리가 필요함. 1.코드받기(인증),2.액세스토큰(권한),3.사용자 프로필 정보를 가져오고
//4-1.그 정보를 토대로 자동 회원가입
//4-2 추가정보 받아서 회원가입

import com.twopow.security.config.oauth.OAuth2FailureHandler;
import com.twopow.security.config.oauth.OAuth2SuccessHandler;
import com.twopow.security.config.oauth.PrincipalOauth2UserService;
import com.twopow.security.jwt.JwtAuthenticationFilter;
import com.twopow.security.jwt.JwtAuthorizationFilter;
import com.twopow.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity//스프링 시큐리티 필터(밑에오는 SecurityConfig)가 스프링 필터체인에 등록됨.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor


public class
SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CorsFilter corsFilter;
    private final UserRepository userRepository;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;
    private final PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public BCryptPasswordEncoder encoderPwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter)
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository));


        http.exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/auth/info/**").authenticated()
                .antMatchers("/register/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .loginPage("/") //Tip.코드X 액세스토큰+사용자프로필정보를 한방에 받는다.
                .failureHandler(failureHandler)
                .successHandler(successHandler)
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
    }
}

