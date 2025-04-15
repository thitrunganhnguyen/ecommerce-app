package com.ecommerce.demo.controller;

import com.ecommerce.demo.dto.user.SignInDto;
import com.ecommerce.demo.dto.user.SignInResponseDto;
import com.ecommerce.demo.dto.user.SignUpDto;
import com.ecommerce.demo.dto.user.SignUpResponseDto;
import com.ecommerce.demo.exceptions.CustomException;
import com.ecommerce.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;


    // Sign Up
    @PostMapping("/signup")
    public SignUpResponseDto signUp(@RequestBody SignUpDto signupDto) throws CustomException {
        return userService.signUp(signupDto);
    }

    // Sign In
    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInDto signInDto) throws CustomException{
        return userService.signIn(signInDto);
    }
}
