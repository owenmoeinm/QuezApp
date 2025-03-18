package ir.mrmoein.quezapplication.service;




import ir.mrmoein.quezapplication.model.dto.CoursesDTO;
import ir.mrmoein.quezapplication.model.dto.RequestCourseDTO;
import ir.mrmoein.quezapplication.model.dto.RequestUpdateCourse;
import ir.mrmoein.quezapplication.model.dto.ResponseCourseDTO;

import java.util.List;

public interface CourseService{

    ResponseCourseDTO createCourse(RequestCourseDTO requestCourse);

    ResponseCourseDTO findCourse (String name);

    ResponseCourseDTO update (RequestUpdateCourse dto);

    List<CoursesDTO> getAll();

}
