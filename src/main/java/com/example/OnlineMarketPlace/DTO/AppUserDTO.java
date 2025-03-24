package com.example.OnlineMarketPlace.DTO;




import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class AppUserDTO {

    private Long id;

    @NotEmpty
    @Email
    private String email;

    // ^(?=.*[a-z])            // At least one lowercase letter
    //(?=.*[A-Z])             // At least one uppercase letter
    //(?=.*\d)                // At least one digit
    //(?=.*[@#$!%*?&])         // At least one special character (@, #, $, !, %, *, ?, &)
    //[A-Za-z\d@#$!%*?&]+$     // Only allows letters, digits, and the specified special characters

    @Size(min = 8, message = "Minimum password length is 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&])[A-Za-z\\d@#$!%*?&]+$",
            message = "Password must contain at least 8 characters, including uppercase, lowercase, numbers, and special symbols (@#$!%*?&).")
    private String password;

    private String confirmPassword;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String role;

    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
