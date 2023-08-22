package com.example.librarymanager.Services.Implement;

import com.example.librarymanager.DTOs.Login;
import com.example.librarymanager.Entity.UserEntity;
import com.example.librarymanager.Jwt.JwtTokenProvider;
import com.example.librarymanager.Jwt.UserDetail;
import com.example.librarymanager.Repository.UserRepository;
import com.example.librarymanager.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String login(Login login) throws Exception {
        if (login.getUserName().isEmpty() || login.getPassword().isEmpty()){
            throw new Exception("Fill your Username & Password!");
        } else {
            UserEntity user = userRepository.findByUserName(login.getUserName());
            if (user != null){
                try {
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    login.getUserName(),
                                    login.getPassword()
                            )
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    return tokenProvider.generateToken(((UserDetail) authentication.getPrincipal()).getUser());
                } catch (Exception e){
                    throw new Exception("Wrong password!");
                }
            } else throw new Exception("User not found!");
        }
    }
}
