package ir.mrmoein.quezapplication.controller;

import ir.mrmoein.quezapplication.model.dto.*;
import ir.mrmoein.quezapplication.service.CourseService;
import ir.mrmoein.quezapplication.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/course")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView course() {
        ArrayList<Object> course = new ArrayList<>();
        try {
            List<CoursesDTO> courses = courseService.getAll();
            return new ModelAndView("course").addObject("courses", courses);
        } catch (NullPointerException e) {
            return new ModelAndView("course").addObject("courses", course);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseCourseDTO> create(
            @ModelAttribute RequestCourseDTO courseDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        logger.info("user {} create Course", userDetails.getUsername());
        return ResponseEntity.ok(courseService.createCourse(courseDTO));
    }

    @GetMapping("/edit")
    public ResponseEntity<ResponseCourseDTO> edit(@RequestParam String courseName) {
        ResponseCourseDTO course = courseService.findCourse(courseName);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/view")
    public ModelAndView view_course() {
        return new ModelAndView("course_edit");
    }

    @PostMapping("/selected")
    public ResponseEntity<List<RequestSelectedUserDTO>> selected(@RequestParam("query") String query) {
        List<RequestSelectedUserDTO> users = userService.searchSelected(query);
        return ResponseEntity.ok(users);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseCourseDTO> update(@ModelAttribute RequestUpdateCourse dto) {
        try {
            ResponseCourseDTO course = courseService.update(dto);
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }

}
