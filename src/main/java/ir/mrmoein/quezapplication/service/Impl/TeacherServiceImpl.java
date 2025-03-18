package ir.mrmoein.quezapplication.service.Impl;


import ir.mrmoein.quezapplication.model.dto.RequestTeacherDTO;
import ir.mrmoein.quezapplication.model.dto.ResponseTeacherDTO;
import ir.mrmoein.quezapplication.model.entity.Teacher;
import ir.mrmoein.quezapplication.repository.elastic.SearchTeacher;
import ir.mrmoein.quezapplication.repository.jpa.TeacherRepository;
import ir.mrmoein.quezapplication.service.TeacherService;
import ir.mrmoein.quezapplication.util.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository repository;
    private final SearchTeacher searchTeacher;

    @Autowired
    public TeacherServiceImpl(TeacherRepository repository , SearchTeacher searchTeacher) {
        this.repository = repository;
        this.searchTeacher = searchTeacher;
    }

    @Override
    public void changeStateTeacher(RequestTeacherDTO teacherDTO) {
    }

    @Override
    public List<ResponseTeacherDTO> findAllTeacher() {
        List<Teacher> teachers = repository.findAll();
        LinkedList<ResponseTeacherDTO> requestCourses = new LinkedList<>();
        for (Teacher teacher : teachers) {
            requestCourses.add(DTOService.getResponseTeacherDTO(teacher));
        }
        return requestCourses;
    }

}
