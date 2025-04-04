package ir.mrmoein.quezapplication.controller.teacher;

import ir.mrmoein.quezapplication.model.dto.CoursesTeacherDTO;
import ir.mrmoein.quezapplication.model.dto.EditionProfileDTO;
import ir.mrmoein.quezapplication.model.dto.StudentCardDTO;
import ir.mrmoein.quezapplication.model.dto.TeacherProfileDTO;
import ir.mrmoein.quezapplication.service.CourseService;
import ir.mrmoein.quezapplication.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private CourseService courseService;
    private TeacherService teacherService;

    @Autowired
    public TeacherController(CourseService courseService, TeacherService teacherService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public ModelAndView getCourses(@AuthenticationPrincipal UserDetails user) {
        List<CoursesTeacherDTO> courses = teacherService.getCourses(user.getUsername());
        return new ModelAndView("teacher").addObject("courses",courses);
    }

    @GetMapping("/show")
    public ResponseEntity<TeacherProfileDTO> show(@AuthenticationPrincipal UserDetails user) {
        TeacherProfileDTO profile = teacherService.getProfile(user.getUsername());
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/home")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ModelAndView home() {
        return new ModelAndView("teacher");
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ModelAndView profile(){
        return new ModelAndView("profile");
    }

    @PutMapping("/update")
    public ResponseEntity<TeacherProfileDTO> update(@ModelAttribute EditionProfileDTO editionProfileDTO) {
        TeacherProfileDTO teacher = teacherService.updateProfile(editionProfileDTO);
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentCardDTO>> students(@RequestParam("course") String courseName) {
        List<StudentCardDTO> students = teacherService.getStudents(courseName);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/students/page")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ModelAndView page() {
        return new ModelAndView("students_page");
    }

}
