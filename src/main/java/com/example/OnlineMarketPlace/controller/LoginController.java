package com.example.OnlineMarketPlace.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LoginController {

    @PostMapping("/login")
    public String login(Model model) {
        return "Welcome";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }
}