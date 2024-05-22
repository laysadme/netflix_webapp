package com.netflix.webapp.controllers;

import com.netflix.webapp.dtos.UserDTO;
import com.netflix.webapp.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;

import java.util.List;

@Controller
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registrationPage(Model model) {
        UserDTO user = new UserDTO();
        model.addAttribute("newUser", user);

        return "register";
    }

    @PostMapping("/register/save")
    public String registerUser(@Valid @ModelAttribute("newUser") UserDTO newUser, BindingResult result,
            Model model) {
        UserDTO user = userService.findOneByUsername(newUser.getLastName());

        if (user != null) {
            result.rejectValue("email", null, "There is already an account registered with the username");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", newUser);
            return "register";
        }

        // Set password and roles, then store the user in the db
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRoles(List.of(new String[]{"USER"}));

        userService.save(newUser);

        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String usersPage(Model model) {
        List<UserDTO> users = userService.findAll();
        model.addAttribute("users", users);

        return "users";
    }
}