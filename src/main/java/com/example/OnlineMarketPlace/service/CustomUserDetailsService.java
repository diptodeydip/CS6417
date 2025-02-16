package com.example.OnlineMarketPlace.service;

import com.example.OnlineMarketPlace.CustomUserDetails;
import com.example.OnlineMarketPlace.database.UserRepository;
import com.example.OnlineMarketPlace.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> appUser = repo.findByEmail(email);

        if (appUser.isPresent()){
            return new CustomUserDetails(appUser.get());
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
