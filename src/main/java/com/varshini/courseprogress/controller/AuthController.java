package com.varshini.courseprogress.controller;

import com.varshini.courseprogress.model.AppUser;
import com.varshini.courseprogress.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        fullName = fullName.trim();
        email = email.trim().toLowerCase();

        if (fullName.isEmpty() || email.isEmpty() || password.isBlank()) {
            model.addAttribute("error", "All fields are required.");
            return "signup";
        }

        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "This email is already registered.");
            return "signup";
        }

        if (password.length() < 8) {
            model.addAttribute("error",
                    "Password must contain at least 8 characters.");
            return "signup";
        }

        AppUser user = new AppUser();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        redirectAttributes.addFlashAttribute(
                "success",
                "Account created successfully. Please log in."
        );

        return "redirect:/login";
    }
}