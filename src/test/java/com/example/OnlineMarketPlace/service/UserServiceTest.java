package com.example.OnlineMarketPlace.service;


import com.example.OnlineMarketPlace.DTO.AppUserDTO;
import com.example.OnlineMarketPlace.database.UserRepository;
import com.example.OnlineMarketPlace.model.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testUpdateProfile_Success() {
        // Arrange (Mock Data)
        String email = "test@example.com";
        AppUser existingUser = new AppUser();
        existingUser.setEmail(email);
        existingUser.setFirstName("OldFirstName");
        existingUser.setLastName("OldLastName");

        AppUserDTO updatedUserDTO = new AppUserDTO();
        updatedUserDTO.setFirstName("NewFirstName");
        updatedUserDTO.setLastName("NewLastName");
        updatedUserDTO.setEmail("updated@example.com");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // Act
        userService.updateProfile(email, updatedUserDTO);

        // Assert
        assertEquals("NewFirstName", existingUser.getFirstName());
        assertEquals("NewLastName", existingUser.getLastName());
        assertEquals("updated@example.com", existingUser.getEmail());
        verify(userRepository, times(1)).save(existingUser); // Ensure save() is called
    }

    @Test
    void testUpdateProfile_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        AppUserDTO updatedUserDTO = new AppUserDTO();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            userService.updateProfile(email, updatedUserDTO);
        });

        assertEquals("No value present", exception.getMessage()); // This is what Optional.get() throws
        verify(userRepository, never()).save(any()); // Ensure save() is NOT called
    }
}