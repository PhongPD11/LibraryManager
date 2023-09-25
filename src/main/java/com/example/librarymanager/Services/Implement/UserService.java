package com.example.librarymanager.Services.Implement;

import com.example.librarymanager.Entity.UserEntity;
import com.example.librarymanager.Jwt.UserDetail;
import com.example.librarymanager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Transactional
    public UserDetail loadUserById(Long id) throws Exception {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );
        return new UserDetail(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetail(user);
    }
}
