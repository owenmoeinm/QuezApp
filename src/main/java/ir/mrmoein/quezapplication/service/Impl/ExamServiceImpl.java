package ir.mrmoein.quezapplication.service.Impl;

import ir.mrmoein.quezapplication.exception.NotFoundRequestException;
import ir.mrmoein.quezapplication.model.document.QuestionDoc;
import ir.mrmoein.quezapplication.model.dto.*;
import ir.mrmoein.quezapplication.model.entity.*;
import ir.mrmoein.quezapplication.repository.elastic.SearchQuestion;
import ir.mrmoein.quezapplication.repository.jpa.*;
import ir.mrmoein.quezapplication.service.ExamService;
import ir.mrmoein.quezapplication.util.DTOService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository repository;
    private final DTOService dtoService;
    private final CourseRepository courseRepository;
    private final ExamQuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final SearchQuestion searchQuestionRepository;
    private final StudentExamRepository studentExamRepository;
    private final Logger logger = LoggerFactory.getLogger(ExamServiceImpl.class);

    @Autowired
    public ExamServiceImpl(ExamRepository repository, DTOService dtoService, CourseRepository courseRepository, ExamQuestionRepository examQuestionRepository, OptionRepository optionRepository, SearchQuestion searchQuestionRepository, StudentExamRepository studentExamRepository) {
        this.repository = repository;
        this.dtoService = dtoService;
        this.courseRepository = courseRepository;
        this.questionRepository = examQuestionRepository;
        this.optionRepository = optionRepository;
        this.searchQuestionRepository = searchQuestionRepository;
        this.studentExamRepository = studentExamRepository;
    }

    @Override
    public List<ExamDTO> getExams() {
        List<Exam> exams = repository.findAll();
        return exams.stream().map((exam -> dtoService.getExamDTO(exam, -1.0))).toList();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ExamDTO createExam(RequestExamDTO exam) {
        try {
            Course course = courseRepository.findByName(exam.getCourse().replace("\"", "")).orElseThrow(() -> new NotFoundRequestException("Course not found!!!"));
            Exam build = Exam.builder()
                    .name(exam.getName())
                    .startDate(exam.getStartDate())
                    .description(exam.getDescription())
                    .endDate(exam.getEndDate())
                    .course(course)
                    .duration(exam.getDuration())
                    .build();
            Exam save = repository.save(build);
            return dtoService.getExamDTO(save, -1.0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NotFoundRequestException("could not create exam !!!");
        }
    }

    @Override
    public boolean removeExam(String id) {
        try {
            repository.deleteById(Long.decode(id));
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public List<ExamQuestionDTO> getQuestionsExam(String id) {
        try {
            Exam exam = repository.findById(Long.decode(id)).orElseThrow(() -> new NotFoundRequestException("Exam not found!!!"));
            return exam.getQuestions().stream().map(dtoService::getExamQuestionsDTO).toList();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NotFoundRequestException("could not get questions !!!");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ExamQuestionDTO createQuestion(RequestExamQuestionDTO dto) {
        try {
            Course course = courseRepository.findByName(dto.getCourse().replace("\"", ""))
                    .orElseThrow(() -> new NotFoundRequestException("Course not found!!!"));

            Exam exam = repository.getReferenceById(Long.decode(dto.getExam().replace("\"", "")));

            ExamQuestion examQuestion = dtoService.getExamQuestion(dto, course, exam);

            ExamQuestion question = questionRepository.save(examQuestion);

            List<Option> options = new LinkedList<>();
            AtomicReference<Boolean> flag = new AtomicReference<>(true);
            if (dto.getOptions() != null) {
                dto.getOptions().forEach((option) -> {
                    Option value = Option.builder().value(option).examQuestion(examQuestion).build();
                    Option save = optionRepository.save(value);
                    if (flag.get()) {
                        save.setCorrection(CorrectionType.RIGHT);
                        flag.set(false);
                    }
                    optionRepository.save(save);
                    options.add(value);
                });
            }
            question.setOptions(options);
            ExamQuestion result = questionRepository.save(examQuestion);
            searchQuestionRepository.save(dtoService.getSearchDoc(examQuestion));
            course.setQuestions(result);
            exam.addQuestions(examQuestion);
            repository.save(exam);
            return dtoService.getExamQuestionsDTO(result);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NotFoundRequestException("could not create question !!!");
        }
    }

    @Override
    public List<QuestionSearchDTO> searchQuestion(String query) {
        List<QuestionDoc> questions = searchQuestionRepository.findByTitleContaining(query);

        return questions.stream().map(dtoService::getQuestionSearchDTO).toList();
    }

    @Override
    public ExamQuestionDTO selectedQuestion(String id, String examId) {
        try {
            ExamQuestion examQuestion = questionRepository.findById(Long.decode(id))
                    .orElseThrow(() -> new NotFoundRequestException("Question not found!!!"));

            Exam exam = repository.findById(Long.decode(examId.replace("\"", "")))
                    .orElseThrow(() -> new NotFoundRequestException("Exam not found!!!"));

            examQuestion.addExam(exam);
            exam.addQuestions(examQuestion);
            repository.save(exam);
            return dtoService.getExamQuestionsDTO(exam.getQuestions().stream()
                    .filter((e) ->
                            e.getId().equals(examQuestion.getId())).findAny()
                    .orElseThrow(() -> new NotFoundRequestException("Question not found!!!")));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NotFoundRequestException("could not get question !!!");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ExamQuestionDTO updateScore(String id, String score, String exam) {
        try {
            Exam exam1 = repository.findById(Long.decode(exam.replace("\"", ""))).orElseThrow(() -> new NotFoundRequestException("Exam not found!!!"));
            ExamQuestion examQuestion = exam1.getQuestions().stream().filter((e) -> Objects.equals(e.getId(), Long.decode(id))).findAny().orElseThrow(() -> new NotFoundRequestException("Question not found!!!"));
            examQuestion.setScore(Double.parseDouble(score));
            repository.save(exam1);
            return dtoService.getExamQuestionsDTO(examQuestion);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new NotFoundRequestException("could not update score !!!");
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void removeQuestion(String id, String exam) {
        try {
            Exam exam1 = repository.findById(Long.decode(exam.replace("\"", "")))
                    .orElseThrow(() -> new NotFoundRequestException("Exam not found!!!"));

            ExamQuestion examQuestion = questionRepository.findById(Long.decode(id))
                    .orElseThrow(() -> new NotFoundRequestException("Question not found!!!"));

            exam1.getQuestions().removeIf(q -> q.getId().equals(examQuestion.getId()));
            repository.deleteExamQuestionRelation(examQuestion.getId(), exam1.getId());
            repository.save(exam1);
        } catch (Exception e) {
            logger.error("Error while removing question: " + e.getMessage());
            throw new NotFoundRequestException("Could not remove question !!!");
        }
    }

    @Override
    public List<StudentsExamDTO> getStudentsExams(String exam) {
        Exam exam1 = repository.findById(Long.decode(exam.replace("\"", "")))
                .orElseThrow(() -> new NotFoundRequestException("Exam not found!!!"));

        return exam1.getStudents().stream().map(dtoService::getStudentsExamDTO).toList();
    }

    @Override
    public List<QuestionCorrectionDTO> getQuestionCorrections(ExamCorrectionRequestDTO requestDTO) {
        Exam exam = repository.findById(Long.decode(requestDTO.getExamId()))
                .orElseThrow(() -> new NotFoundRequestException("Exam not found!!!"));

        Student student = exam.getStudents().stream().filter((s -> s.getId().equals(UUID.fromString(requestDTO.getStudentId())))).findFirst()
                .orElseThrow(() -> new NotFoundRequestException("student not found in exam !!!"));

        StudentExam studentExam = studentExamRepository.findByStudentAndExam(student, exam)
                .orElseThrow(() -> new NotFoundRequestException("student not found in exam !!!"));

        List<ExamQuestion> questions = exam.getQuestions();
        List<AnswerQuestion> answerQuestions = studentExam.getAnswerQuestions();

        List<QuestionCorrectionDTO> result = new LinkedList<>();
        questions.forEach((question -> {
            AnswerQuestion answerQuestion = answerQuestions.stream().filter(a -> a.getExamQuestion().equals(question)).findFirst()
                    .orElseThrow(() -> new NotFoundRequestException("question not found!!!"));
            if (answerQuestion.getVisit().equals(Visit.UNSEEN)) {
                result.add(dtoService.getQuestionCorrectionDTO(question, answerQuestion, studentExam.getId()));
            }
        }));

        return result;
    }

    @Override
    public void correctionForm(List<RequestSubmitCorrection> correction) {
        correction.forEach((correct -> {
            StudentExam studentExam = studentExamRepository.findById(Long.decode(correct.getExamStudentID()))
                    .orElseThrow(() -> new NotFoundRequestException("student not found!!!"));

            studentExam.getAnswerQuestions().forEach((answerQuestion -> {
                if (answerQuestion.getId().equals(Long.decode(correct.getAnswerId()))) {
                    answerQuestion.setGrade(Double.parseDouble(correct.getScore()));
                    studentExam.setGrade(studentExam.getGrade() + answerQuestion.getGrade());
                }else {
                    answerQuestion.setGrade(0.0);
                }
            }));
            studentExam.setVisit(Visit.SEEN);
            studentExamRepository.save(studentExam);
            Exam exam = studentExam.getExam();
            repository.save(exam);
        }));
    }


}
