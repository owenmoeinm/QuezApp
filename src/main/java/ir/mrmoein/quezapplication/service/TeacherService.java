package ir.mrmoein.quezapplication.service;


import ir.mrmoein.quezapplication.model.dto.*;

import java.util.List;

public interface TeacherService {

    boolean remove(String nationalCode);

    List<CoursesTeacherDTO> getCourses(String username);

    TeacherProfileDTO getProfile (String username);

    TeacherProfileDTO updateProfile (EditionProfileDTO profile);

    List<StudentCardDTO> getStudents(String name);

}
