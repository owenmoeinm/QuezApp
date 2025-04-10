package ir.mrmoein.quezapplication.util;

import ir.mrmoein.quezapplication.model.document.QuestionDoc;
import ir.mrmoein.quezapplication.model.document.StudentDoc;
import ir.mrmoein.quezapplication.model.document.TeacherDoc;
import ir.mrmoein.quezapplication.model.dto.*;
import ir.mrmoein.quezapplication.model.entity.*;

import java.io.IOException;
import java.util.List;

public interface DTOService {

    Student getRequestRegisterStudent(StudentRegisterRequest registerRequest) throws IOException;

    Teacher getRequestRegisterTeacher(TeacherRegisterRequest registerRequest) throws IOException;

    List<StatusDTO> concatList(List<StudentDoc> students , List<TeacherDoc> teachers);

    CoursesTeacherDTO getCoursesTeacherResponse(Course course);

    TeacherDoc getTeacherDoc(OutboxEvent outboxEvent);

    StudentDoc getStudentDoc(OutboxEvent outboxEvent);

    TeacherProfileDTO getProfileTeacher(Teacher teacher);

    StudentCardDTO getStudentCard(Student student);

    ExamDTO getExamDTO(Exam exam ,double score);

    Exam getExam(ExamDTO exam);

    QuestionDoc getSearchDoc(ExamQuestion question);

    ExamQuestionDTO getExamQuestionsDTO(ExamQuestion examQuestion);

    ExamQuestion getExamQuestion(RequestExamQuestionDTO dto , Course course , Exam exam);

    QuestionSearchDTO getQuestionSearchDTO(QuestionDoc questionDoc);

    CoursesStudentDTO getStudentCourseDTO(Course course);

    CoursePersonDTO getCoursePersonDTO(Course course);

    ExamQuestionStartDTO getExamQuestionStartDTO(ExamQuestion examQuestion);

    OptionDTO getOptionDTO(Option option);

    StudentsExamDTO getStudentsExamDTO(Student student);

    QuestionCorrectionDTO getQuestionCorrectionDTO(ExamQuestion question , AnswerQuestion answer , long studentExamId);

}
