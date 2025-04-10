package ir.mrmoein.quezapplication.service.Impl;

import ir.mrmoein.quezapplication.exception.NotAccessException;
import ir.mrmoein.quezapplication.exception.NotFoundRequestException;
import ir.mrmoein.quezapplication.model.dto.*;
import ir.mrmoein.quezapplication.model.entity.*;
import ir.mrmoein.quezapplication.repository.elastic.SearchStudent;
import ir.mrmoein.quezapplication.repository.jpa.*;
import ir.mrmoein.quezapplication.service.StudentService;
import ir.mrmoein.quezapplication.util.DTOService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final SearchStudent searchRepository;
    private final DTOService dtoService;
    private final TeacherRepository teacherRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final ExamRepository examRepository;
    private final OptionRepository optionRepository;
    private final AnswerQuestionRepository answerQuestionRepository;
    private final StudentExamRepository studentExamRepository;
    private final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);


    @Autowired
    public StudentServiceImpl(UserRepository userRepository, StudentRepository studentRepository, SearchStudent searchRepository, DTOService dtoService, TeacherRepository teacherRepository, ExamQuestionRepository examQuestionRepository, ExamRepository examRepository, OptionRepository optionRepository, AnswerQuestionRepository answerQuestionRepository, StudentExamRepository studentExamRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.searchRepository = searchRepository;
        this.dtoService = dtoService;
        this.teacherRepository = teacherRepository;
        this.examQuestionRepository = examQuestionRepository;
        this.examRepository = examRepository;
        this.optionRepository = optionRepository;
        this.answerQuestionRepository = answerQuestionRepository;
        this.studentExamRepository = studentExamRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean remove(String nationalCode) {
        Student student = studentRepository.findByNationalCode(nationalCode)
                .orElseThrow(() -> new NotFoundRequestException("Student Not Found !!!"));
        studentRepository.deleteStudentFromJoinTable(student.getId());
        studentRepository.deleteByNationalCode(student.getNationalCode());
        searchRepository.deleteByNationalCode(student.getNationalCode());
        return true;
    }

    @Override
    public List<CoursesStudentDTO> getCourses(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("user not found!!! "));

        Student student = studentRepository.findByUserId(user)
                .orElseThrow(() -> new NotFoundRequestException("Student Not Found !!!"));

        return student.getCourses().stream().map((dtoService::getStudentCourseDTO)).toList();
    }

    @Override
    public CoursePersonDTO getMyCourseDetails(String course, UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("user not found!!! "));

        Student student = studentRepository.findByUserId(user)
                .orElseThrow(() -> new NotFoundRequestException("Student Not Found !!!"));

        Course target = student.getCourses().stream().filter((e) -> e.getId().equals(Long.decode(course))).findFirst()
                .orElseThrow(() -> new NotFoundRequestException("Course Not Found !!!"));

        double score;

        List<StudentExam> studentExams = studentExamRepository.findAllByStudent(student);
        score = studentExams.stream().filter(studentExam -> studentExam.getExam().getCourse().getId().equals(target.getId()) &&
                studentExam.getVisit().equals(Visit.SEEN)).findFirst().map(StudentExam::getGrade).orElse(-1.0);

        CoursePersonDTO coursePersonDTO = dtoService.getCoursePersonDTO(target);
        List<ExamDTO> list = target.getExams().stream().map((exam)->dtoService.getExamDTO(exam , score)).toList();
        Teacher teacher = teacherRepository.findById(target.getTeacher().getId()).orElseThrow(() -> new NotFoundRequestException("Teacher Not Found !!!"));
        coursePersonDTO.setTeacher(teacher.getName() + " " + teacher.getLastName());
        coursePersonDTO.setExams(list);
        return coursePersonDTO;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ExamStartDTO getMyExamDetails(String examId, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("user not found!!! "));

        Student student = studentRepository.findByUserId(user)
                .orElseThrow(() -> new NotFoundRequestException("Student Not Found !!!"));

        Exam exam = examRepository.findById(Long.decode(examId))
                .orElseThrow(() -> new NotFoundRequestException("Exam Not Found !!!"));

        exam.addStudent(student);
        Optional<StudentExam> studentExam = studentExamRepository.findByStudentId(student.getId());
        if (studentExam.isPresent()) {
            throw new NotAccessException("شما قبلا در این امتحان شرکت کرده اید !!!");
        } else {
            StudentExam build = StudentExam.builder()
                    .exam(exam)
                    .student(student)
                    .visit(Visit.UNSEEN)
                    .build();
            StudentExam save = studentExamRepository.save(build);
            List<ExamQuestionStartDTO> list = exam.getQuestions().stream().map((e) -> {
                ExamQuestionStartDTO examQuestionStartDTO = dtoService.getExamQuestionStartDTO(e);
                if ("MULTIPLE_CHOICE".equals(e.getQuestionType().name())) {
                    examQuestionStartDTO.setOptions(e.getOptions().stream().map(dtoService::getOptionDTO).toList());
                }
                return examQuestionStartDTO;
            }).toList();
            examRepository.save(exam);
            return ExamStartDTO.builder()
                    .id(String.valueOf(exam.getId()))
                    .examName(exam.getName())
                    .studentExam(String.valueOf(save.getId()))
                    .duration(exam.getDuration())
                    .examQuestions(list)
                    .build();
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void submitExam(ExamSubmitDTO examSubmitDTO) {
        StudentExam se = studentExamRepository.findById(Long.decode(examSubmitDTO.getStudentExam()))
                .orElseThrow(() -> new NotFoundRequestException("Exam Not Found !!!"));

        List<AnswerQuestion> answers = new ArrayList<>();
        Map<String, String> submittedAnswers = examSubmitDTO.getAnswers();

        int flagInfoExam = 0;
        for (Map.Entry<String, String> entry : submittedAnswers.entrySet()) {
            String questionId = entry.getKey();
            String answerID = entry.getValue();

            ExamQuestion examQuestion = examQuestionRepository.findById(Long.decode(questionId))
                    .orElseThrow(() -> new NotFoundRequestException("Question Not Found !!!"));

            QuestionType questionType = examQuestion.getQuestionType();

            AnswerQuestion build = AnswerQuestion.builder()
                    .studentExam(se)
                    .examQuestion(examQuestion)
                    .visit(Visit.UNSEEN).build();

            if ("MULTIPLE_CHOICE".equals(questionType.name())) {
                Option option = optionRepository.findById(Long.decode(answerID))
                        .orElseThrow(() -> new NotFoundRequestException("Option Not Found !!!"));

                examQuestion.addOption(option);
                flagInfoExam++;
                double grade = CorrectionType.RIGHT.equals(option.getCorrection()) ? examQuestion.getScore() : 0.0;
                build.setAnswer(answerID);
                build.setGrade(grade);
                se.setGrade(se.getGrade() + grade);
                build.setVisit(Visit.SEEN);

            } else if ("SHORT_ANSWER".equals(questionType.name())) {
                build.setAnswer(answerID);
                build.setGrade(examQuestion.getScore());
            }

            answerQuestionRepository.save(build);
            answers.add(build);

            examQuestionRepository.save(examQuestion);
        }
        se.setSendTime(LocalTime.now());
        if (flagInfoExam == submittedAnswers.size()){
            se.setVisit(Visit.SEEN);
        }

        se.setAnswerQuestions(answers);
        studentExamRepository.save(se);
    }




}
