package com.takku.project.controller;

import com.takku.project.domain.UserDTO;
import com.takku.project.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

	@Autowired
    UserService userService;

    @GetMapping("/user")
    public String getUser(@RequestParam("id") int id, Model model) {
        UserDTO user = userService.selectByUserId(id);
        model.addAttribute("user", user);
        return "user/userDetail";
    }
}
