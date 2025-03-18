package ir.mrmoein.quezapplication.service;


import ir.mrmoein.quezapplication.model.dto.RequestTeacherDTO;
import ir.mrmoein.quezapplication.model.dto.ResponseTeacherDTO;

import java.util.List;

public interface TeacherService {

    void changeStateTeacher(RequestTeacherDTO teacherDTO);

    List<ResponseTeacherDTO> findAllTeacher();


}
