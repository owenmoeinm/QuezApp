package ir.mrmoein.quezapplication.util;


import ir.mrmoein.quezapplication.model.document.StudentDoc;
import ir.mrmoein.quezapplication.model.document.TeacherDoc;
import ir.mrmoein.quezapplication.model.dto.*;
import ir.mrmoein.quezapplication.model.entity.*;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DTOService {

    public static ResponseCourseDTO getRequestCourseDTO(Course course) {
        return ResponseCourseDTO.builder()
                .name(course.getName())
                .image(course.getImage())
                .teacher(course.getTeacher().getName().concat(course.getTeacher().getLastName()))
                .startDate(course.getStartDate())
                .endOfTerms(course.getEndOfTerms())
                .students(new LinkedList<>())
                .build();
    }

    public static Teacher getRequestRegisterTeacher(TeacherRegisterRequest registerRequest) throws IOException {
        return Teacher.builder()
                .userId(User.builder().username(registerRequest.getUsername()).password(registerRequest.getPassword()).build())
                .name(registerRequest.getName())
                .image(registerRequest.getImage().getBytes())
                .lastName(registerRequest.getLastname())
                .dob(registerRequest.getDob())
                .email(registerRequest.getEmail())
                .nationalCode(registerRequest.getNationalCode())
                .build();
    }

    public static RequestTeacherDTO getRequestCourseDTO(Teacher teacher) {
        return RequestTeacherDTO.builder()
                .name(teacher.getName())
                .lastName(teacher.getLastName())
                .email(teacher.getEmail())
                .nationalCode(teacher.getNationalCode())
                .courses(teacher.getCourses())
//                .dop(teacher.getDop())
                .build();
    }

    public static List<StatusDTO> concatList(List<StudentDoc> students , List<TeacherDoc> teachers){
        return Stream.concat(students.stream().map(e ->
                StatusDTO.builder()
                        .fullName(e.getFullName())
                        .nationalCode(e.getNationalCode())
                        .status(State.valueOf(e.getStatus()))
                        .roleName(RoleName.ROLE_STUDENT)
                        .build()
        ), teachers.stream().map(e ->
                StatusDTO.builder()
                        .fullName(e.getFullName())
                        .nationalCode(e.getNationalCode())
                        .status(State.valueOf(e.getStatus()))
                        .roleName(RoleName.ROLE_TEACHER)
                        .build()
        )).collect(Collectors.toList());
    }

    public static ResponseTeacherDTO getResponseTeacherDTO(Teacher teacher) {
        return ResponseTeacherDTO.builder()
                .name(teacher.getName())
                .lastName(teacher.getLastName())
                .email(teacher.getEmail())
                .nationalCode(teacher.getNationalCode())
                .courses(teacher.getCourses().size())
//                .dop(teacher.getDop())
                .build();
    }

    public static Student getRequestRegisterStudent(StudentRegisterRequest registerRequest) throws IOException {
        return Student.builder()
                .userId(User.builder().username(registerRequest.getUsername()).password(registerRequest.getPassword()).build())
                .name(registerRequest.getName())
                .image(registerRequest.getImage().getBytes())
                .lastName(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .nationalCode(registerRequest.getNationalCode())
                .build();
    }

    public static StudentDoc convertSearchStudent(Student student) {
        List<String> courses = student.getCourses().stream().map(Course::getName).collect(Collectors.toList());
        return StudentDoc.builder()
                .fullName(student.getName() +" "+ student.getLastName())
                .status(student.getStatus().name())
                .role(RoleName.ROLE_STUDENT.name())
                .nationalCode(student.getNationalCode())
                .createDate(student.getCreateDate())
                .email(student.getEmail())
                .userId(student.getUserId().getId().toString())
                .courses(courses)
                .build();
    }

    public static TeacherDoc convertSearchTeacher(Teacher teacher) {
        List<String> courses = teacher.getCourses().stream().map(Course::getName).collect(Collectors.toList());
        return TeacherDoc.builder()
                .fullName(teacher.getName() +" "+ teacher.getLastName())
                .status(teacher.getStatus().name())
                .role(RoleName.ROLE_TEACHER.name())
                .nationalCode(teacher.getNationalCode())
                .dob(teacher.getDob())
                .email(teacher.getEmail())
                .courses(courses)
                .build();
    }
}
