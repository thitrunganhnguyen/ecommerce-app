package com.ecommerce.demo.service;

import com.ecommerce.demo.config.MessageStrings;
import com.ecommerce.demo.exceptions.AuthentificationFailException;
import com.ecommerce.demo.model.AuthenticationToken;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.AuthenticationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationTokenService {
    @Autowired
    AuthenticationTokenRepo authenticationTokenRepo;

    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        authenticationTokenRepo.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {
        return authenticationTokenRepo.findByUser(user);
    }

    public User getUser(String token) {
        final AuthenticationToken authenticationToken = authenticationTokenRepo.findByToken(token);
        if (authenticationToken == null) {
            return null;
        }
        return authenticationToken.getUser();
    }

    public void authenticate(String token) throws AuthentificationFailException{
        if (token == null) {
            throw new AuthentificationFailException(MessageStrings.AUTH_TOEKN_NOT_PRESENT);
        }
        User user = getUser(token);
        if (user == null) {
            throw new AuthentificationFailException(MessageStrings.AUTH_TOEKN_NOT_VALID);
        }
    }
}
