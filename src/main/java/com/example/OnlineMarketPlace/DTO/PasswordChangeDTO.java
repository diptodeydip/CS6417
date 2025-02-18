package com.example.OnlineMarketPlace.DTO;

import javax.validation.constraints.Size;

public class PasswordChangeDTO {
    private String currentPassword;

    @Size(min = 8, message = "Minimum password length is 8 characters")
    private String newPassword;

    private String confirmPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}
