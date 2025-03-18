package ir.mrmoein.quezapplication.service.Impl;

import ir.mrmoein.quezapplication.exception.NotFoundRequestException;
import ir.mrmoein.quezapplication.model.document.CourseDoc;
import ir.mrmoein.quezapplication.model.dto.CoursesDTO;
import ir.mrmoein.quezapplication.model.dto.RequestCourseDTO;
import ir.mrmoein.quezapplication.model.dto.RequestUpdateCourse;
import ir.mrmoein.quezapplication.model.dto.ResponseCourseDTO;
import ir.mrmoein.quezapplication.model.entity.Course;
import ir.mrmoein.quezapplication.model.entity.Person;
import ir.mrmoein.quezapplication.model.entity.Student;
import ir.mrmoein.quezapplication.model.entity.Teacher;
import ir.mrmoein.quezapplication.repository.elastic.SearchCourseRepo;
import ir.mrmoein.quezapplication.repository.elastic.SearchStudent;
import ir.mrmoein.quezapplication.repository.elastic.SearchTeacher;
import ir.mrmoein.quezapplication.repository.jpa.CourseRepository;
import ir.mrmoein.quezapplication.repository.jpa.StudentRepository;
import ir.mrmoein.quezapplication.repository.jpa.TeacherRepository;
import ir.mrmoein.quezapplication.service.CourseService;
import ir.mrmoein.quezapplication.util.DTOService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final SearchCourseRepo searchCourse;
    private final SearchStudent searchStudent;
    private final SearchTeacher searchTeacher;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, SearchCourseRepo searchCourse, TeacherRepository teacherRepository, StudentRepository studentRepository, SearchStudent searchStudent, SearchTeacher searchTeacher) {
        this.courseRepository = courseRepository;
        this.searchCourse = searchCourse;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.searchStudent = searchStudent;
        this.searchTeacher = searchTeacher;
    }

    @Transactional(rollbackOn = Throwable.class)
    @Override
    public ResponseCourseDTO createCourse(RequestCourseDTO requestCourse) {
        try {
            Course course = Course.builder()
                    .name(requestCourse.getName())
                    .startDate(requestCourse.getStartTime())
                    .endOfTerms(requestCourse.getEndTime())
                    .image(requestCourse.getImage().getBytes()).build();
            Course entity = courseRepository.save(course);

            CourseDoc courseDoc = CourseDoc.builder()
                    .name(entity.getName())
                    .startDate(entity.getStartDate())
                    .endOfTerms(entity.getEndOfTerms())
                    .build();
            searchCourse.save(courseDoc);
            return ResponseCourseDTO.builder()
                    .name(entity.getName())
                    .students(new LinkedList<>())
                    .teacher("")
                    .image(entity.getImage())
                    .startDate(entity.getStartDate())
                    .endOfTerms(entity.getEndOfTerms())
                    .build();
        } catch (IOException e) {
            throw new NotFoundRequestException("this exception for convert image to byte !!!");
        }
    }

    @Override
    public ResponseCourseDTO findCourse(String name) {
        Optional<Course> course = courseRepository.findByName(name);
        if (course.isPresent()) {
            return ResponseCourseDTO.builder()
                    .name(course.get().getName())
                    .startDate(course.get().getStartDate())
                    .endOfTerms(course.get().getEndOfTerms())
                    .teacherCode(course.get().getTeacher().getNationalCode())
                    .teacher(course.get().getTeacher() != null ? course.get().getTeacher().getName() + " " + course.get().getTeacher().getLastName() : "No Teacher")
                    .students(course.get().getStudents() != null ? course.get().getStudents().stream().map(Person::getNationalCode).collect(Collectors.toList()) : new ArrayList<>())
                    .image(course.get().getImage())
                    .build();
        } else {
            throw new NotFoundRequestException("not found course !!!");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseCourseDTO update(RequestUpdateCourse dto) {
        try {
            Course course = courseRepository.findByName(dto.getName()).orElseThrow(() ->
                    new NotFoundRequestException("course Not Found !!!"));

            Teacher teacher = null;

            if (dto.getTeacherCode().equals("null")) {
                 teacher = teacherRepository.findByNationalCode(dto.getTeacher()).orElseThrow(() ->
                        new NotFoundRequestException("Teacher Not Found !!!"));
            } else {
                 teacher = teacherRepository.findByNationalCode(dto.getTeacherCode()).orElseThrow(() ->
                         new NotFoundRequestException("Teacher Not Found !!!"));
            }
            List<String> nationalCodes = dto.getStudents();
            List<String> codes = nationalCodes.stream().map((code) -> code.replace("[", "").replace("]", "").replace("\"", "")).toList();

            List<Student> students = new LinkedList<>();
            for (String nationalCode : codes) {
                students.add(studentRepository.findByNationalCode(nationalCode).orElseThrow());
            }

            course.setTeacher(teacher);
            course.setStartDate(dto.getStartDate());
            course.setEndOfTerms(dto.getEndOfTerms());
            course.setStudents(students);
            courseRepository.save(course);

            teacher.setCourses(course);
            for (Student student : students) {
                student.setCourses(course);
                studentRepository.save(student);
                searchStudent.save(DTOService.convertSearchStudent(student));
            }
            teacherRepository.save(teacher);
            searchTeacher.save(DTOService.convertSearchTeacher(teacher));

            return findCourse(dto.getName());
        } catch (Exception e) {
            throw new NotFoundRequestException("update failed !!!" + e.getMessage());
        }
    }


    @Override
    public List<CoursesDTO> getAll() {
        Iterable<CourseDoc> courses = searchCourse.findAll();
        List<CourseDoc> coursesDOC = StreamSupport.stream(courses.spliterator(), false).toList();
        if (!courses.iterator().hasNext()) {
            throw new NullPointerException("iterator is null");
        }

        return coursesDOC.stream().map((course) ->
                CoursesDTO.builder()
                        .name(course.getName())
                        .startDate(course.getStartDate())
                        .endOfTerms(course.getEndOfTerms())
                        .teacher(course.getTeacher() != null ? course.getTeacher() : "NO TEACHER")
                        .students(course.getStudents() != null ? course.getStudents() : new ArrayList<>())
                        .build()).collect(Collectors.toList());
    }
}
