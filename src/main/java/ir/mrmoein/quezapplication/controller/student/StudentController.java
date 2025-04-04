package ir.mrmoein.quezapplication.controller.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/student")
public class StudentController {

    @GetMapping
    public ModelAndView start() {
        return new ModelAndView("student");
    }

}
