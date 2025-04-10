package ir.mrmoein.quezapplication.controller.student;

import ir.mrmoein.quezapplication.model.dto.*;
import ir.mrmoein.quezapplication.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final Logger logger = LoggerFactory.getLogger(StudentController.class);

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping
    public ModelAndView start() {
        return new ModelAndView("student");
    }

    @GetMapping("/details")
    public ResponseEntity<List<CoursesStudentDTO>> details(@AuthenticationPrincipal UserDetails userDetails) {
        List<CoursesStudentDTO> courses = studentService.getCourses(userDetails);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/page")
    public ModelAndView course() {
        return new ModelAndView("student_course");
    }

    @GetMapping("/course")
    public ResponseEntity<CoursePersonDTO> courseDetails(@RequestParam("id") String course,@AuthenticationPrincipal UserDetails userDetails) {
        CoursePersonDTO myCourseDetails = studentService.getMyCourseDetails(course, userDetails);
        return ResponseEntity.ok(myCourseDetails);
    }

    @GetMapping("/exam_page")
    public ModelAndView exam(){
        return new ModelAndView("start_exam");
    }

    @PostMapping("/exam")
    public ResponseEntity<?> examDetails(@RequestParam String exam ,@AuthenticationPrincipal UserDetails userDetails) {
        ExamStartDTO myExamDetails = studentService.getMyExamDetails(exam, userDetails);
        return ResponseEntity.ok(myExamDetails);
    }

    @PostMapping("/answer")
    public ResponseEntity<?> answerExam(@RequestBody ExamSubmitDTO answer) {
        studentService.submitExam(answer);
        return ResponseEntity.ok().build();
    }

}
