package com.authorization.springauthorization.controller;

import com.authorization.springauthorization.pojo.AuthenticateRequest;
import com.authorization.springauthorization.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticateController {

    // in securityConfig i have created the bean for authentication manager

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTUtils  jwtUtils;

    @PostMapping("/authenticate") // this is an open request.
    public ResponseEntity<String> authenticateUser(@RequestBody AuthenticateRequest request){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            return ResponseEntity.ok(jwtUtils.generateToken(request.getUsername()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
