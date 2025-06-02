package com.kshrd.devconnect_springboot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser implements UserDetails {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String profileImageUrl;
    private Boolean isRecruiter;
    private Boolean isVerified;
    private LocalDateTime createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(isRecruiter ? "ROLE_RECRUITER" : "ROLE_DEVELOPER"));
        return simpleGrantedAuthorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
