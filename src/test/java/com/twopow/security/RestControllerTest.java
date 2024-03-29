package com.twopow.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.twopow.security.config.auth.PrincipalDetails;
import com.twopow.security.jwt.JwtUtil;
import com.twopow.security.model.User;
import com.twopow.security.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserRepository userRepository;
    private MockMvc mvc;
    private User user;
    private String jwtToken;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        user = User.builder()
                .name("홍길동")
                .username("naver_123456789")
                .email("RedRoadCopper@naver.com")
                .role("ROLE_USER")
                .provider("naver")
                .providerId("123456789")
                .picture("https://ssl.pstatic.net/static/newsstand/up/2013/0813/nsd114029379.gif")
                .build();
        userRepository.save(user);

        jwtToken = JwtUtil.CreateToken(user,JwtUtil.Minutes(1));
    }


    @Test
    public void 회원정보를가져온다() throws Exception {
        PrincipalDetails principalDetails = new PrincipalDetails(user, null);
        mvc.perform(get("/auth/info").with(user(principalDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username",is(user.getName())))
                .andExpect(jsonPath("$.email",is(user.getEmail())))
                .andExpect(jsonPath("$.picture",is(user.getPicture())));

    }
}