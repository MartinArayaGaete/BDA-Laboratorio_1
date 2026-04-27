package com.example.demo.dtos;

public class AuthResponseDTO {
    private String token;
    private UserInfoDTO userInfo;

    public AuthResponseDTO(String token, UserInfoDTO userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }

    public String getToken() { return token; }
    public UserInfoDTO getUserInfo() { return userInfo; }
}