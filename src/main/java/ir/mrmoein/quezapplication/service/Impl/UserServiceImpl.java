package ir.mrmoein.quezapplication.service.Impl;


import ir.mrmoein.quezapplication.controller.admin.AdminController;
import ir.mrmoein.quezapplication.exception.NotAccessException;
import ir.mrmoein.quezapplication.exception.NotFoundRequestException;
import ir.mrmoein.quezapplication.exception.RegisterFailedException;
import ir.mrmoein.quezapplication.model.document.StudentDoc;
import ir.mrmoein.quezapplication.model.document.TeacherDoc;
import ir.mrmoein.quezapplication.model.dto.*;
import ir.mrmoein.quezapplication.model.entity.*;
import ir.mrmoein.quezapplication.repository.elastic.SearchStudent;
import ir.mrmoein.quezapplication.repository.elastic.SearchTeacher;
import ir.mrmoein.quezapplication.repository.jpa.*;
import ir.mrmoein.quezapplication.service.UserService;
import ir.mrmoein.quezapplication.util.DTOService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final DTOService dtoService;
    private final OutBoxRepository outBoxRepository;
    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder bCrypt, TeacherRepository teaccherRepository,
                           StudentRepository studentRepository, SearchTeacher searchTeacher, SearchStudent searchStudent, RoleRepository roleRepository, DTOService dtoService, OutBoxRepository outBoxRepository) {
        this.userRepository = userRepository;
        this.bCrypt = bCrypt;
        this.teacherRepository = teaccherRepository;
        this.studentRepository = studentRepository;
        this.searchTeacher = searchTeacher;
        this.searchStudent = searchStudent;
        this.roleRepository = roleRepository;
        this.dtoService = dtoService;
        this.outBoxRepository = outBoxRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean registerTeacher(TeacherRegisterRequest requestDTO) {
        try {
            Optional<TeacherDoc> teacher1 = searchTeacher.findByNationalCode(requestDTO.getNationalCode());
            if (teacher1.isEmpty()) {
                Teacher teacher = dtoService.getRequestRegisterTeacher(requestDTO);
                User user = teacher.getUserId();
                user.setPassword(bCrypt.encode(user.getPassword()));
                user.setRoles(getRole(RoleName.ROLE_TEACHER));
                user.setEnable(Boolean.FALSE);
                User save = userRepository.save(user);
                teacher.setUserId(save);
                Teacher entity = teacherRepository.save(teacher);

                OutboxEvent outBox = OutboxEvent.builder()
                        .fullName(entity.getName() + " " + entity.getLastName())
                        .userId(entity.getUserId().getId())
                        .email(entity.getEmail())
                        .role(RoleName.ROLE_TEACHER.name())
                        .nationalCode(entity.getNationalCode())
                        .generalDate(entity.getDob())
                        .build();

                outBoxRepository.save(outBox);

                return true;
            } else {
                throw new DataIntegrityViolationException("this user is already exist !!!");
            }
        } catch (Exception e) {
            throw new RegisterFailedException("please again signup !!! " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean registerStudent(StudentRegisterRequest requestDTO) {
        try {
            Optional<StudentDoc> student1 = searchStudent.findByNationalCode(requestDTO.getNationalCode());
            if (student1.isEmpty()) {
                Student student = dtoService.getRequestRegisterStudent(requestDTO);
                User user = student.getUserId();
                user.setPassword(bCrypt.encode(user.getPassword()));
                user.setRoles(getRole(RoleName.ROLE_STUDENT));
                user.setEnable(Boolean.FALSE);
                User save = userRepository.save(user);
                student.setUserId(save);
                Student entity = studentRepository.save(student);
                OutboxEvent outBox = OutboxEvent.builder()
                        .fullName(entity.getName() + " " + entity.getLastName())
                        .userId(entity.getUserId().getId())
                        .email(entity.getEmail())
                        .role(RoleName.ROLE_STUDENT.name())
                        .nationalCode(entity.getNationalCode())
                        .generalDate(entity.getCreateDate())
                        .build();

                outBoxRepository.save(outBox);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
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

        return dtoService.concatList(students, teachers);
    }

    @Override
    public List<StatusDTO> liveSearchWithFullName(String value) {
        List<StudentDoc> students = searchStudent.findByFullNameContains(value);
        List<TeacherDoc> teachers = searchTeacher.findByFullNameContains(value);

        return dtoService.concatList(students, teachers);
    }

    @Override
    public ProfileDTO getProfileUser(String nationalCode) {
        try {
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
                throw new NotFoundRequestException("this request failed !!!");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
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
