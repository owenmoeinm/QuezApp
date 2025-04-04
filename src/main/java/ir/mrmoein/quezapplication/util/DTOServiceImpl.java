package ir.mrmoein.quezapplication.util;


import ir.mrmoein.quezapplication.model.document.QuestionDoc;
import ir.mrmoein.quezapplication.model.document.StudentDoc;
import ir.mrmoein.quezapplication.model.document.TeacherDoc;
import ir.mrmoein.quezapplication.model.dto.*;
import ir.mrmoein.quezapplication.model.entity.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DTOServiceImpl implements DTOService {

    @Override
    public Teacher getRequestRegisterTeacher(TeacherRegisterRequest registerRequest) throws IOException {
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

    @Override
    public List<StatusDTO> concatList(List<StudentDoc> students, List<TeacherDoc> teachers) {
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

    @Override
    public CoursesTeacherDTO getCoursesTeacherResponse(Course course) {
        return CoursesTeacherDTO.builder()
                .image(Base64.getEncoder().encodeToString(course.getImage()))
                .name(course.getName())
                .startDate(course.getStartDate())
                .endOfTerms(course.getEndOfTerms())
                .students(course.getStudents().size())
                .build();
    }

    @Override
    public TeacherDoc getTeacherDoc(OutboxEvent outBox) {
        return TeacherDoc.builder()
                .fullName(outBox.getFullName())
                .dob(outBox.getGeneralDate())
                .userId(outBox.getUserId().toString())
                .email(outBox.getEmail())
                .role(RoleName.ROLE_TEACHER.name())
                .status(State.VALIDATING.name())
                .nationalCode(outBox.getNationalCode())
                .build();
    }

    @Override
    public StudentDoc getStudentDoc(OutboxEvent outBox) {
        return StudentDoc.builder()
                .fullName(outBox.getFullName())
                .createDate(outBox.getGeneralDate())
                .userId(outBox.getUserId().toString())
                .email(outBox.getEmail())
                .role(RoleName.ROLE_STUDENT.name())
                .status(State.VALIDATING.name())
                .nationalCode(outBox.getNationalCode())
                .build();
    }

    @Override
    public TeacherProfileDTO getProfileTeacher(Teacher teacher) {
        return TeacherProfileDTO.builder()
                .name(teacher.getName())
                .image(teacher.getImage())
                .lastName(teacher.getLastName())
                .dob(teacher.getDob())
                .email(teacher.getEmail())
                .nationalCode(teacher.getNationalCode())
                .build();
    }

    @Override
    public StudentCardDTO getStudentCard(Student student) {
        return StudentCardDTO.builder()
                .fullName(student.getName() + " " + student.getLastName())
                .email(student.getEmail())
                .status(student.getStatus().name())
                .image(student.getImage())
                .nationalCode(student.getNationalCode())
                .exams(student.getExams())
                .build();
    }

    @Override
    public ExamDTO getExamDTO(Exam exam) {
        return ExamDTO.builder()
                .id(exam.getId())
                .name(exam.getName())
                .description(exam.getDescription())
                .startDate(exam.getStartDate())
                .endDate(exam.getEndDate())
                .state(exam.getState())
                .duration(exam.getDuration())
                .build();
    }

    @Override
    public Exam getExam(ExamDTO exam) {
        return Exam.builder()
                .name(exam.getName())
                .startDate(exam.getStartDate())
                .endDate(exam.getEndDate())
                .state(exam.getState())
                .duration(exam.getDuration())
                .build();
    }

    @Override
    public QuestionDoc getSearchDoc(ExamQuestion question) {
        return QuestionDoc.builder()
                .title(question.getTitle())
                .question(question.getQuestion())
                .examQuestionId(question.getId())
                .score(question.getScore())
                .questionType(question.getQuestionType().name())
                .build();
    }

    @Override
    public ExamQuestionDTO getExamQuestionsDTO(ExamQuestion examQuestion) {
        return ExamQuestionDTO.builder()
                .title(examQuestion.getTitle())
                .id(examQuestion.getId())
                .score(examQuestion.getScore())
                .options(examQuestion.getOptions().stream().map((Option::getValue)).toList())
                .questionType(examQuestion.getQuestionType().name())
                .question(examQuestion.getQuestion())
                .build();
    }

    @Override
    public ExamQuestion getExamQuestion(RequestExamQuestionDTO dto , Course course , Exam exam) {
        ExamQuestion examQuestion = ExamQuestion.builder()
                .question(dto.getQuestion())
                .category(course)
                .questionType(dto.getType().equals("mcq") ? QuestionType.MULTIPLE_CHOICE : QuestionType.SHORT_ANSWER)
                .score(Double.parseDouble(dto.getScore()))
                .title(dto.getTitle())
                .build();
        examQuestion.addExam(exam);
        return examQuestion;
    }

    @Override
    public QuestionSearchDTO getQuestionSearchDTO(QuestionDoc questionDoc) {
        return QuestionSearchDTO.builder()
                .value(questionDoc.getTitle())
                .id(String.valueOf(questionDoc.getExamQuestionId()))
                .questionType(questionDoc.getQuestionType())
                .question(questionDoc.getQuestion())
                .score(String.valueOf(questionDoc.getScore()))
                .build();
    }

    @Override
    public Student getRequestRegisterStudent(StudentRegisterRequest registerRequest) throws IOException {
        return Student.builder()
                .userId(User.builder().username(registerRequest.getUsername()).password(registerRequest.getPassword()).build())
                .name(registerRequest.getName())
                .image(registerRequest.getImage().getBytes())
                .lastName(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .nationalCode(registerRequest.getNationalCode())
                .build();
    }


}
