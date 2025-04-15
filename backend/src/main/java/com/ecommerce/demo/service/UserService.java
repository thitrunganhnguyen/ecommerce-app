package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.user.SignInDto;
import com.ecommerce.demo.dto.user.SignInResponseDto;
import com.ecommerce.demo.dto.user.SignUpDto;
import com.ecommerce.demo.dto.user.SignUpResponseDto;
import com.ecommerce.demo.enums.ResponseStatus;
import com.ecommerce.demo.exceptions.AuthentificationFailException;
import com.ecommerce.demo.exceptions.CustomException;
import com.ecommerce.demo.model.AuthenticationToken;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.UserRepo;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static com.ecommerce.demo.config.MessageStrings.USER_CREATED;
import static com.ecommerce.demo.config.MessageStrings.WRONG_PASSWORD;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthenticationTokenService authenticationTokenService;

    Logger logger= LoggerFactory.getLogger(UserService.class);

    @Transactional
    public SignUpResponseDto signUp(SignUpDto signupDto) {

        // Check to see if the current email address has already been registered
        if (Objects.nonNull(userRepo.findByEmail(signupDto.getEmail()))) {
            throw new CustomException("User already exists");
        }

        // hash the password
        String encryptedPassword;
        try {
            encryptedPassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("hashing password failed {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }
        
        // save the user
        User user= new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), encryptedPassword);

        User createdUser;

        try {
            createdUser = userRepo.save(user);
            // create token and save token in database
            final AuthenticationToken authenticationToken = new AuthenticationToken(user);
            authenticationTokenService.saveConfirmationToken(authenticationToken);
            return new SignUpResponseDto(ResponseStatus.success.toString(), USER_CREATED);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        return hash;

    }

    public SignInResponseDto signIn(SignInDto signInDto) {
        // find user by email
        User user = userRepo.findByEmail(signInDto.getEmail());

        if (Objects.isNull(user)) {
            throw new AuthentificationFailException("user is not present");
        }

        // compare the password in DB
        try {
            if(!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                throw new AuthentificationFailException(WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // if password match retrieve token
        AuthenticationToken token = authenticationTokenService.getToken(user);
        if(Objects.isNull(token)) {
            throw new CustomException("token is not present");
        }
        // return response
        return new SignInResponseDto("success", token.getToken());
    }
}
