package ir.mrmoein.quezapplication.service.Impl;


import ir.mrmoein.quezapplication.controller.admin.AdminController;
import ir.mrmoein.quezapplication.exception.NotFoundRequestException;
import ir.mrmoein.quezapplication.model.document.TeacherDoc;
import ir.mrmoein.quezapplication.model.dto.CoursesTeacherDTO;
import ir.mrmoein.quezapplication.model.dto.EditionProfileDTO;
import ir.mrmoein.quezapplication.model.dto.StudentCardDTO;
import ir.mrmoein.quezapplication.model.dto.TeacherProfileDTO;
import ir.mrmoein.quezapplication.model.entity.Course;
import ir.mrmoein.quezapplication.model.entity.Student;
import ir.mrmoein.quezapplication.model.entity.Teacher;
import ir.mrmoein.quezapplication.model.entity.User;
import ir.mrmoein.quezapplication.repository.elastic.SearchTeacher;
import ir.mrmoein.quezapplication.repository.jpa.CourseRepository;
import ir.mrmoein.quezapplication.repository.jpa.TeacherRepository;
import ir.mrmoein.quezapplication.repository.jpa.UserRepository;
import ir.mrmoein.quezapplication.service.TeacherService;
import ir.mrmoein.quezapplication.util.DTOService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.LinkedList;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository repository;
    private final SearchTeacher searchTeacher;
    private final CourseRepository courseRepository;
    private final DTOService dtoService;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(AdminController.class);


    @Autowired
    public TeacherServiceImpl(TeacherRepository repository, SearchTeacher searchTeacher, CourseRepository courseRepository, DTOService dtoService, UserRepository userRepository) {
        this.repository = repository;
        this.searchTeacher = searchTeacher;
        this.courseRepository = courseRepository;
        this.dtoService = dtoService;
        this.userRepository = userRepository;
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public boolean remove(String nationalCode) {
        try {
            Teacher teacher = repository.findByNationalCode(nationalCode).orElseThrow(() -> new NotFoundRequestException("not found teacher !!! "));
            List<Course> courses = teacher.getCourses();
            for (Course course : courses) {
                course.setTeacher(null);
                courseRepository.save(course);
            }
            repository.deleteByNationalCode(nationalCode);
            searchTeacher.deleteByNationalCode(nationalCode);
            return true;
        } catch (Exception e) {
            throw new NotFoundRequestException(" delete failed!!! " + e.getMessage());
        }
    }


    @Override
    public List<CoursesTeacherDTO> getCourses(String username) {
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundRequestException("this user name not found in dataBase !!!"));
            Teacher teacher = repository.findByUserId(user.getId()).orElseThrow(() -> new NotFoundRequestException("not found teacher in postgres !!! "));
            List<Course> courses = teacher.getCourses();
            if (courses.isEmpty()) {
                return new LinkedList<>();
            } else {
                return courses.stream().map(dtoService::getCoursesTeacherResponse).toList();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NotFoundRequestException("Courses failed!!! " + e.getMessage());
        }
    }

    @Override
    public TeacherProfileDTO getProfile(String username) {
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundRequestException("not Found User !!! "));
            Teacher teacher = repository.findByUserId(user.getId()).orElseThrow(() -> new NotFoundRequestException("teacher not found Exception !!! "));
            return dtoService.getProfileTeacher(teacher);
        } catch (Exception e) {
            throw new NotFoundRequestException("teacher failed!!! " + e.getMessage());
        }
    }

    @Override
    public TeacherProfileDTO updateProfile(EditionProfileDTO profile) {
        try {
            Teacher teacher = repository.findByNationalCode(profile.getNationalCode()).orElseThrow(() -> new NotFoundRequestException("teacher Not Found Exception !!!"));
            teacher.setName(profile.getName());
            teacher.setEmail(profile.getEmail());
            teacher.setLastName(profile.getLastName());
            teacher.setDob(profile.getDob());
            teacher.setImage(profile.getImage() != null ? profile.getImage().getBytes() : teacher.getImage());
            repository.save(teacher);
            TeacherDoc teacherDoc = searchTeacher.findByNationalCode(teacher.getNationalCode()).orElseThrow(() -> new NotFoundRequestException("teacher Not Found Exception !!!"));
            teacherDoc.setFullName(teacher.getName() + " " + teacher.getLastName());
            teacherDoc.setEmail(teacher.getEmail());
            teacherDoc.setDob(teacher.getDob());
            searchTeacher.save(teacherDoc);
            logger.info("teacher {} updated" , teacherDoc.getFullName() + "/" + teacher.getNationalCode());
            return dtoService.getProfileTeacher(teacher);
        } catch (Exception e) {
            throw new NotFoundRequestException("teacher update Failed!!! " + e.getMessage());
        }
    }

    @Override
    public List<StudentCardDTO> getStudents(String name) {
        Course course = courseRepository.findByName(name).orElseThrow(() -> new NotFoundRequestException("course Not Found Exception !!!"));
        List<Student> students = course.getStudents();
        List<StudentCardDTO> studentCardDTOS = new LinkedList<>();
        for (Student student : students) {
            studentCardDTOS.add(dtoService.getStudentCard(student));
        }
        return studentCardDTOS;
    }


}
