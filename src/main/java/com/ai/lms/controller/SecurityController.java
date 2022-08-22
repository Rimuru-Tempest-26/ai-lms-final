package com.ai.lms.controller;

import com.ai.lms.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {


    @GetMapping({"/", "login"})
    public String login(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null && auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(User.Role.Admin.name()))){
            return "redirect:/admin/home";
        }
        if(auth!=null && auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(User.Role.Student.name()))){
            return "redirect:/student/home";
        }
        if(auth!=null && auth.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(User.Role.Teacher.name()))){
            return "redirect:/teacher/home";
        }
        return "LGN01";
    }

}
