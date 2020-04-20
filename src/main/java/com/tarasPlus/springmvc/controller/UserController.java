package com.tarasPlus.springmvc.controller;


import com.tarasPlus.springmvc.model.User;
import com.tarasPlus.springmvc.service.UserService;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private UserService service;
    private Environment environment;

    @Autowired
    public UserController(UserService service, Environment environment) {
        this.service = service;
        this.environment = environment;
    }

    @GetMapping("/user")
    public String showUser(){
        return "userPage";
    }

    @GetMapping("/access_denied")
    public String accessDenied() {
        return "accessDeniedView";
    }

    @PostMapping("/upgrade/user")
    public String upgrade(@ModelAttribute("user") User user, Model model){
        if (service.updateUser(user)) {
            return "redirect:/user";
        } else {
            model.addAttribute("message", environment.getRequiredProperty("invalidData"));
        }
        return "redirect:/userPage";
    }
}
