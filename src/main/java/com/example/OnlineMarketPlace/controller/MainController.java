package com.example.OnlineMarketPlace.controller;
import com.example.OnlineMarketPlace.DTO.AppUserDTO;
import com.example.OnlineMarketPlace.database.UserRepository;
import com.example.OnlineMarketPlace.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("index")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("loginPage")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("registerPage")
    public String register(Model model) {
        AppUserDTO appUserDTO = new AppUserDTO();
        model.addAttribute(appUserDTO);
        model.addAttribute("success", false);
        return "register";
    }

    @PostMapping("register")
    public String processRegister(Model model, @Valid @ModelAttribute AppUserDTO appUserDTO
    , BindingResult result) {

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
            return "register";
        }

        try {
            AppUser newUser = new AppUser();
            newUser.setFirstName(appUserDTO.getFirstName());
            newUser.setLastName(appUserDTO.getLastName());
            newUser.setEmail(appUserDTO.getEmail());
            newUser.setCreatedAt(new Date());
            newUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));

            repo.save(newUser);

            model.addAttribute("appUserDTO", new AppUserDTO());
            model.addAttribute("success", true);

        }catch (Exception e){
            result.addError(
                    new FieldError("appUserDTO", "firstName",
                            e.getMessage())
            );
        }

        return "register";
    }

}