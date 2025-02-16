package com.example.OnlineMarketPlace.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/home")
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        return "welcome";
    }

}