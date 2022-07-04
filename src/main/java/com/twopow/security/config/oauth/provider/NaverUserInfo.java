package com.twopow.security.config.oauth.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{

    public NaverUserInfo(Map<String,Object> attributes){
        this.attributes=attributes;
    }
    private Map<String,Object> attributes;//oauth2User.getAttributes()

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}