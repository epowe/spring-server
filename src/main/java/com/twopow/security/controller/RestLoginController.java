package com.twopow.security.controller;

import com.twopow.security.config.auth.PrincipalDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class RestLoginController {
//    @GetMapping("/oauth2/redirect")
//    public JoinedUser oauthRedirect(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth){
//
//        ObjectMapper objectMapper=new ObjectMapper();
//        PrincipalDetails principalDetails=(PrincipalDetails)authentication.getPrincipal();
//
//        String email = principalDetails.getUser().getEmail();
//        String name = principalDetails.getUser().getName();
//        String picture = principalDetails.getUser().getPicture();
//        String encodedname = null;
//        try {
//            encodedname = URLEncoder.encode(name, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//
//        String jwtToken = JWT.create()
//                .withSubject("2-pow Token")
//                .withExpiresAt(new Date(System.currentTimeMillis() + (60000) * 30)) //30min
//                .withClaim("id", principalDetails.getUser().getId())
//                .withClaim("username", principalDetails.getUser().getUsername())
//                .sign(Algorithm.HMAC512("2powTeam"));
//
//        JoinedUser joinedUser=new JoinedUser();
//        joinedUser.setUserToken(jwtToken);
//        joinedUser.setEmail(email);
//        joinedUser.setUsername(encodedname);
//        joinedUser.setProfileImageUrl(picture);
//
//        return joinedUser;
//    }
    @GetMapping("/hello")
    public String Hello(){
        return "hello";
    }



    @GetMapping("/afterLogin")
    public void afterLogin(HttpServletResponse response, @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        Collection<? extends GrantedAuthority> roles = principalDetails.getAuthorities();
        if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))){
            response.sendRedirect("http://localhost:3000/Register");
        } else if(roles.contains(new SimpleGrantedAuthority(("ROLE_VERIFIED")))) {
          response.sendRedirect("http://localhost:3000/Interview");
        }
        else{
            response.sendRedirect("http://localhost:3000");
        }
    }
}
