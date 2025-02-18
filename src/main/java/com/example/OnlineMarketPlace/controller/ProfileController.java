package com.example.OnlineMarketPlace.controller;

import com.example.OnlineMarketPlace.Commons;
import com.example.OnlineMarketPlace.DTO.AppUserDTO;
import com.example.OnlineMarketPlace.DTO.FeedbackDTO;
import com.example.OnlineMarketPlace.DTO.PasswordChangeDTO;
import com.example.OnlineMarketPlace.DTO.ProductDTO;
import com.example.OnlineMarketPlace.database.FeedbackRepository;
import com.example.OnlineMarketPlace.database.UserRepository;
import com.example.OnlineMarketPlace.model.AppUser;
import com.example.OnlineMarketPlace.model.Feedback;
import com.example.OnlineMarketPlace.model.Product;
import com.example.OnlineMarketPlace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository repo;

    @Autowired
    private FeedbackRepository feedbackRepo;

    @GetMapping("profile")
    public String showProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Fetch logged-in username
        Optional<AppUser> appUser = repo.findByEmail(email);

        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setEmail(appUser.get().getEmail());
        appUserDTO.setFirstName(appUser.get().getFirstName());
        appUserDTO.setLastName(appUser.get().getLastName());
        appUserDTO.setCreatedAt(appUser.get().getCreatedAt());
        model.addAttribute(appUserDTO);
        return "user_profile";
    }

    @PostMapping("updateProfile")
    public String updateProfile(Model model, @Valid @ModelAttribute AppUserDTO appUserDTO
            , BindingResult result, HttpSession session) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Fetch logged-in username

        Optional<AppUser> appUser = repo.findByEmail(appUserDTO.getEmail());
        if (appUser.isPresent() && !appUser.get().getEmail().equals(appUserDTO.getEmail()))
        {
            result.addError(
                    new FieldError("appUserDTO", "email",
                            "Email address is already used")
            );
        }
        if (result.hasErrors()){
            return "user_profile";
        }
        userService.updateProfile(email, appUserDTO);
        session.setAttribute(Commons.name, appUserDTO.getFirstName()+ " " + appUserDTO.getLastName());
        model.addAttribute("message", "Profile updated.");
        return "user_profile";
    }

    @GetMapping("changePasswordForm")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
        return "change_password";
    }


    // Handle password change
    @PostMapping("changePassword")
    public String changePassword(@Valid @ModelAttribute PasswordChangeDTO passwordChangeDTO, BindingResult result, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmPassword()))
        {
            result.addError(
                    new FieldError("passwordChangeDTO", "confirmPassword",
                            "Password didn't match")
            );

        }

        if (result.hasErrors()){
            return "change_password";
        }

        boolean isPasswordChanged = userService.changePassword(username, passwordChangeDTO);
        if (isPasswordChanged) {
            model.addAttribute("message", "Password changed successfully.");
        } else {
            model.addAttribute("message", "Failed to change password. Please check your current password.");
        }

        model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
        return "change_password";
    }

    @PostMapping("/submitFeedback")
    public String submitFeedback(@Valid @ModelAttribute FeedbackDTO feedbackDTO, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Fetch logged-in username
        Feedback feedback = new Feedback();
        feedback.setDescription(feedbackDTO.getDescription());
        feedback.setEmail(email);
        feedback.setCreatedAt(new Date());
        feedbackRepo.save(feedback);

        model.addAttribute("feedbackDTO", new FeedbackDTO());
        model.addAttribute("message", "Feedback Sent");
        return "feedback";
    }

    @GetMapping("feedback")
    public String feedback( Model model) {
        model.addAttribute("feedbackDTO", new FeedbackDTO());
        return "feedback";
    }

    @GetMapping("feedbacks")
    public String feedbacks( Model model) {
        model.addAttribute("feedbacks", feedbackRepo.findAll());
        return "feedbacks";
    }
}
