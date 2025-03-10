package com.example.OnlineMarketPlace.controller;
import com.example.OnlineMarketPlace.Commons;
import com.example.OnlineMarketPlace.DTO.AppUserDTO;
import com.example.OnlineMarketPlace.DTO.FeedbackDTO;
import com.example.OnlineMarketPlace.DTO.PasswordChangeDTO;
import com.example.OnlineMarketPlace.database.UserRepository;
import com.example.OnlineMarketPlace.model.AppUser;
import com.example.OnlineMarketPlace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class MainController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("index")
    public String index(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Fetch logged-in username
        Optional<AppUser> appUser = repo.findByEmail(email);

        // Store username in session
        session.setAttribute(Commons.name, appUser.get().getFirstName()+ " " + appUser.get().getLastName());
        session.setAttribute(Commons.userId, appUser.get().getId());
        session.setAttribute(Commons.role, appUser.get().getRole());

        return "index";
    }

    @GetMapping("loginPage")
    public String loginPage(Model model) {
        return "login_page";
    }

    @GetMapping("registerPage")
    public String register(Model model) {
        AppUserDTO appUserDTO = new AppUserDTO();
        model.addAttribute(appUserDTO);
        return "register_page";
    }

    @PostMapping("register")
    public String processRegister(Model model, @Valid @ModelAttribute AppUserDTO appUserDTO
    , BindingResult result, RedirectAttributes redirectAttributes) {

        if (!appUserDTO.getPassword().equals(appUserDTO.getConfirmPassword()))
        {
            result.addError(
                    new FieldError("appUserDTO", "confirmPassword",
                            "Password didn't match")
            );
        }

        Optional<AppUser> appUser = repo.findByEmail(appUserDTO.getEmail());
        if (appUser.isPresent())
        {
            result.addError(
                    new FieldError("appUserDTO", "email",
                            "Email address is already used")
            );
        }

        if (result.hasErrors()){
            return "register_page";
        }

        try {
            AppUser newUser = new AppUser();
            newUser.setFirstName(appUserDTO.getFirstName());
            newUser.setLastName(appUserDTO.getLastName());
            newUser.setEmail(appUserDTO.getEmail());
            newUser.setCreatedAt(new Date());
            newUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
            newUser.setRole(Commons.ROLE_USER);

            repo.save(newUser);

        }catch (Exception e){
            result.addError(
                    new FieldError("appUserDTO", "firstName",
                            e.getMessage())
            );
            return "register_page";
        }

        redirectAttributes.addFlashAttribute("message", "Account created successfully!");
        return "redirect:/loginPage";
    }

}