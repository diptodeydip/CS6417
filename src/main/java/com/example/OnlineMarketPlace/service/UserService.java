package com.example.OnlineMarketPlace.service;

import com.example.OnlineMarketPlace.DTO.PasswordChangeDTO;
import com.example.OnlineMarketPlace.database.UserRepository;
import com.example.OnlineMarketPlace.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean changePassword(String email, PasswordChangeDTO passwordChangeDTO) {
        // Fetch the user by username
        Optional<AppUser> user = userRepository.findByEmail(email);

        // Check if the current password matches
        if (passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), user.get().getPassword())) {
            // Encode the new password and set it
            String encodedPassword = passwordEncoder.encode(passwordChangeDTO.getNewPassword());
            user.get().setPassword(encodedPassword);
            userRepository.save(user.get());
            return true;
        } else {
            return false; // Current password is incorrect
        }
    }
}
