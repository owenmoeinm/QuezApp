package ir.mrmoein.quezapplication.controller.teacher;

import ir.mrmoein.quezapplication.model.dto.*;
import ir.mrmoein.quezapplication.service.ExamService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/teacher/exam")
public class ExamController {

    private final ExamService service;
    private final Logger logger = LoggerFactory.getLogger(ExamController.class);

    @Autowired
    public ExamController(ExamService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ResponseEntity<List<ExamDTO>> getExams() {
        return new ResponseEntity<>(service.getExams(), HttpStatus.OK);
    }

    @GetMapping("/page")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public ModelAndView examPage() {
        return new ModelAndView("exam_page");
    }

    @PostMapping("/create")
    public ResponseEntity<?> createExam(@Valid @ModelAttribute RequestExamDTO exam) {
        ExamDTO createdExam = service.createExam(exam);
        if (createdExam == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("خطا در ایجاد آزمون");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExam);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteExam(@RequestParam String id) {
        if (service.removeExam(id)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/question")
    public ResponseEntity<List<ExamQuestionDTO>> getExamQuestion(@RequestParam String id) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(service.getQuestionsExam(id));
    }

    @GetMapping("/questionsPage")
    public ModelAndView questionsPage() {
        return new ModelAndView("exam_question");
    }

    @PostMapping("/generate")
    public ResponseEntity<ExamQuestionDTO> createQuestion(@ModelAttribute RequestExamQuestionDTO dto) {
        ExamQuestionDTO question = service.createQuestion(dto);
        if (question == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(question);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<QuestionSearchDTO>> searchExam(@RequestParam("value") String query) {
        List<QuestionSearchDTO> questionSearchDTOS = service.searchQuestion(query);
        return ResponseEntity.ok(questionSearchDTOS);
    }

    @PostMapping("/selected")
    public ResponseEntity<ExamQuestionDTO> selectExam(@RequestParam("id") String id, @RequestParam("exam") String exam) {
        ExamQuestionDTO examQuestionDTO = service.selectedQuestion(id, exam);
        return ResponseEntity.ok(examQuestionDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<ExamQuestionDTO> updateExam(@RequestParam("id") String id, @RequestParam("value") String value, @RequestParam("exam") String exam) {
        ExamQuestionDTO examQuestionDTO = service.updateScore(id, value, exam);
        return ResponseEntity.ok(examQuestionDTO);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeExam(@RequestParam("id") String id, @RequestParam("exam") String exam) {
        try {
            service.removeQuestion(id, exam);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/students_exam")
    public ModelAndView pageExam() {
        return new ModelAndView("students_exam");
    }

    @GetMapping("/student_collection")
    public ResponseEntity<List<StudentsExamDTO>> pageCollection(@RequestParam("exam") String exam) {
        List<StudentsExamDTO> studentsExams = service.getStudentsExams(exam);
        return ResponseEntity.ok(studentsExams);
    }

    @GetMapping("/seen_exam")
    public ModelAndView seenExam() {
        return new ModelAndView("seen_exam");
    }

    @PostMapping("/test_correction")
    public ResponseEntity<List<QuestionCorrectionDTO>> test_correction(@RequestBody ExamCorrectionRequestDTO requestDTO) {
        List<QuestionCorrectionDTO> questionCorrections = service.getQuestionCorrections(requestDTO);
        return ResponseEntity.ok(questionCorrections);
    }

    @PostMapping("/submit_correction")
    public ResponseEntity<?> correctionForm(@RequestBody List<RequestSubmitCorrection> requestDTO) {
        service.correctionForm(requestDTO);
        return ResponseEntity.ok().build();
    }

}
