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
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class AdminController {

    private UserService service;
    private Environment environment;

    @Autowired
    public AdminController(UserService service, Environment environment) {
        this.service = service;
        this.environment = environment;
    }

    @GetMapping("/admin")
    public String userList(@ModelAttribute("user") User user,
                           @ModelAttribute("message") String message, Model model, Principal principal) {

      //  User admin = service.getUserByLogin(principal.getName());
      //  User editUser = service.editUserView(edit, user);
        List<User> list = service.getAllUsers();

        model.addAttribute("list", list);
     //  model.addAttribute("userEdit", editUser);
     //  model.addAttribute("admin", admin);
        return "admin";
    }

    @PostMapping(value = "/admin/add")
    public String userAdd(@ModelAttribute("user") User user, Model model, @ModelAttribute("role") String role) {

        if (!service.addUserAdmin(user, role)) {
            model.addAttribute("message", environment.getRequiredProperty("invalidData"));
        }
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/update")
    public String userUpdate(@ModelAttribute("user") User user, Model model, @ModelAttribute("role") String role) {

        if (!service.updateUser(user)) {
            model.addAttribute("message", environment.getRequiredProperty("invalidData"));
        }
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/delete")
    public String userDelete(@ModelAttribute("user") User user) {

        service.deleteUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/edit_user")
    public String editUserForm(@RequestParam("id") Integer id, Model model){
        model.addAttribute(service.getUserById(id));
        return "/user_edit";
    }

    @PostMapping(value = "/admin/edit_user/user")
    public String editUser(@ModelAttribute("user") User user, Model model){
        if (service.updateUser(user)){
            return "redirect:/admin";
        } else {
            model.addAttribute("message", environment.getRequiredProperty("invalidData"));
        }
        return "redirect:/admin";
    }
}