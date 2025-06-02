package com.kshrd.devconnect_springboot.service.implementation;

import com.kshrd.devconnect_springboot.model.entity.AppUser;
import com.kshrd.devconnect_springboot.respository.AppUserRepository;
import com.kshrd.devconnect_springboot.respository.AuthRepository;
import com.kshrd.devconnect_springboot.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserServiceImplementation implements AppUserService {
    private final AuthRepository authRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authRepository.getUserByEmail(email);
    }
}

