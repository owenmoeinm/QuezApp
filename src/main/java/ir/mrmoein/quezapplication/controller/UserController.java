package ir.mrmoein.quezapplication.controller;


import ir.mrmoein.quezapplication.controller.admin.AdminController;
import ir.mrmoein.quezapplication.model.dto.StudentRegisterRequest;
import ir.mrmoein.quezapplication.model.dto.TeacherRegisterRequest;
import ir.mrmoein.quezapplication.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/start")
public class UserController {

    private final UserService service;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "role_selection";
    }

    @GetMapping("/teacher-signup")
    public String teacher() {
        return "signup_teacher";
    }

    @GetMapping("/student-signup")
    public String student() {
        return "signup_student";
    }

    @PostMapping(value = "/student", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String student(@ModelAttribute StudentRegisterRequest registerRequest , Model model) {
        if (service.registerStudent(registerRequest)) {
            logger.info("student {} Register and Pending confirmation", registerRequest.getUsername());
            model.addAttribute("info" , true);
            return "redirect:/login";
        } else {
            model.addAttribute("error" , true);
            return "signup_student";
        }
    }

    @PostMapping(value = "/teacher", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String teacher(@ModelAttribute TeacherRegisterRequest registerRequest , RedirectAttributes redirectAttributes) {
        if (service.registerTeacher(registerRequest)) {
            logger.info("teacher {} Register and Pending confirmation", registerRequest.getUsername());
            redirectAttributes.addAttribute("info" , true);
            return "redirect:/login";
        } else {
            redirectAttributes.addAttribute("error" , true);
            return "redirect:/signup";
        }
    }

}
