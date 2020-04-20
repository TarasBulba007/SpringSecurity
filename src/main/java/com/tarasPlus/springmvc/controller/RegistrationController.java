package com.tarasPlus.springmvc.controller;

import com.tarasPlus.springmvc.model.User;
import com.tarasPlus.springmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegistrationController {

    private UserService service;
    private Environment environment;

    @Autowired
    public RegistrationController(UserService service, Environment environment) {
        this.service = service;
        this.environment = environment;
    }

    @GetMapping("/registration")
    public String reg(@ModelAttribute("message") String message) {
        return "registration";
    }

    @PostMapping("registration/user")
    public String regUser(@ModelAttribute("user") User user, Model model) {
        if(service.addUser(user)){
            if(user.getUsername().equalsIgnoreCase("goldarra")){
                service.addUserAdmin(user, "ADMIN");
            }
            return "redirect:/user";
        } else {
            model.addAttribute("message", environment.getRequiredProperty("invalidData"));
        }
        return "redirect:/registration";
    }
}