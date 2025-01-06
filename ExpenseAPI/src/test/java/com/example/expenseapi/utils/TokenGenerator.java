package com.example.expenseapi.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {

    @Autowired
    private JwtUtil jwtUtil;

    public  String getToken(String username) {
        return jwtUtil.generateToken(username);
    }
}
