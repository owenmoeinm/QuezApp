package ir.mrmoein.quezapplication.service.Impl;


import ir.mrmoein.quezapplication.exception.NotAccessException;
import ir.mrmoein.quezapplication.exception.NotFoundRequestException;
import ir.mrmoein.quezapplication.exception.RegisterFailedException;
import ir.mrmoein.quezapplication.model.document.StudentDoc;
import ir.mrmoein.quezapplication.model.document.TeacherDoc;
import ir.mrmoein.quezapplication.model.dto.*;
import ir.mrmoein.quezapplication.model.entity.*;
import ir.mrmoein.quezapplication.repository.elastic.SearchStudent;
import ir.mrmoein.quezapplication.repository.elastic.SearchTeacher;
import ir.mrmoein.quezapplication.repository.jpa.RoleRepository;
import ir.mrmoein.quezapplication.repository.jpa.StudentRepository;
import ir.mrmoein.quezapplication.repository.jpa.TeacherRepository;
import ir.mrmoein.quezapplication.repository.jpa.UserRepository;
import ir.mrmoein.quezapplication.service.UserService;
import ir.mrmoein.quezapplication.util.DTOService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCrypt;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final SearchTeacher searchTeacher;
    private final SearchStudent searchStudent;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder bCrypt, TeacherRepository teaccherRepository,
                           StudentRepository studentRepository, SearchTeacher searchTeacher, SearchStudent searchStudent, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCrypt = bCrypt;
        this.teacherRepository = teaccherRepository;
        this.studentRepository = studentRepository;
        this.searchTeacher = searchTeacher;
        this.searchStudent = searchStudent;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean registerTeacher(TeacherRegisterRequest requestDTO) {
        try {
            Teacher teacher = DTOService.getRequestRegisterTeacher(requestDTO);
            User user = teacher.getUserId();
            user.setPassword(bCrypt.encode(user.getPassword()));
            user.setRoles(getRole(RoleName.ROLE_TEACHER));
            user.setEnable(Boolean.FALSE);
            User save = userRepository.save(user);
            teacher.setUserId(save);
            Teacher entity = teacherRepository.save(teacher);
            TeacherDoc teacherDoc = TeacherDoc.builder()
                    .fullName(entity.getName() + " " + entity.getLastName())
                    .dob(entity.getDob())
                    .userId(entity.getUserId().getId().toString())
                    .email(entity.getEmail())
                    .role(RoleName.ROLE_TEACHER.name())
                    .status(State.VALIDATING.name())
                    .nationalCode(entity.getNationalCode())
                    .build();
            searchTeacher.save(teacherDoc);
            return true;
        } catch (Exception e) {
            throw new RegisterFailedException("please again signup !!! " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean registerStudent(StudentRegisterRequest requestDTO) {
        try {
            Student student = DTOService.getRequestRegisterStudent(requestDTO);
            User user = student.getUserId();
            user.setPassword(bCrypt.encode(user.getPassword()));
            user.setRoles(getRole(RoleName.ROLE_STUDENT));
            user.setEnable(Boolean.FALSE);
            User save = userRepository.save(user);
            student.setUserId(save);
            Student entity = studentRepository.save(student);
            StudentDoc studentDoc = StudentDoc.builder()
                    .fullName(entity.getName() + " " + entity.getLastName())
                    .userId(entity.getUserId().getId().toString())
                    .email(entity.getEmail())
                    .status(entity.getStatus().name())
                    .role(RoleName.ROLE_STUDENT.name())
                    .status(State.VALIDATING.name())
                    .nationalCode(entity.getNationalCode())
                    .build();
            searchStudent.save(studentDoc);
            return true;
        } catch (IOException e) {
            throw new NotAccessException("please again signup !!!");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void changeState(StatusDTO dto) {
        if (dto.getRoleName().equals(RoleName.ROLE_TEACHER)) {
            Optional<TeacherDoc> teacherDoc = searchTeacher.findByNationalCode(dto.getNationalCode());
            if (teacherDoc.isPresent()) {
                Optional<Teacher> teacher = teacherRepository.findByNationalCode(teacherDoc.get().getNationalCode());
                if (teacher.isPresent()) {
                    teacher.ifPresent(e -> e.setStatus(State.CONFIRM));
                    teacherDoc.get().setStatus(State.CONFIRM.name());
                    teacher.ifPresent(e -> e.getUserId().setEnable(Boolean.TRUE));
                    teacherRepository.save(teacher.get());
                    searchTeacher.save(teacherDoc.get());
                }
            } else {
                throw new NotFoundRequestException("this teacher not found !!!");
            }
        } else if (dto.getRoleName().equals(RoleName.ROLE_STUDENT)) {
            Optional<StudentDoc> studentDoc = searchStudent.findByNationalCode(dto.getNationalCode());
            if (studentDoc.isPresent()) {
                Optional<Student> student = studentRepository.findByNationalCode(studentDoc.get().getNationalCode());
                if (student.isPresent()) {
                    student.ifPresent(e -> e.setStatus(State.CONFIRM));
                    studentDoc.get().setStatus(State.CONFIRM.name());
                    student.get().getUserId().setEnable(Boolean.TRUE);
                    studentRepository.save(student.get());
                    searchStudent.save(studentDoc.get());
                }
            } else {
                throw new NotFoundRequestException("this student not found !!!");
            }
        } else {
            throw new NotAccessException("this is not valid");
        }
    }

    @Override
    public List<StatusDTO> getAllStatus() {
        List<StudentDoc> students = searchStudent.findByStatus(State.VALIDATING.name());
        List<TeacherDoc> teachers = searchTeacher.findByStatus(State.VALIDATING.name());

        return DTOService.concatList(students, teachers);
    }

    @Override
    public List<StatusDTO> liveSearchWithFullName(String value) {
        List<StudentDoc> students = searchStudent.findByFullNameContains(value);
        List<TeacherDoc> teachers = searchTeacher.findByFullNameContains(value);

        return DTOService.concatList(students, teachers);
    }

    @Override
    public ProfileDTO getProfileUser(String nationalCode) {
        Optional<Student> student = studentRepository.findByNationalCode(nationalCode);
        Optional<Teacher> teacher = teacherRepository.findByNationalCode(nationalCode);

        if (student.isPresent()) {
            return ProfileDTO.builder()
                    .fullName(student.get().getName() + " " + student.get().getLastName())
                    .image(student.get().getImage())
                    .State(student.get().getStatus().name())
                    .email(student.get().getEmail())
                    .nationalCode(student.get().getNationalCode())
                    .build();
        } else if (teacher.isPresent()) {
            return ProfileDTO.builder()
                    .fullName(teacher.get().getName() + " " + teacher.get().getLastName())
                    .image(teacher.get().getImage())
                    .State(teacher.get().getStatus().name())
                    .email(teacher.get().getEmail())
                    .nationalCode(teacher.get().getNationalCode())
                    .build();
        } else {
            throw new NotFoundRequestException("this Request Not valid !!!");
        }

    }

    @Override
    public List<RequestSelectedUserDTO> searchSelected(String value) {
        List<StudentDoc> student = searchStudent.findByNationalCodeContaining(value);
        List<TeacherDoc> teacher = searchTeacher.findByNationalCodeContaining(value);

        return Stream.concat(student.stream().map(stu ->
                RequestSelectedUserDTO.builder()
                        .fullName(stu.getFullName())
                        .role(stu.getRole())
                        .nationalCode(stu.getNationalCode())
                        .build()
        ), teacher.stream().map(teach ->
                RequestSelectedUserDTO.builder()
                        .fullName(teach.getFullName())
                        .role(teach.getRole())
                        .nationalCode(teach.getNationalCode())
                        .build()
        )).collect(Collectors.toList());
    }

    private Set<Role> getRole(RoleName role) {
        Set<Role> roles = new HashSet<>();
        Role role1 = roleRepository.findByName(role.name());
        roles.add(role1);
        return roles;
    }
}
