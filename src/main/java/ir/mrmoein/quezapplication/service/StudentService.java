package ir.mrmoein.quezapplication.service;

import ir.mrmoein.quezapplication.model.dto.CoursePersonDTO;
import ir.mrmoein.quezapplication.model.dto.CoursesStudentDTO;
import ir.mrmoein.quezapplication.model.dto.ExamStartDTO;
import ir.mrmoein.quezapplication.model.dto.ExamSubmitDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface StudentService {

    boolean remove(String nationalCode);

    List<CoursesStudentDTO> getCourses(UserDetails userDetails);

    CoursePersonDTO getMyCourseDetails(String course , UserDetails userDetails);

    ExamStartDTO getMyExamDetails(String exam , UserDetails userDetails);

    void submitExam(ExamSubmitDTO examSubmitDTO);



}
